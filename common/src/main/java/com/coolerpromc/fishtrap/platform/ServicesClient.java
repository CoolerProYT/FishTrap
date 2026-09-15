package com.coolerpromc.fishtrap.platform;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.platform.services.client.IRegistryHelper;

import java.util.ServiceLoader;

public class ServicesClient {
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz, ServicesClient.class.getClassLoader()).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}