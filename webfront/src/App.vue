<script setup>
import { ref, reactive, nextTick, onMounted, computed } from 'vue'
import MarkdownRenderer from './components/MarkdownRenderer.vue'

// Backend base URL — use direct URL for SSE to avoid Vite proxy buffering
const BACKEND_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081'

// --- State ---
const chatId = ref('')
const inputMessage = ref('')
const isLoading = ref(false)
const streamingIndex = ref(-1) // index of the message currently streaming
const messages = ref([])
const messagesContainer = ref(null)

// --- Helpers ---
function generateId() {
  return 'chat_' + Date.now() + '_' + Math.random().toString(36).substring(2, 10)
}

function scrollToBottom() {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}


// --- Send message ---
async function sendMessage() {
  const text = inputMessage.value.trim()
  if (!text || isLoading.value) return

  messages.value.push({ role: 'user', content: text })
  inputMessage.value = ''
  isLoading.value = true
  scrollToBottom()

  // Create AI message as a reactive object so streaming mutations trigger re-renders
  const aiMsg = reactive({ role: 'assistant', content: '' })
  messages.value.push(aiMsg)
  streamingIndex.value = messages.value.length - 1

  const params = new URLSearchParams({
    messageId: chatId.value,
    userMessage: text,
  })

  // Use native EventSource — browser handles SSE parsing natively
  const url = `${BACKEND_BASE_URL}/api/ai/chat?${params.toString()}`
  const eventSource = new EventSource(url)

  eventSource.onmessage = (event) => {
    if (event.data) {
      aiMsg.content += event.data
      scrollToBottom()
    }
  }

  eventSource.onerror = () => {
    // EventSource fires 'error' both on real errors AND when the server
    // closes the stream (normal completion). Close it to prevent auto-reconnect.
    eventSource.close()
    streamingIndex.value = -1
    isLoading.value = false

    if (!aiMsg.content) {
      aiMsg.content = '（未收到回复，请检查后端服务是否已启动并配置了 CORS）'
    }
    scrollToBottom()
  }
}

// --- Keyboard shortcut ---
function onKeydown(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

// --- Lifecycle ---
onMounted(() => {
  chatId.value = generateId()
  messages.value.push({
    role: 'assistant',
    content: '你好！我是**交通AI小助手** 🚗\n\n我可以帮助你解答以下问题：\n- 🚌 出行路线与通勤建议\n- 🌤️ 天气对出行的影响\n- 🚦 交通状况与避堵建议\n- 🚇 公共交通查询\n\n请输入你的问题，我来为你解答！',
  })
})

const shortChatId = computed(() => {
  return chatId.value ? chatId.value.slice(0, 16) + '...' : ''
})
</script>

<template>
  <div class="app">
    <!-- Header -->
    <header class="header">
      <div class="header-content">
        <div class="header-left">
          <span class="logo">🚗</span>
          <div>
            <h1 class="header-title">交通AI小助手</h1>
            <p class="header-subtitle">出行 · 通勤 · 天气 · 智能问答</p>
          </div>
        </div>
        <div class="header-right">
          <span class="session-label">会话 ID</span>
          <span class="session-id" :title="chatId">{{ shortChatId }}</span>
        </div>
      </div>
    </header>

    <!-- Chat area -->
    <main class="chat-container" ref="messagesContainer">
      <div class="chat-inner">
        <div
          v-for="(msg, idx) in messages"
          :key="idx"
          class="message-row"
          :class="msg.role"
        >
          <!-- AI avatar -->
          <div v-if="msg.role === 'assistant'" class="avatar ai-avatar">🤖</div>

          <div class="bubble" :class="msg.role">
            <!-- AI message rendered via markdown-it plugin -->
            <MarkdownRenderer
              v-if="msg.role === 'assistant'"
              :content="msg.content"
              class="bubble-content"
            />
            <!-- Blinking cursor shown only while streaming AND content has arrived -->
            <span
              v-if="msg.role === 'assistant' && idx === streamingIndex && msg.content !== ''"
              class="typing-cursor"
            />
            <div v-else-if="msg.role !== 'assistant'" class="bubble-content">{{ msg.content }}</div>
          </div>

          <!-- User avatar -->
          <div v-if="msg.role === 'user'" class="avatar user-avatar">👤</div>
        </div>

        <!-- Typing indicator -->
        <div v-if="isLoading && messages[messages.length - 1]?.content === ''" class="message-row assistant">
          <div class="avatar ai-avatar">🤖</div>
          <div class="bubble assistant typing-bubble">
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
          </div>
        </div>
      </div>
    </main>

    <!-- Input area -->
    <footer class="input-area">
      <div class="input-inner">
        <textarea
          v-model="inputMessage"
          @keydown="onKeydown"
          placeholder="输入你的问题，例如：明天上班怎么走最快？"
          :disabled="isLoading"
          rows="1"
          class="chat-input"
        />
        <button
          @click="sendMessage"
          :disabled="isLoading || !inputMessage.trim()"
          class="send-btn"
        >
          <span v-if="isLoading" class="spinner"></span>
          <span v-else class="send-icon">➤</span>
        </button>
      </div>
      <p class="input-hint">按 <kbd>Enter</kbd> 发送，<kbd>Shift+Enter</kbd> 换行</p>
    </footer>
  </div>
</template>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f0f2f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC',
    'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
}

