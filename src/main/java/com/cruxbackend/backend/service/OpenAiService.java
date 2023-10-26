package com.cruxbackend.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cruxbackend.backend.pojo.Template;
import com.cruxbackend.backend.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

@Service
public class OpenAiService {

  @Value("${openai.api.key}")
  private String openAiKey;

  public static String requestBodyToString(RequestBody requestBody) throws IOException {
    Buffer buffer = new Buffer();
    requestBody.writeTo(buffer);
    return buffer.readUtf8();
  }

  public JsonNode parseOpenAiResponse(JsonNode response) throws JsonMappingException, JsonProcessingException {
    JsonNode choicesNode = response.get("choices");

    if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
      JsonNode functionCallNode = choicesNode.get(0).path("message").path("function_call");
      String argumentsString = functionCallNode.get("arguments").asText();
      JsonNode argumentsJsonNode = (new ObjectMapper()).readTree(argumentsString);

      System.out.println("Function Call Node: " + functionCallNode.get("arguments").asText());
      System.out.println("Arguments JSON String: " + argumentsString);
      System.out.println("Arguments JSON Node: " + argumentsJsonNode);

      return argumentsJsonNode;
    }

    System.out.println("Check failed");
    return null;
  }

  public JsonNode callOpenAIChatCompletionsAPI(String query, String datasetSnippet) throws Exception {
    Map<String, Object> jsonBody = new HashMap<>();

    Map<String, Object> functionCall = new HashMap<>();
    functionCall.put("name", "getChartsConfig");

    jsonBody.put("model", "openai/gpt-4");
    jsonBody.put("messages", Arrays.asList(new HashMap<String, Object>() {
      {
        put("role", "system");
        put("content", Template.instructionsv2 + datasetSnippet);
      }
    }, new HashMap<String, Object>() {
      {
        put("role", "user");
        put("content", query);
      }
    }));
    jsonBody.put("functions", Template.functionsDesc());
    jsonBody.put("function_call", functionCall);
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    clientBuilder.connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS);
    clientBuilder.readTimeout(60, java.util.concurrent.TimeUnit.SECONDS);
    OkHttpClient client = clientBuilder.build();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(
        Utils.writeToJson(jsonBody),
        mediaType);

    Request request = new Request.Builder()
        .url("https://openrouter.ai/api/v1/chat/completions")
        .addHeader("Authorization", "Bearer " + openAiKey)
        .addHeader("HTTP-Referer", "http://localhost:8080")
        .addHeader("Content-Type", "application/json")
        .method("POST", body)
        .build();
    try {
      Response response = client.newCall(request).execute();
      System.out.println("Preprocessed response :" + response);
      if (!response.isSuccessful()) {
        String jsonResponse = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        return jsonNode;
      } else {
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
