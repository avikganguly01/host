package org.provider.jpfimpl;

import java.util.HashMap;
import java.util.Map;

public class PluginInstanceHolder {

    private static PluginInstanceHolder pluginInstanceHolder = new PluginInstanceHolder();

    private Map<String, CommandHandler> pluginHolderMap = new HashMap<>();

    private PluginInstanceHolder() {

    }

    public static PluginInstanceHolder getInstance() {
        return pluginInstanceHolder;
    }

    public void storePluginRefernce(String key, CommandHandler plugin) {
        pluginHolderMap.put(key, plugin);
    }

    public CommandHandler getPlugin(String key) {
        return pluginHolderMap.get(key);
    }

}
