package com.coolerpromc.fishtrap.compat.jei;

import com.coolerpromc.fishtrap.catches.CatchTable;

/** Identity-based wrapper so hiding an old sync never hides an equal-looking new recipe. */
public final class CatchTableRecipe {
    private final CatchTable table;

    public CatchTableRecipe(CatchTable table) {
        this.table = table;
    }

    public CatchTable table() {
        return this.table;
    }
}
