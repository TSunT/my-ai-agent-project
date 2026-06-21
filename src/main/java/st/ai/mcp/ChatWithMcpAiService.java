package st.ai.mcp;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ChatWithMcpAiService {
    /**
     * 使用SystemMessage注解指定系统提示词
     * @param userMessage
     * @return
     */
    @SystemMessage({"你是一个通勤的智能助手，帮助用户了解相关的通勤信息。"})
    Result<String> chatWithMcp(@MemoryId String messageId, @UserMessage String userMessage);
}
