package org.provider.springplugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateClientCommandHandler implements CommandHandler {

	private final Logger logger = LoggerFactory
			.getLogger(UpdateClientCommandHandler.class);

	@Transactional
	@Override
	public void processCommand(final String command) {
		logger.info(command);
	}

}
