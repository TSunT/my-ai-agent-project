<script setup>
import { computed } from 'vue'
import MarkdownIt from 'markdown-it'

const props = defineProps({
  content: {
    type: String,
    default: '',
  },
})

const md = new MarkdownIt({
  html: false,
  linkify: true,
  typographer: true,
  breaks: true,
})

/**
 * Preprocess raw markdown before parsing.
 * - Ensure ATX headings have a space after # markers
 *   (some AI models emit `###text` instead of `### text`)
 */
function preprocess(raw) {
  return raw
    // Fix headings missing space after # at start of a line
    .replace(/(^|\n)(#{1,6})([^ #\n])/g, '$1$2 $3')
}

const renderedHtml = computed(() => {
  if (!props.content) return ''
  return md.render(preprocess(props.content))
})
</script>

<template>
  <div class="markdown-body" v-html="renderedHtml" />
</template>
