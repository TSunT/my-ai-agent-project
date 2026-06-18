package st.ai.rag;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface MyFirstRagAiService {
    /**
     * 使用SystemMessage注解指定系统提示词
     * @param userMessage
     * @return
     */
    @SystemMessage({"你是一个财务管理系统的智能助手，帮助用户了解相关系统的操作方式。"})
    Result<String> chatWithRag(@MemoryId String messageId, @UserMessage String userMessage);
}