/* ── Header ────────────────────────────────── */
.header {
  background: linear-gradient(135deg, #1e3a5f 0%, #2d6a9f 100%);
  color: #fff;
  padding: 0 24px;
  height: 68px;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 10;
}

.header-content {
  max-width: 900px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  font-size: 32px;
  line-height: 1;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  line-height: 1.3;
}

.header-subtitle {
  font-size: 12px;
  opacity: 0.8;
  margin: 0;
  line-height: 1.3;
}

.header-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px;
}

.session-label {
  font-size: 11px;
  opacity: 0.7;
}

.session-id {
  font-size: 12px;
  font-family: monospace;
  background: rgba(255, 255, 255, 0.15);
  padding: 2px 8px;
  border-radius: 4px;
  cursor: pointer;
  user-select: all;
}

/* ── Chat area ─────────────────────────────── */
.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 24px 16px;
  scroll-behavior: smooth;
}

.chat-inner {
  max-width: 900px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* ── Message row ───────────────────────────── */
.message-row {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.message-row.user {
  flex-direction: row-reverse;
}

/* ── Avatar ─────────────────────────────────── */
.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}

.ai-avatar {
  background: #e8f4fd;
}

.user-avatar {
  background: #dbeafe;
}

/* ── Bubble ─────────────────────────────────── */
.bubble {
  max-width: 72%;
  padding: 10px 16px;
  border-radius: 18px;
  line-height: 1.6;
  word-break: break-word;
  font-size: 15px;
}

.bubble.assistant {
  background: #ffffff;
  color: #1f2937;
  border: 1px solid #e5e7eb;
  border-top-left-radius: 4px;
}

.bubble.user {
  background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
  color: #fff;
  border-top-right-radius: 4px;
}

.bubble-content {
  white-space: pre-wrap;
}

/* ── Typing indicator ──────────────────────── */
.typing-bubble {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 14px 20px !important;
}

.typing-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #94a3b8;
  animation: typingBounce 1.2s infinite ease-in-out;
}
.typing-dot:nth-child(2) { animation-delay: 0.2s; }
.typing-dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes typingBounce {
  0%, 60%, 100% { transform: translateY(0); opacity: 0.6; }
  30% { transform: translateY(-6px); opacity: 1; }
}

/* ── Input area ────────────────────────────── */
.input-area {
  background: #ffffff;
  border-top: 1px solid #e5e7eb;
  padding: 12px 16px 8px;
  flex-shrink: 0;
}

.input-inner {
  max-width: 900px;
  margin: 0 auto;
  display: flex;
  gap: 10px;
  align-items: flex-end;
}

.chat-input {
  flex: 1;
  border: 1.5px solid #d1d5db;
  border-radius: 22px;
  padding: 10px 18px;
  font-size: 15px;
  font-family: inherit;
  resize: none;
  outline: none;
  max-height: 120px;
  line-height: 1.5;
  transition: border-color 0.2s;
  background: #f9fafb;
}

.chat-input:focus {
  border-color: #2563eb;
  background: #fff;
}

.chat-input:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.send-btn {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: none;
  background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: opacity 0.2s, transform 0.1s;
}

.send-btn:hover:not(:disabled) {
  opacity: 0.9;
  transform: scale(1.05);
}

.send-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.send-icon {
  font-size: 18px;
  line-height: 1;
}

.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.4);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.input-hint {
  max-width: 900px;
  margin: 6px auto 0;
  text-align: right;
  font-size: 12px;
  color: #9ca3af;
}

.input-hint kbd {
  background: #f3f4f6;
  border: 1px solid #d1d5db;
  border-radius: 3px;
  padding: 0 4px;
  font-size: 11px;
  font-family: monospace;
}

/* ── Scrollbar ─────────────────────────────── */
.chat-container::-webkit-scrollbar {
  width: 6px;
}
.chat-container::-webkit-scrollbar-track {
  background: transparent;
}
.chat-container::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}
.chat-container::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>
