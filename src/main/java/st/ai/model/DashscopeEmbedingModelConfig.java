package st.ai.model;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Setter
@Configuration
@ConfigurationProperties(prefix = "dashscope.embedding-model")
public class DashscopeEmbedingModelConfig {

    private String modelName;

    private String apiKey;

    private String baseUrl;

    @Primary
    @Bean(name = "dashscopeEmbeding")
    public EmbeddingModel dashscopeEmbedingModel(){
        return OpenAiEmbeddingModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .build();
    }
}
