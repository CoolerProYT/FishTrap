package com.coolerpromc.fishtrap.compat.jei;

import com.coolerpromc.fishtrap.bait.BaitType;

/** Identity-based wrapper so hiding an old sync never hides an equal-looking new recipe. */
public final class BaitRecipe {
    private final BaitType bait;

    public BaitRecipe(BaitType bait) {
        this.bait = bait;
    }

    public BaitType bait() {
        return this.bait;
    }
}
