package st.ai.web;

import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import st.ai.mcp.ChatWithMcpAiService;

@RestController
@RequestMapping("/ai")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class MyAiServiceApiController {

    @Resource
    private ChatWithMcpAiService chatWithMcpAiService;

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatWithMcpStream(String messageId, String userMessage) {
        return chatWithMcpAiService.chatWithMcpStream(messageId, userMessage)
                .map(message -> ServerSentEvent.<String>builder().data(message).build());
    }
}
