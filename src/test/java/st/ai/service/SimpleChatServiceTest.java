package st.ai.service;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SimpleChatServiceTest {

    @Resource
    private SimpleChatService service;

    /**
     * 简单调用
     */
    @Test
    void testChat(){
        service.chat("你好，我是程序员st，我想了解下java中的各种锁和锁升级机制");
    }


    /**
     * 多模态调用(需模型支持)
     */
    @Test
    void chatWithMessage() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述图片"),
                ImageContent.from("https://cdn.deepseek.com/logo.png?x-image-process=image%2Fresize%2Cw_1920")
        );
        service.chatWithMessage(userMessage);
    }
}