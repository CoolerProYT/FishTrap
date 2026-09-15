import DefaultTheme from 'vitepress/theme'
import type { Theme } from 'vitepress'
import AdvancementTree from './components/AdvancementTree.vue'
import BaitTable from './components/BaitTable.vue'
import CatchExplorer from './components/CatchExplorer.vue'
import ItemSlot from './components/ItemSlot.vue'
import NetTable from './components/NetTable.vue'
import RecipeCard from './components/RecipeCard.vue'
import './style.css'

export default {
  extends: DefaultTheme,
  enhanceApp({ app }) {
    app.component('AdvancementTree', AdvancementTree)
    app.component('BaitTable', BaitTable)
    app.component('CatchExplorer', CatchExplorer)
    app.component('ItemSlot', ItemSlot)
    app.component('NetTable', NetTable)
    app.component('RecipeCard', RecipeCard)
  },
} satisfies Theme
