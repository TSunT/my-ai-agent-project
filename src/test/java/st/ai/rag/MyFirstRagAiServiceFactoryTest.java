package st.ai.rag;

import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyFirstRagAiServiceFactoryTest {

    @Resource
    private MyFirstRagAiService ragAiService;

    @Test
    void chatWithRag(){
        Result<String> result = ragAiService.chatWithRag(UUID.randomUUID().toString(),"风险指标编码规则是什么？");
        String content = result.content();
        List<Content> sources = result.sources();
        System.out.println(content);
        System.out.println(sources);
    }
}