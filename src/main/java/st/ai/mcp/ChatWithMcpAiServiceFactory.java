package st.ai.mcp;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import st.ai.rag.MyFirstRagAiService;

@Configuration
public class ChatWithMcpAiServiceFactory {

    @Resource
    private ChatModel deepseekChatModel;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Resource
    @Qualifier("deepseekApiStream")
    private StreamingChatModel deepseekApiStream;

    @Bean
    public ChatWithMcpAiService createChatWithMcpAiService() {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        ChatWithMcpAiService service = AiServices.builder(ChatWithMcpAiService.class)
                .chatModel(deepseekChatModel)
                .streamingChatModel(deepseekApiStream)
                .chatMemory(chatMemory)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .toolProvider(mcpToolProvider)
                .build();
        return service;
    }
}
