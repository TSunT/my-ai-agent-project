package st.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import st.ai.entity.CityFood;

/**
 * 聊天机器人service
 */
public interface ChatBotAiService {

    @SystemMessage(fromResource = "chatbot-system-prompt.txt")
    String chat(@MemoryId String memoryId, @UserMessage String message);

    /**
     * 格式化输出
     * @param memoryId
     * @param message
     * @return
     */
    @SystemMessage(fromResource = "chatbot-system-prompt.txt")
    CityFood chatForCityFood(@MemoryId String memoryId, @UserMessage String message);
}
