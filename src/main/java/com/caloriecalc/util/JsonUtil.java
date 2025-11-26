
package com.caloriecalc.util;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
public final class JsonUtil {
  private static final ObjectMapper MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL);
  private JsonUtil(){}
  public static ObjectMapper mapper(){ return MAPPER; }
  public static String toJson(Object o){
    try { return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(o); }
    catch (JsonProcessingException e){ throw new RuntimeException(e); }
  }
}
