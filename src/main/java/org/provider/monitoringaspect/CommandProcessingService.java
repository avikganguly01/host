package org.provider.monitoringaspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
public class CommandProcessingService {

	private final ApplicationContext applicationContext;

	@Autowired
	public CommandProcessingService(
			final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void processAndLogCommand(@SuppressWarnings("unused") final String command) {
		executeExampleWithEmbeddedSpringMonitoring();
	}
	
	

	private void executeExampleWithEmbeddedSpringMonitoring() {

		int SIMULATED_INVOCATION_COUNT = 10;
		StopWatch monitor = new StopWatch();
		CommandHandler service = (CommandHandler) applicationContext
				.getBean("createClientCommandHandler");

		monitor.start("processCommand");

		for (int i = 0; i < SIMULATED_INVOCATION_COUNT; i++) {
			service.processCommand();
		}

		monitor.stop();

	}

}
