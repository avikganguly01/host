package org.provider.jpfimpl;

import org.java.plugin.standard.StandardObjectFactory;

public class PluginManagerObjectFactory extends StandardObjectFactory {

    private PluginManager pluginManager;

    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    protected PluginLifecycleHandler createLifecycleHandler() {
        PluginLifecycleHandler result = new PluginLifecycleHandler();
        result.configure(config.getSubset(PluginLifecycleHandler.class.getName() + "."));
        result.setPluginManager(pluginManager);
        return result;
    }
}