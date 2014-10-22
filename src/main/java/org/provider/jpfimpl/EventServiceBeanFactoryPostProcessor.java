package org.provider.jpfimpl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author ashwin kumar
 *
 */
public class EventServiceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    String extensionBeanName;
    String propertyName;
    String pluginBeanName;

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EventServiceBeanFactoryPostProcessor.class);

    /**
     * The bean that is being extended (the bean with a {@link List} based
     * property.
     * 
     * @param beanName
     *            Spring bean name.
     */
    public void setExtensionBeanName(String beanName) {
        this.extensionBeanName = beanName;
    }

    /**
     * The name of the {@link List} property within the
     * {@link #setExtensionBeanName(String) extension} bean.
     * 
     * @param propertyName
     *            property name.
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * The name of the bean to plug-in to the extension bean's list property.
     * 
     * @param pluginName
     *            The plugin bean's name.
     */
    public void setPluginBeanName(String pluginName) {
        pluginBeanName = pluginName;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("In Post Processor");
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("Child Factory has Bean with name:" + beanName);

            if (StringUtils.endsWithIgnoreCase(beanName, "Plugin")) {
                PluginInstanceHolder.getInstance().storePluginRefernce(beanName, beanFactory.getBean(beanName, CommandHandler.class));
            }
        }

        // beanFactory.getParentBeanFactory().containsBean("platformService")
        // beanFactory.containsBeanDefinition("platformService")
        /*
         * logger.info("Plugging in " + pluginBeanName + " into bean:" +
         * extensionBeanName + " property:" + propertyName); if (
         * extensionBeanName == null ||
         * !beanFactory.containsBeanDefinition(extensionBeanName) ) throw new
         * IllegalArgumentException("Cannot find bean " + extensionBeanName);
         * 
         * BeanDefinition beanDef =
         * beanFactory.getBeanDefinition(extensionBeanName);
         * MutablePropertyValues propValues = beanDef.getPropertyValues(); if (
         * propertyName == null || !propValues.contains(propertyName)) throw new
         * IllegalArgumentException("Cannot find property " + propertyName +
         * " in bean " + extensionBeanName); PropertyValue pv =
         * propValues.getPropertyValue(propertyName); Object prop =
         * pv.getValue(); if ( !(prop instanceof List)) throw new
         * IllegalArgumentException("Property " + propertyName +
         * " in extension bean " + extensionBeanName +
         * " is not an instanceof List.");
         * 
         * List l = (List) pv.getValue();
         * 
         * l.add(new RuntimeBeanReference(pluginBeanName));
         */
    }
}
