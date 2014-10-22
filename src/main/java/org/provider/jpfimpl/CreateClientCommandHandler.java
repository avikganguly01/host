package org.provider.jpfimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateClientCommandHandler implements CommandHandler {

	private final Logger logger = LoggerFactory
			.getLogger(CreateClientCommandHandler.class);

	@Override
	public void processCommand(final String command) {
		logger.info(command);
	}

}
