import { defineConfig } from 'vitepress'

// GitHub Pages serves a project site from /<repository>/. For a custom domain or a user site, build with DOCS_BASE=/.
const base = process.env.DOCS_BASE ?? '/FishTrap/'
const TROUT_ICON = 'https://storage.googleapis.com/coolerpromc/textures/fishtrap/trout.png'

export default defineConfig({
  title: 'Fish Trap',
  description: 'Automatic underwater fishing for Minecraft 26.1+: bait, catches, new fish and datapacks.',
  base,
  cleanUrls: true,
  srcExclude: ['README.md', 'scripts/**'],
  head: [['link', { rel: 'icon', type: 'image/png', href: TROUT_ICON }]],
  themeConfig: {
    logo: { src: TROUT_ICON, alt: '' },
    nav: [
      { text: 'Guide', link: '/guide/getting-started' },
      { text: 'Catches', link: '/guide/catches' },
      { text: 'Datapacks', link: '/datapacks/' },
    ],
    sidebar: [
      {
        text: 'Guide',
        items: [
          { text: 'Getting started', link: '/guide/getting-started' },
          { text: 'The fish trap', link: '/guide/fish-trap' },
          { text: 'Bait', link: '/guide/bait' },
          { text: 'Nets', link: '/guide/nets' },
          { text: 'Catches', link: '/guide/catches' },
          { text: 'New fish', link: '/guide/fish' },
          { text: 'Advancements', link: '/guide/advancements' },
          { text: 'JEI & Jade', link: '/guide/compat' },
        ],
      },
      {
        text: 'Datapacks',
        items: [
          { text: 'Overview', link: '/datapacks/' },
          { text: 'Bait files', link: '/datapacks/bait' },
          { text: 'Net files', link: '/datapacks/nets' },
          { text: 'Catch tables', link: '/datapacks/catch-tables' },
          { text: 'Advancement trigger', link: '/datapacks/advancement-trigger' },
        ],
      },
      { text: 'FAQ', link: '/faq' },
    ],
    search: { provider: 'local' },
    outline: { level: [2, 3] },
    footer: { message: 'Released under the MIT License.' },
  },
})
