<script setup lang="ts">
import { computed, ref } from 'vue'
import { baitsByStrength, chances, data, itemName, netsByStrength, percent, signed } from '../fishtrap'
import ItemSlot from './ItemSlot.vue'

const props = withDefaults(defineProps<{ table?: string }>(), { table: '' })

const tables = data.tables
const baits = baitsByStrength()
const nets = netsByStrength()
const selectedTable = ref(tables.find((t) => t.id.endsWith(`/${props.table}`))?.id ?? tables[0]?.id)
const selectedBait = ref(baits[0]?.id ?? '')
const selectedNet = ref('')

const table = computed(() => tables.find((t) => t.id === selectedTable.value))
const bait = computed(() => baits.find((b) => b.id === selectedBait.value))
const net = computed(() => nets.find((n) => n.id === selectedNet.value) ?? null)
const luck = computed(() => (bait.value?.luck ?? 0) + (net.value?.luck ?? 0))
const rows = computed(() =>
  table.value
    ? chances(table.value, luck.value, net.value?.item ?? null).sort(
        (a, b) => Number(b.available) - Number(a.available) || b.chance - a.chance,
      )
    : [],
)
const maxChance = computed(() => Math.max(...rows.value.map((row) => row.chance), 0.0001))

function kind(quality: number) {
  return quality < 0 ? 'junk' : quality > 0 ? 'treasure' : 'fish'
}
</script>

<template>
  <div v-if="table" class="ft-explorer">
    <div class="tabs" role="tablist" aria-label="Catch table">
      <button
        v-for="t in tables"
        :key="t.id"
        role="tab"
        :aria-selected="t.id === selectedTable"
        :class="{ active: t.id === selectedTable }"
        @click="selectedTable = t.id"
      >
        {{ t.name }}
      </button>
    </div>

    <div class="controls">
      <label>
        Bait
        <select v-model="selectedBait">
          <option v-for="b in baits" :key="b.id" :value="b.id">{{ itemName(b.item) }} (luck {{ signed(b.luck) }})</option>
        </select>
      </label>
      <label>
        Net
        <select v-model="selectedNet">
          <option value="">No net</option>
          <option v-for="n in nets" :key="n.id" :value="n.id">{{ itemName(n.item) }} (luck {{ signed(n.luck) }})</option>
        </select>
      </label>
      <span class="total">Total luck <strong>{{ signed(luck) }}</strong></span>
    </div>

    <ul class="rows">
      <li v-for="row in rows" :key="row.item" :class="[kind(row.quality), { never: row.chance === 0 }]">
        <ItemSlot :id="row.item" />
        <div class="info">
          <div class="line">
            <span class="name">{{ itemName(row.item) }}</span>
            <span class="pct">{{ row.available ? percent(row.chance) : '—' }}</span>
          </div>
          <div class="bar" aria-hidden="true">
            <span :style="{ width: `${(row.chance / maxChance) * 100}%` }" />
          </div>
          <div v-if="!row.available" class="meta needs">Needs {{ row.requires.map(itemName).join(' or ') }}</div>
          <div v-else class="meta">weight {{ row.weight }} · quality {{ signed(row.quality) }} → {{ row.effective }}</div>
        </div>
      </li>
    </ul>
    <p class="legend">
      <span class="swatch fish" /> neutral <span class="swatch junk" /> junk (negative quality)
      <span class="swatch treasure" /> treasure (positive quality)
    </p>
  </div>
</template>

<style scoped>
.ft-explorer {
  margin: 16px 0;
  padding: 16px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  background: var(--vp-c-bg-soft);
}

.tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 12px;
}

.tabs button {
  padding: 4px 12px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 999px;
  font-size: 14px;
  color: var(--vp-c-text-2);
}

.tabs button.active {
  border-color: var(--vp-c-brand-1);
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
  font-weight: 600;
}

.controls {
  display: flex;
  flex-wrap: wrap;
  gap: 10px 18px;
  align-items: center;
  margin-bottom: 14px;
  font-size: 14px;
}

.controls label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.controls select {
  padding: 4px 8px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  background: var(--vp-c-bg);
}

.total {
  color: var(--vp-c-text-2);
}

.rows {
  list-style: none;
  padding: 0 !important;
  margin: 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 10px 20px;
}

.rows li {
  display: flex;
  gap: 10px;
  align-items: center;
  margin: 0 !important;
  --kind: var(--ft-fish);
}

.rows li.junk {
  --kind: var(--ft-junk);
}

.rows li.treasure {
  --kind: var(--ft-treasure);
}

.rows li.never {
  opacity: 0.45;
}

.info {
  flex: 1;
  min-width: 0;
}

.line {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  font-size: 14px;
}

.name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pct {
  font-family: var(--vp-font-family-mono);
  font-weight: 600;
}

.bar {
  height: 6px;
  margin: 3px 0;
  border-radius: 3px;
  background: var(--vp-c-divider);
  overflow: hidden;
}

.bar span {
  display: block;
  height: 100%;
  background: var(--kind);
  transition: width 0.25s;
}

.meta {
  font-size: 12px;
  color: var(--vp-c-text-3);
}

.meta.needs {
  color: var(--ft-treasure);
  font-weight: 600;
}

.legend {
  margin: 12px 0 0;
  font-size: 13px;
  color: var(--vp-c-text-2);
}

.swatch {
  display: inline-block;
  width: 10px;
  height: 10px;
  margin: 0 4px 0 10px;
  border-radius: 2px;
  background: var(--ft-fish);
}

.swatch.junk {
  background: var(--ft-junk);
}

.swatch.treasure {
  background: var(--ft-treasure);
}
</style>
