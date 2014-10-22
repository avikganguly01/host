package org.provider.jpfimpl;

import org.java.plugin.PluginClassLoader;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.standard.StandardPluginLifecycleHandler;

public class PluginLifecycleHandler extends StandardPluginLifecycleHandler {

    private PluginManager pluginManager;

    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @SuppressWarnings("null")
    @Override
    protected org.java.plugin.Plugin createPluginInstance(final PluginDescriptor descr) throws PluginLifecycleException {
        PluginClassLoader loader = getPluginManager().getPluginClassLoader(descr);

        Class<?> pluginClass = null;
        try {
            pluginClass = loader.loadClass(descr.getPluginClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            return new PluginWrapper(pluginManager, descr, pluginClass.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;

    }
}