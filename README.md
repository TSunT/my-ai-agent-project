## application-local.yml Template
```yaml
spring:
  application:
    name: my-ai-project
server:
  port: 8081
  servlet:
    context-path: /api
deepseek:
  chat-model:
    base-url: https://api.deepseek.com
    api-key: <YOU_KEY>
    model-name: <MODEL_NAME>
kimi:
  chat-model:
    base-url: https://api.moonshot.cn/v1
    api-key: <YOU_KEY>
    model-name: <MODEL_NAME>
```