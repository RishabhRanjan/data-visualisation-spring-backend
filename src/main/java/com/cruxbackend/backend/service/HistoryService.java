package com.cruxbackend.backend.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cruxbackend.backend.model.History;
import com.cruxbackend.backend.pojo.Message;
import com.cruxbackend.backend.pojo.MessageBlock;
import com.cruxbackend.backend.pojo.Message.MessageAction;
import com.cruxbackend.backend.pojo.Message.MessageType;
import com.cruxbackend.backend.pojo.Message.RoleType;
import com.cruxbackend.backend.repo.HistoryRepository;
import com.cruxbackend.backend.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HistoryService {
  @Autowired
  private HistoryRepository repo;

  @Autowired
  private OpenAiService openAi;

  public History getHistory(Long cid) {
    History history = repo.findByCid(cid);
    return history;
  }

  public History createHistory(Long cid, MultipartFile csv) {
    History newHistory = new History();
    newHistory.setCid(cid);
    try {
      String fileName = csv.getOriginalFilename();
      String data = Utils.convertCsvToJsonString(csv);
      newHistory.setCsv("{ \"filename\": \"" + fileName + "\", \"data\": " + data + " }");

      MessageBlock messageBlock = new MessageBlock();
      messageBlock.setDate(System.currentTimeMillis());

      Message message = new Message(System.currentTimeMillis(), MessageType.CSV_UPLOAD, RoleType.ASSISTANT, null,
          UUID.randomUUID());
      message.setTime(System.currentTimeMillis());
      message.setRole(RoleType.ASSISTANT);
      message.setType(MessageType.CSV_UPLOAD);

      List<Message> messageList = new ArrayList<Message>();
      messageList.add(message);

      messageBlock.setThread(messageList);

      List<MessageBlock> historyList = new ArrayList<MessageBlock>();
      historyList.add(messageBlock);

      newHistory.setHistory(Utils.writeToJson(historyList));

    } catch (Exception e) {
      return null;
    }
    return repo.save(newHistory);
  }

  public History addThread(Long id, String query) {
    Optional<History> historyOptional = repo.findById(id);
    if (historyOptional.isPresent()) {
      History history = historyOptional.get();
      String csv = history.getCsv();

      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.findAndRegisterModules();

      try {
        List<MessageBlock> hList = objectMapper.readValue(history.getHistory(),
            new TypeReference<List<MessageBlock>>() {
            });

        long today = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String todayFormatted = dateFormat.format(new Date(today));

        Optional<MessageBlock> todayBlock = hList.stream()
            .filter(node -> todayFormatted.equals(dateFormat.format(new Date(node.getDate())))).findFirst();
        Map<String, Object> dataMap = objectMapper.readValue(csv, new TypeReference<Map<String, Object>>() {
        });

        Object dataObject = dataMap.get("data");
        List<Object> firstTwoRows = new ArrayList<>();
        if (dataObject instanceof List) {
          List<Object> dataList = (List<Object>) dataObject;
          firstTwoRows = dataList.subList(0, 2);
        }

        JsonNode openAiResponse = openAi.callOpenAIChatCompletionsAPI(query, Utils.writeToJson(firstTwoRows));
        System.out.println("Response from openai :" + openAiResponse);

        if (openAiResponse.isNull()) {
          return null;
        }

        if (todayBlock.isPresent()) {
          MessageBlock currentBlock = todayBlock.get();

          List<String> content = new ArrayList<String>();
          content.add(query);

          Message newThreadElement = new Message(System.currentTimeMillis(), MessageType.QUERY, RoleType.HUMAN,
              content, UUID.randomUUID());

          List<Message> currentThread = currentBlock.getThread();
          currentThread.add(newThreadElement);

          JsonNode responseContent = openAi.parseOpenAiResponse(openAiResponse);
          Message resMessage = new Message(System.currentTimeMillis(), MessageType.CHART, RoleType.ASSISTANT,
              responseContent, UUID.randomUUID());

          currentThread.add(resMessage);
        } else {
          List<String> content = new ArrayList<String>();
          content.add(query);

          MessageBlock newThread = new MessageBlock();

          Message message = new Message(System.currentTimeMillis(), MessageType.QUERY, RoleType.HUMAN, content,
              UUID.randomUUID());

          List<Message> messageList = new ArrayList<Message>();
          messageList.add(message);

          JsonNode responseContent = openAi.parseOpenAiResponse(openAiResponse);

          Message resMessage = new Message(System.currentTimeMillis(), MessageType.CHART, RoleType.ASSISTANT,
              responseContent, UUID.randomUUID());

          messageList.add(resMessage);

          newThread.setThread(messageList);

          hList.add(newThread);
        }
        history.setHistory(Utils.writeToJson(hList));
        return repo.save(history);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public void addFeedback(Long hid, Long date, UUID chatId, String action, String value) {
    Optional<History> historyOptional = repo.findById(hid);

    if (historyOptional.isPresent()) {
      ObjectMapper objectMapper = new ObjectMapper();
      History history = historyOptional.get();
      try {
        List<MessageBlock> hList = objectMapper.readValue(history.getHistory(),
            new TypeReference<List<MessageBlock>>() {
            });

        Optional<Message> messageOptional = hList.stream()
            .filter(block -> block.getDate().equals(date))
            .flatMap(block -> block.getThread().stream())
            .filter(message -> message.getUuid().equals(chatId))
            .findFirst();

        if (messageOptional.isPresent()) {
          Message message = messageOptional.get();
          if (MessageAction.valueOf(action) == MessageAction.LIKED) {
            message.setLiked(Integer.valueOf(value));
          }
          if (MessageAction.valueOf(action) == MessageAction.FEEDBACK) {
            message.setFeedback(value);
          }

          history.setHistory(Utils.writeToJson(hList));
          
          repo.save(history);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
