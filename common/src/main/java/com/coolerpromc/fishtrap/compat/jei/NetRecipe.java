package com.coolerpromc.fishtrap.compat.jei;

import com.coolerpromc.fishtrap.upgrade.NetType;

/** Identity-based wrapper so hiding an old sync never hides an equal-looking new recipe. */
public final class NetRecipe {
    private final NetType net;

    public NetRecipe(NetType net) {
        this.net = net;
    }

    public NetType net() {
        return this.net;
    }
}
