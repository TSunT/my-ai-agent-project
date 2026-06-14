package st.ai.service;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.*;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import st.ai.entity.CityFood;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatBotAiServiceTest {

    @Resource
    private ChatModel deepseekChatModel;

    @Resource
    private ChatBotAiService chatBotAiService;

    /**
     * 测试会话记忆
     */
    @Test
    void chatWithMemory(){
        String memoryId = String.valueOf(UUID.randomUUID());
        String firstResult = chatBotAiService.chat(memoryId,"你好，我是Tiny Stone，今年18岁，来自南京。");
        System.out.println(firstResult);
        String secondResult = chatBotAiService.chat(memoryId,"我刚刚说我是谁？");
        System.out.println(secondResult);
    }

    /**
     * 使用prompt 来结构化输出
     */
    @Test
    void chatForCityFood(){
        String memoryId = String.valueOf(UUID.randomUUID());
        //  LangChain4j AI Services 的提示词驱动方式（通过 CityFood 返回类型让框架自动生成 JSON 提示词并解析）
        CityFood cityFood = chatBotAiService.chatForCityFood(memoryId, "你好，我是Tiny Stone，今年18岁，来自南京。我想了解一些杭州的特色美食");
        System.out.println(cityFood.foodList());
    }

    /**
     * 使用Json Schema 配合 ChatModel 来实现结构化输出
     * deepseek 不支持该方式
     */
    @Test
    void chatWithResponseFormat(){
        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .rootElement(JsonObjectSchema.builder()
                                .addStringProperty("cityName", "the name of target city")
                                .addProperty("foodList", JsonArraySchema.builder().items(JsonStringSchema.builder().build()).build())
                                .build())
                        .build())
                .build();
        UserMessage userMessage = UserMessage.from("你好，我是Tiny Stone，今年18岁，来自南京。我想了解一些杭州的特色美食");
        ChatRequest chatRequest = ChatRequest.builder()
                .responseFormat(responseFormat)
                .messages(userMessage)
                .build();
        ChatResponse chatResponse = deepseekChatModel.chat(chatRequest);
        String text = chatResponse.aiMessage().text();
        System.out.println(text);
    }

}