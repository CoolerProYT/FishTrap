<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { itemIcon, itemName } from '../fishtrap'

const props = withDefaults(defineProps<{ id?: string | null; count?: number; label?: boolean }>(), {
  id: null,
  count: 1,
  label: false,
})

const name = computed(() => (props.id ? itemName(props.id) : ''))
const src = computed(() => (props.id ? itemIcon(props.id) : null))

// Falls back to initials when an item has no icon or the hosted icon fails to load.
const failed = ref(false)
watch(src, () => (failed.value = false))
const initials = computed(() =>
  name.value
    .split(' ')
    .filter((word) => /^[A-Z]/.test(word))
    .slice(0, 2)
    .map((word) => word[0])
    .join(''),
)
</script>

<template>
  <span class="ft-item" :class="{ 'with-label': label }">
    <span class="ft-slot" :title="name" :aria-label="name" role="img">
      <img v-if="src && !failed" class="pixelated" :src="src" alt="" loading="lazy" @error="failed = true" />
      <span v-else-if="id" class="ft-initials">{{ initials }}</span>
      <span v-if="count > 1" class="ft-count">{{ count }}</span>
    </span>
    <span v-if="label && id" class="ft-label">{{ name }}</span>
  </span>
</template>

<style scoped>
.ft-item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  vertical-align: middle;
}

.ft-slot {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  flex: none;
  background: var(--ft-slot-bg);
  border: 2px solid;
  border-color: var(--ft-slot-dark) var(--ft-slot-light) var(--ft-slot-light) var(--ft-slot-dark);
}

.ft-slot img {
  width: 32px;
  height: 32px;
}

.ft-initials {
  font: 600 12px/1 var(--vp-font-family-mono);
  color: #fff;
  text-shadow: 1px 1px 0 #3f3f3f;
}

.ft-count {
  position: absolute;
  right: 1px;
  bottom: -1px;
  font: 700 12px/1 var(--vp-font-family-mono);
  color: #fff;
  text-shadow: 1px 1px 0 #3f3f3f;
}

.ft-label {
  font-weight: 500;
}
</style>
