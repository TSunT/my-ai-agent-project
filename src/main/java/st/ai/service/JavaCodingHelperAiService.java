package st.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * 使用AiSerevice方式调用langchain4j
 * 注意：这里类型必须是接口
 * @AiService 注解需要引入使用langchain4j-spring-boot-stater，使用注解后无需创建Factory配置类，不过，要实现复杂业务的情况使用注解不够灵活
 */
// @AiService
public interface JavaCodingHelperAiService {

    /**
     * 使用SystemMessage注解指定系统提示词
     * @param userMessage
     * @return
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMessage);
}
