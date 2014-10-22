package org.provider.jpfimpl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.java.plugin.ObjectFactory;
import org.java.plugin.Plugin;
import org.java.plugin.PluginManager.PluginLocation;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.standard.StandardPluginLocation;
import org.java.plugin.util.ExtendedProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.XmlWebApplicationContext;

@Service
public class PluginManager implements InitializingBean, ApplicationContextAware, ServletContextAware {

    private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);
    private static final String DEFAULT_PLUGIN = "org.provider.jpfimpl.CreateClientCommandHandler";
    private static final String DEFAULT_EXTENSION_POINT = "plugin";
    private ApplicationContext applicationContext;
    private ServletContext servletContext;

    private final Resource[] repositories;
    private final org.java.plugin.PluginManager manager;

    /**
     * @param repositories
     * @throws Exception
     */
    public PluginManager(org.springframework.core.io.Resource[] repositories) throws Exception {
        this.repositories = repositories;

        ExtendedProperties extendedProperties = new ExtendedProperties();
        extendedProperties.setProperty("org.java.plugin.ObjectFactory",
                "org.provider.jpfimpl.PluginManagerObjectFactory");

        PluginManagerObjectFactory objectFactory = (PluginManagerObjectFactory) ObjectFactory.newInstance(extendedProperties);
        objectFactory.setPluginManager(this);
        manager = objectFactory.createManager();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PluginLocation[] pluginLocations = discoverPluginLocations();
        manager.publishPlugins(pluginLocations);
        for (PluginDescriptor pd : manager.getRegistry().getPluginDescriptors()) {
            Plugin plugin = manager.getPlugin(pd.getId());
            ExtensionPoint extensionPoint = plugin.getDescriptor().getExtensionPoint(DEFAULT_EXTENSION_POINT);

            for (Extension extension : extensionPoint.getAvailableExtensions()) {
                try {
                    PluginDescriptor descriptor = extension.getDeclaringPluginDescriptor();
                    manager.activatePlugin(descriptor.getId());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        if (manager.getRegistry().getPluginDescriptors().size() > 0) manager.activatePlugin(DEFAULT_PLUGIN);
    }

    /**
     * @return
     * @throws IOException
     * @throws MalformedURLException
     */
    private PluginLocation[] discoverPluginLocations() throws IOException, MalformedURLException {
        List<PluginLocation> pluginLocations = new LinkedList<>();

        for (Resource repository : repositories) {
            if (!repository.exists()) continue;

            for (File pluginPath : repository.getFile().listFiles()) {
                if (StringUtils.containsIgnoreCase(pluginPath.getAbsolutePath(), ".svn")) {
                    continue;
                }

                unpackPluginResources(pluginPath);

                pluginLocations.add(StandardPluginLocation.create(pluginPath));
            }
        }

        return pluginLocations.toArray(new PluginLocation[pluginLocations.size()]);
    }


    /**
     * Unpack Plugin resources to respective folders
     * /templates to * webinf/classes folder
     * /resources/sql to * webinf/sql/{pluginname}/
     */
    private void unpackPluginResources(File pluginPath) {
        int lastIndexOfWebInf = pluginPath.getAbsolutePath().lastIndexOf("WEB-INF");
        String baseFolder = pluginPath.getAbsolutePath().substring(0, lastIndexOfWebInf);
        String webInfFolder = pluginPath.getAbsolutePath().substring(0, lastIndexOfWebInf) + "WEB-INF\\";
        String classesFolder = pluginPath.getAbsolutePath().substring(0, lastIndexOfWebInf) + "WEB-INF\\classes\\";

        String pluginFileName = pluginPath.getName();
        logger.info("PluginFileName:" + pluginFileName);
        int hyphenIndex = pluginFileName.indexOf("-");
        String pluginName = pluginFileName.substring(0, hyphenIndex);

        pluginName = pluginName.substring(pluginName.lastIndexOf(".") + 1);

        ZipUtils.extractZip(pluginPath.getAbsolutePath(), new String[] { "templates" },
                classesFolder, "templates/");

        ZipUtils.extractZip(pluginPath.getAbsolutePath(), new String[] { "resources/webapp" },
                baseFolder + "plugins\\" + pluginName, "resources/webapp");

        ZipUtils.extractZip(pluginPath.getAbsolutePath(), new String[] { "resources/sql" },
                classesFolder + "sql\\" + pluginName, "resources/sql");
    }

    public void registerApiPlugin(XmlWebApplicationContext context) {
        context.setParent(applicationContext);
        context.setServletContext(servletContext);
        context.refresh();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;

    }

    public ClassLoader getPluginClassLoader(PluginDescriptor pluginDescriptor) {
        return manager.getPluginClassLoader(pluginDescriptor);
    }
}
