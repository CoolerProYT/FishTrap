// @ts-ignore
import raw from '../data/data.json'

export interface Bait {
  id: string
  item: string
  minTicks: number
  maxTicks: number
  luck: number
}

export interface Net {
  id: string
  item: string
  style: string
  catchTimeMultiplier: number
  luck: number
  bonusCatchChance: number
}

export interface CatchEntry {
  item: string
  weight: number
  quality: number
  /** Nets (tool items) the entry needs in the trap; empty when it has no such condition. */
  requires: string[]
}

export interface CatchTable {
  id: string
  name: string
  entries: CatchEntry[]
}

export interface Recipe {
  id: string
  type: string
  result: { id: string; count: number }
  pattern?: string[]
  key?: Record<string, string>
  ingredients?: string[]
  ingredient?: string
  cookingTime?: number
  experience?: number
  template?: string
  base?: string
  addition?: string
}

export interface Advancement {
  id: string
  parent: string | null
  icon: string | null
  title: string
  description: string
  frame: 'task' | 'goal' | 'challenge'
}

export const data = raw as unknown as {
  names: Record<string, string>
  textures: Record<string, string>
  baits: Bait[]
  nets: Net[]
  tables: CatchTable[]
  recipes: Recipe[]
  advancements: Advancement[]
  biomeTags: Record<string, string[]>
}

const TAG_NAMES: Record<string, string> = {
  '#minecraft:fishes': 'Any fish',
}

/** Item shown for a tag ingredient. */
const TAG_ICONS: Record<string, string> = {
  '#minecraft:fishes': 'minecraft:cod',
}

/** Mod items use their in-game name; vanilla ids are turned into readable names. */
export function itemName(id: string): string {
  if (data.names[id]) return data.names[id]
  if (TAG_NAMES[id]) return TAG_NAMES[id]
  const path = id.replace(/^#/, '').split(':').pop() ?? id
  return path
    .split('_') // @ts-ignore
    .map((word) => (['of', 'the'].includes(word) ? word : word.charAt(0).toUpperCase() + word.slice(1)))
    .join(' ')
}

/** Hosted renders of vanilla items, one PNG per item id. Mojang's textures are not bundled here. */
const VANILLA_ICONS = 'https://storage.googleapis.com/coolerpromc/textures'

/**
 * Where to load an item's icon from. Mod items use the hosted textures listed by the sync script;
 * vanilla items use the hosted renders.
 */
export function itemIcon(id: string): string | null {
  const itemId = TAG_ICONS[id] ?? id
  if (data.textures[itemId]) return data.textures[itemId]
  // @ts-ignore
  const [namespace, path] = itemId.includes(':') ? itemId.split(':') : ['minecraft', itemId]
  if (namespace !== 'minecraft') return null
  return `${VANILLA_ICONS}/${namespace}/${path}.png`
}

/** Vanilla loot pools: max(floor(weight + quality * luck), 0). */
export function effectiveWeight(entry: CatchEntry, luck: number): number {
  return Math.max(Math.floor(entry.weight + entry.quality * luck), 0)
}

export function availableWith(entry: CatchEntry, netItem: string | null): boolean {
  // @ts-ignore
  return entry.requires.length === 0 || (netItem !== null && entry.requires.includes(netItem))
}

/** Chances with the given total luck (bait + net) and net in the trap. Entries the net does not unlock cannot roll. */
export function chances(table: CatchTable, luck: number, netItem: string | null) {
  const total = table.entries
    .filter((entry) => availableWith(entry, netItem))
    .reduce((sum, entry) => sum + effectiveWeight(entry, luck), 0)
  return table.entries.map((entry) => {
    const available = availableWith(entry, netItem)
    const effective = available ? effectiveWeight(entry, luck) : 0
    return { ...entry, available, effective, chance: total === 0 ? 0 : effective / total }
  })
}

export function seconds(ticks: number): string {
  const value = ticks / 20
  // @ts-ignore
  return Number.isInteger(value) ? `${value}` : value.toFixed(1)
}

export function signed(value: number): string {
  return value > 0 ? `+${value}` : `${value}`
}

export function percent(fraction: number): string {
  const value = Math.round(fraction * 1000) / 10
  // @ts-ignore
  return `${Number.isInteger(value) ? value : value.toFixed(1)}%`
}

export function baitsByStrength(): Bait[] {
  return [...data.baits].sort((a, b) => a.luck - b.luck || b.minTicks - a.minTicks)
}

export function netsByStrength(): Net[] {
  return [...data.nets].sort((a, b) => a.luck - b.luck || b.catchTimeMultiplier - a.catchTimeMultiplier)
}
