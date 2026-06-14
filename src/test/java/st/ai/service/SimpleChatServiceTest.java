package st.ai.service;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URL;
import java.util.Base64;

@SpringBootTest
class SimpleChatServiceTest {

    @Resource
    private SimpleChatService service;

    /**
     * 简单调用
     */
    @Test
    void testChat(){
        service.chat("你好，我是程序员st，我想了解下java中常用的数据类型");
    }


    /**
     * 多模态调用(需模型支持)
     * Kimi API 不支持外部图片 URL，需将图片转为 Base64 编码
     */
    @Test
    void chatWithMessage() throws Exception {
        // 下载图片并转为 Base64（Kimi 多模态 API 只接受 base64 格式）
        byte[] imageBytes = new URL("https://cdn.deepseek.com/logo.png").openStream().readAllBytes();
        String base64 = Base64.getEncoder().encodeToString(imageBytes);

        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述图片"),
                ImageContent.from(base64, "image/png")
        );
        service.chatWithMessage(userMessage);
    }
}