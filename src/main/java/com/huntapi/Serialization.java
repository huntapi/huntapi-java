package com.huntapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Serialization {
  static final ObjectMapper MAPPER = new ObjectMapper();
  private static final Pattern PATH_PARAM = Pattern.compile("\\{([^}]+)\\}");
  /** The API reads the global parameters from the query string, whatever the method. */
  private static final List<String> QUERY_STRING_PARAMS = List.of("fields", "is_async", "soft_fail");

  private Serialization() {}

  record Serialized(String path, String query, Map<String, Object> body) {}

  /**
   * Fills {name} path segments, then builds the query string (GET) or JSON body (POST). On POST the global
   * parameters still go in the query string.
   */
  static Serialized serialize(String method, String pathTemplate, Map<String, ?> params) {
    Map<String, Object> remaining = new LinkedHashMap<>();
    params.forEach((name, value) -> {
      if (value != null) {
        remaining.put(name, value);
      }
    });
    Matcher matcher = PATH_PARAM.matcher(pathTemplate);
    StringBuilder path = new StringBuilder();
    while (matcher.find()) {
      String name = matcher.group(1);
      Object value = remaining.remove(name);
      if (value == null) {
        throw new HuntApiSerializationException("The \"" + name + "\" path parameter is required.", name);
      }
      matcher.appendReplacement(path, Matcher.quoteReplacement(encode(wireString(name, value))));
    }
    matcher.appendTail(path);
    if ("POST".equals(method)) {
      List<String> globals = new ArrayList<>();
      for (String name : QUERY_STRING_PARAMS) {
        Object value = remaining.remove(name);
        if (value != null) {
          globals.add(encode(name) + "=" + encode(wireString(name, value)));
        }
      }
      return new Serialized(path.toString(), String.join("&", globals), remaining);
    }
    List<String> pairs = new ArrayList<>();
    remaining.forEach((name, value) -> {
      if (value instanceof Collection<?> items) {
        for (Object item : items) {
          if (item != null) {
            pairs.add(encode(name) + "=" + encode(wireString(name, item)));
          }
        }
      } else {
        pairs.add(encode(name) + "=" + encode(wireString(name, value)));
      }
    });
    return new Serialized(path.toString(), String.join("&", pairs), null);
  }

  static String wireString(String name, Object value) {
    if (value instanceof String text) {
      return text;
    }
    if (value instanceof Boolean flag) {
      return flag ? "true" : "false";
    }
    if (value instanceof Double || value instanceof Float) {
      double number = ((Number) value).doubleValue();
      if (Double.isNaN(number) || Double.isInfinite(number)) {
        throw new HuntApiSerializationException("The \"" + name + "\" parameter must be a finite number.", name);
      }
      return BigDecimal.valueOf(number).stripTrailingZeros().toPlainString();
    }
    if (value instanceof BigDecimal decimal) {
      return decimal.stripTrailingZeros().toPlainString();
    }
    if (value instanceof Number number) {
      return number.toString();
    }
    if (value instanceof Map<?, ?> || value instanceof Collection<?>) {
      return new String(toJson(value), StandardCharsets.UTF_8);
    }
    return String.valueOf(value);
  }

  static byte[] toJson(Object value) {
    try {
      return MAPPER.writeValueAsBytes(value);
    } catch (JsonProcessingException e) {
      throw new HuntApiSerializationException("The request body could not be serialized as JSON.", null, e);
    }
  }

  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
  }
}
