package org.provider.monitoringaspect;

import org.springframework.stereotype.Service;

@Service
public interface CommandHandler {
	
	void processCommand();

}
