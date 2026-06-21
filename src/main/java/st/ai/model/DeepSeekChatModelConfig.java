package st.ai.model;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Setter
@Configuration
@ConfigurationProperties(prefix = "deepseek.chat-model")
public class DeepSeekChatModelConfig {
    private String modelName;

    private String apiKey;

    private String baseUrl;

    @Resource
    private ChatModelListener chatModelListener;

    @Primary
    @Bean(name = "deepseekApi")
    public ChatModel myDeepSeekChatModel() {
        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .listeners(List.of(chatModelListener))
                .build();
    }

    @Primary
    @Bean(name = "deepseekApiStream")
    public StreamingChatModel myDeepSeekStreamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .listeners(List.of(chatModelListener))
                .build();
    }
}
