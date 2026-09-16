# Fish Trap wiki

VitePress site for the mod. Bait stats, catch tables, recipes, advancements and item textures are read from the mod itself, so regenerate the mod's data before building when it changes.

```bash
./gradlew :neoforge:runData   # from the repository root, when mod data changed
cd docs
npm install
npm run dev                   # syncs data, then serves http://localhost:5173
npm run build                 # syncs data, then builds to .vitepress/dist
```

`npm run sync` (run automatically by `dev` and `build`) writes `.vitepress/data/data.json` and copies the mod's item textures to `public/items/`. Both are git-ignored. Vanilla textures are not copied: vanilla items load from the hosted renders at `https://storage.googleapis.com/coolerpromc/textures/`, set in `.vitepress/theme/fishtrap.ts`. Mod block items have no flat texture of their own and use the PNGs committed in `public/icons/`.
