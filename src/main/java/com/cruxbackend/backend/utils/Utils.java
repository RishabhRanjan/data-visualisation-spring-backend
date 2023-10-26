package com.cruxbackend.backend.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.cruxbackend.backend.pojo.MessageBlock;
import com.cruxbackend.backend.pojo.Template;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
  @Value("${openai.api.key}")
  private String openAiKey;

  public static String convertCsvToJsonString(MultipartFile file) throws Exception {
    CsvMapper csvMapper = new CsvMapper();
    CsvSchema schema = CsvSchema.emptySchema().withHeader();
    List<Object> data = csvMapper.readerFor(Map.class).with(schema).readValues(file.getInputStream()).readAll();
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(data);
  }

  public static String writeToJson(Object object) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper.writeValueAsString(object);
  }

  public static Object readAsJson(String object, TypeReference<List<MessageBlock>> typeReference) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper.readValue(object, typeReference);
  };
}
