<script setup lang="ts">
import { computed } from 'vue'
import { data, type Advancement } from '../fishtrap'
import ItemSlot from './ItemSlot.vue'

interface Node {
  advancement: Advancement
  depth: number
}

/** Depth-first order so each advancement appears under its parent. */
const nodes = computed<Node[]>(() => {
  const children = new Map<string | null, Advancement[]>()
  const ids = new Set(data.advancements.map((a) => a.id))
  for (const advancement of data.advancements) {
    const parent = advancement.parent && ids.has(advancement.parent) ? advancement.parent : null
    children.set(parent, [...(children.get(parent) ?? []), advancement])
  }
  const result: Node[] = []
  const visit = (parent: string | null, depth: number) => {
    for (const advancement of children.get(parent) ?? []) {
      result.push({ advancement, depth })
      visit(advancement.id, depth + 1)
    }
  }
  visit(null, 0)
  return result
})
</script>

<template>
  <ul class="ft-advancements">
    <li v-for="{ advancement, depth } in nodes" :key="advancement.id" :style="{ '--depth': depth }">
      <ItemSlot :id="advancement.icon" />
      <div>
        <div class="title">
          {{ advancement.title }}
          <span v-if="advancement.frame !== 'task'" class="frame" :class="advancement.frame">{{ advancement.frame }}</span>
        </div>
        <div class="description">{{ advancement.description }}</div>
      </div>
    </li>
  </ul>
</template>

<style scoped>
.ft-advancements {
  list-style: none;
  padding: 0 !important;
}

.ft-advancements li {
  display: flex;
  gap: 12px;
  align-items: center;
  margin: 10px 0 !important;
  padding-left: calc(var(--depth) * 28px);
}

.title {
  font-weight: 600;
}

.description {
  font-size: 14px;
  color: var(--vp-c-text-2);
}

.frame {
  margin-left: 6px;
  padding: 1px 8px;
  border-radius: 999px;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
}

.frame.challenge {
  background: rgba(124, 58, 237, 0.14);
  color: var(--ft-treasure);
}
</style>
