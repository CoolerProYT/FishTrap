<script setup lang="ts">
import { netsByStrength, percent, signed } from '../fishtrap'
import ItemSlot from './ItemSlot.vue'

const nets = netsByStrength()

function speed(multiplier: number) {
  if (multiplier === 1) return 'Normal'
  return multiplier < 1 ? `${percent(1 - multiplier)} faster` : `${percent(multiplier - 1)} slower`
}
</script>

<template>
  <table>
    <thead>
      <tr>
        <th>Net</th>
        <th>Catch time</th>
        <th>Luck</th>
        <th>Bonus catch</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><span class="ft-muted">No net</span></td>
        <td>Normal</td>
        <td>0</td>
        <td>—</td>
      </tr>
      <tr v-for="net in nets" :key="net.id">
        <td><ItemSlot :id="net.item" label /></td>
        <td>{{ speed(net.catchTimeMultiplier) }} <span class="ft-muted">(×{{ net.catchTimeMultiplier }})</span></td>
        <td :class="{ good: net.luck > 0 }">{{ signed(net.luck) }}</td>
        <td>{{ net.bonusCatchChance > 0 ? percent(net.bonusCatchChance) : '—' }}</td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped>
td {
  vertical-align: middle;
}

.good {
  color: var(--vp-c-green-1);
  font-weight: 600;
}
</style>
