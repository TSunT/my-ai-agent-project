package st.ai.service;

import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JavaCodingHelperAiServiceTest {

    @Resource
    private JavaCodingHelperAiService javaCodingHelperAiService;

    @Test
    void chat(){
        String chatResult = javaCodingHelperAiService.chat("你好，我是程序员st，我想了解下java中StringBuilder 和StringBuffer的区别。");
        System.out.println(chatResult);
    }
}