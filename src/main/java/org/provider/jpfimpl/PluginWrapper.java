package org.provider.jpfimpl;

import java.util.ArrayList;
import java.util.Collection;

import org.java.plugin.registry.PluginDescriptor;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author ashwin kumar
 * 
 */
public class PluginWrapper extends org.java.plugin.Plugin {

    private final Collection<XmlWebApplicationContext> plugins = new ArrayList<>();

    private final PluginDescriptor pluginDescriptor;
    private final PluginManager pluginManager;

    /**
     * @param pluginManager
     * @param pluginDescriptor
     * @param plugin
     */
    public PluginWrapper(PluginManager pluginManager, PluginDescriptor pluginDescriptor, Object plugin) {
        Class<?> clazz = plugin.getClass();

        this.pluginManager = pluginManager;
        this.pluginDescriptor = pluginDescriptor;

        processAnnotations(clazz);
    }

    /**
     * @param clazz
     */
    private void processAnnotations(Class<?> clazz) {
        Plugin plugin = clazz.getAnnotation(Plugin.class);

        XmlWebApplicationContext context = new XmlWebApplicationContext();

        context.setClassLoader(getPluginClassLoader());
        context.setNamespace(pluginDescriptor.getId());

        context.setConfigLocation(plugin.value());
        context.addBeanFactoryPostProcessor(new EventServiceBeanFactoryPostProcessor());

        plugins.add(context);
    }

    /**
     * @return
     */
    private ClassLoader getPluginClassLoader() {
        return pluginManager.getPluginClassLoader(pluginDescriptor);
    }

    @Override
    protected void doStart() throws Exception {
        for (XmlWebApplicationContext context : plugins) {
            pluginManager.registerApiPlugin(context);
        }
    }

    @Override
    protected void doStop() throws Exception {}
}