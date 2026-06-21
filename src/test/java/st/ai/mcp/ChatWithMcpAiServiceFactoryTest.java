package st.ai.mcp;

import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatWithMcpAiServiceFactoryTest {

    @Resource
    private ChatWithMcpAiService chatWithMcpAiService;

    @Test
    void chatWithMcp(){
        Result<String> result = chatWithMcpAiService.chatWithMcp(UUID.randomUUID().toString(),"上海的今天天气如何？");
        String content = result.content();
        List<Content> sources = result.sources();
        System.out.println(content);
        System.out.println(sources);
    }
}