package org.provider.jpfimpl;

public class CommandHandlerPluginContext {

    private String pluginName = "";
    private CommandHandler handler;

    /**
     * @return the platformService
     */
    public CommandHandler getPlatformService() {
        return handler;
    }

    /**
     * @param platformService
     *            the platformService to set
     */
    public void setPlatformService(CommandHandler handler) {
        this.handler = handler;
    }

    /**
     * @return the pluginName
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * @param pluginName
     *            the pluginName to set
     */
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

}
