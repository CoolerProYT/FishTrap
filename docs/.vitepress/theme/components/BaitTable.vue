<script setup lang="ts">
import { baitsByStrength, seconds, signed } from '../fishtrap'
import ItemSlot from './ItemSlot.vue'

const baits = baitsByStrength()
</script>

<template>
  <table class="ft-bait-table">
    <thead>
      <tr>
        <th>Bait</th>
        <th>Catch time</th>
        <th>Average</th>
        <th>Luck</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="bait in baits" :key="bait.id">
        <td><ItemSlot :id="bait.item" label /></td>
        <td>{{ seconds(bait.minTicks) }}–{{ seconds(bait.maxTicks) }} s</td>
        <td>{{ seconds((bait.minTicks + bait.maxTicks) / 2) }} s</td>
        <td :class="bait.luck > 0 ? 'good' : bait.luck < 0 ? 'bad' : ''">{{ signed(bait.luck) }}</td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped>
.ft-bait-table td {
  vertical-align: middle;
}

.good {
  color: var(--vp-c-green-1);
  font-weight: 600;
}

.bad {
  color: var(--vp-c-red-1);
  font-weight: 600;
}
</style>
