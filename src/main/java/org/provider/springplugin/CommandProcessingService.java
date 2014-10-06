package org.provider.springplugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CommandProcessingService {

	@SuppressWarnings("unused")
	private final ApplicationContext applicationContext;
	private final List<CommandHandler> plugins;

	@Autowired
	public CommandProcessingService(
			final ApplicationContext applicationContext,
			final List<CommandHandler> plugins) {
		this.applicationContext = applicationContext;
		this.plugins = plugins;
	}

	public void processAndLogCommand(final String command) {
		executeAllCommandHandlers(command);
	}

	private void executeAllCommandHandlers(final String command) {
		// if (command.equalsIgnoreCase("CREATECLIENT")) {
		// handler = this.applicationContext.getBean("createClientCommandHandler",
		// CommandHandler.class);
		// } else if(command.equalsIgnoreCase("UPDATECLIENT")) {
		// handler = this.applicationContext.getBean("updateClientCommandHandler",
		// CommandHandler.class);
		// }
		for (CommandHandler handler : plugins) {
			handler.processCommand(command);
		}
	}

}
