package st.ai.rag;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyFirstRagAiServiceFactory {

    @Resource
    private ChatModel deepseekChatModel;
    @Resource
    private ContentRetriever contentRetriever;

    @Bean
    public MyFirstRagAiService myFirstRagAiService(){
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        MyFirstRagAiService service = AiServices.builder(MyFirstRagAiService.class)
                .chatModel(deepseekChatModel)
                .chatMemory(chatMemory)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(contentRetriever) // rag
                .build();
        return service;
    }
}
