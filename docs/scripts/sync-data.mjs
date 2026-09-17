// Pulls wiki data straight from the mod so the docs never drift from the game:
// datagen output (bait, nets, catch tables, recipes, advancements, lang, biome tags) and the mod's own textures.
// Run `./gradlew :neoforge:runData` first when the mod's data changes.
import { copyFileSync, existsSync, mkdirSync, readdirSync, readFileSync, writeFileSync } from 'node:fs'
import { basename, dirname, join } from 'node:path'
import { fileURLToPath } from 'node:url'

const docs = join(dirname(fileURLToPath(import.meta.url)), '..')
const root = join(docs, '..')
const generated = join(root, 'common/src/generated/resources')
const assets = join(root, 'common/src/main/resources/assets/fishtrap')
const data = join(generated, 'data/fishtrap')

if (!existsSync(generated)) {
  console.error(`No datagen output at ${generated}. Run ./gradlew :neoforge:runData first.`)
  process.exit(1)
}

const readJson = (file) => JSON.parse(readFileSync(file, 'utf8'))
const jsonFiles = (dir) => (existsSync(dir) ? readdirSync(dir).filter((f) => f.endsWith('.json')).sort() : [])
const text = (component) => (typeof component === 'string' ? component : component?.translate ?? component?.text ?? '')
const list = (value) => (value == null ? [] : Array.isArray(value) ? value : [value])

const lang = readJson(join(generated, 'assets/fishtrap/lang/en_us.json'))

// Item names for mod items; vanilla names are prettified on the page.
const names = {}
for (const [key, value] of Object.entries(lang)) {
  const match = key.match(/^(item|block)\.fishtrap\.([a-z0-9_]+)$/)
  if (match) names[`fishtrap:${match[2]}`] = value
}

// Mod textures are hosted at 1024x1024 alongside the vanilla renders; upload new ones there before syncing.
// Block items have no flat texture, so their icons are rendered from the block model (sources kept in public/icons/).
const HOSTED_TEXTURES = 'https://storage.googleapis.com/coolerpromc/textures/fishtrap'
const pngNames = (dir) => (existsSync(dir) ? readdirSync(dir).filter((f) => f.endsWith('.png')).map((f) => basename(f, '.png')) : [])
const textures = {}
for (const name of [...pngNames(join(assets, 'textures/item')), ...pngNames(join(docs, 'public/icons'))]) {
  textures[`fishtrap:${name}`] = `${HOSTED_TEXTURES}/${name}.png`
}
mkdirSync(join(docs, 'public/gui'), { recursive: true })
copyFileSync(join(assets, 'textures/gui/container/fish_trap.png'), join(docs, 'public/gui/fish_trap.png'))

const baits = jsonFiles(join(data, 'bait')).map((file) => {
  const json = readJson(join(data, 'bait', file))
  return { id: `fishtrap:${basename(file, '.json')}`, item: json.item, minTicks: json.minTicks, maxTicks: json.maxTicks, luck: json.luck ?? 0 }
})

const nets = jsonFiles(join(data, 'net')).map((file) => {
  const json = readJson(join(data, 'net', file))
  return {
    id: `fishtrap:${basename(file, '.json')}`,
    item: json.item,
    style: json.style,
    catchTimeMultiplier: json.catchTimeMultiplier ?? 1,
    luck: json.luck ?? 0,
    bonusCatchChance: json.bonusCatchChance ?? 0,
  }
})

// Items named by minecraft:match_tool conditions: the net a catch needs.
const requiredTools = (entry) =>
  (entry.conditions ?? [])
    .filter((condition) => condition.condition === 'minecraft:match_tool')
    .flatMap((condition) => list(condition.predicate?.items))
    .filter((id) => typeof id === 'string' && !id.startsWith('#'))

const tables = jsonFiles(join(data, 'loot_table/fish_trap')).map((file) => {
  const name = basename(file, '.json')
  const json = readJson(join(data, 'loot_table/fish_trap', file))
  const entries = (json.pools ?? []).flatMap((pool) =>
    (pool.entries ?? [])
      .filter((entry) => entry.type === 'minecraft:item')
      .map((entry) => ({ item: entry.name, weight: entry.weight ?? 1, quality: entry.quality ?? 0, requires: requiredTools(entry) })),
  )
  return { id: `fishtrap:fish_trap/${name}`, name: lang[`catch_table.fishtrap.${name}`] ?? name, entries }
})

const ingredient = (value) => {
  if (typeof value === 'string') return value
  if (Array.isArray(value)) return ingredient(value[0])
  if (value?.item) return value.item
  if (value?.tag) return `#${value.tag}`
  return '?'
}

const recipes = jsonFiles(join(data, 'recipe')).map((file) => {
  const json = readJson(join(data, 'recipe', file))
  const recipe = { id: `fishtrap:${basename(file, '.json')}`, type: json.type, result: { id: json.result?.id, count: json.result?.count ?? 1 } }
  if (json.type === 'minecraft:crafting_shaped') {
    recipe.pattern = json.pattern
    recipe.key = Object.fromEntries(Object.entries(json.key).map(([symbol, value]) => [symbol, ingredient(value)]))
  } else if (json.type === 'minecraft:crafting_shapeless') {
    recipe.ingredients = json.ingredients.map(ingredient)
  } else if (json.type === 'minecraft:smithing_transform') {
    recipe.template = ingredient(json.template)
    recipe.base = ingredient(json.base)
    recipe.addition = ingredient(json.addition)
  } else {
    recipe.ingredient = ingredient(json.ingredient)
    recipe.cookingTime = json.cookingtime
    recipe.experience = json.experience
  }
  return recipe
})

const advancements = jsonFiles(join(data, 'advancement/fish_trap')).map((file) => {
  const json = readJson(join(data, 'advancement/fish_trap', file))
  const display = json.display ?? {}
  return {
    id: `fishtrap:fish_trap/${basename(file, '.json')}`,
    parent: json.parent ?? null,
    icon: display.icon?.id ?? display.icon?.item ?? null,
    title: lang[text(display.title)] ?? text(display.title),
    description: lang[text(display.description)] ?? text(display.description),
    frame: display.frame ?? 'task',
  }
})

const biomeTags = Object.fromEntries(
  jsonFiles(join(data, 'tags/worldgen/biome/fish_trap')).map((file) => [
    basename(file, '.json'),
    readJson(join(data, 'tags/worldgen/biome/fish_trap', file)).values,
  ]),
)

mkdirSync(join(docs, '.vitepress/data'), { recursive: true })
writeFileSync(
  join(docs, '.vitepress/data/data.json'),
  JSON.stringify({ names, textures, baits, nets, tables, recipes, advancements, biomeTags }, null, 2),
)
console.log(
  `Synced ${baits.length} baits, ${nets.length} nets, ${tables.length} catch tables, ${recipes.length} recipes, ${advancements.length} advancements, ${Object.keys(textures).length} textures.`,
)
