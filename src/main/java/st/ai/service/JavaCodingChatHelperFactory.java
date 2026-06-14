package st.ai.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

// @Configuration
public class JavaCodingChatHelperFactory {

    @Resource
    private ChatModel chatModel;

    /**
     * 使用反射和动态代理实例化AiService接口
     * @return
     */
    @Bean
    public JavaCodingHelperAiService javaCodingHelperAiService(){
        // return AiServices.create(JavaCodingHelperAiService.class, deepseekChatModel);
        // 生成会话记忆 最大10个信息
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(JavaCodingHelperAiService.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory) // 会话记忆
                .build();
    }
}
