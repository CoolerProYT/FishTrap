package com.coolerpromc.fishtrap;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String MODID = "fishtrap";
	public static final String MOD_NAME = "Fish Trap";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier id(String path){
		return Identifier.fromNamespaceAndPath(MODID, path);
	}
}