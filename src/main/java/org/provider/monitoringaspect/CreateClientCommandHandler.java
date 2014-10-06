package org.provider.monitoringaspect;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.springframework.stereotype.Service;

@Service
public class CreateClientCommandHandler implements CommandHandler{

	protected static final long DEFAULT_SLEEP_INTERVAL = 10;
	private long sleepInterval;

	public CreateClientCommandHandler()
	{
		setSleepInterval(DEFAULT_SLEEP_INTERVAL);
	}	
	
	@Override
	public void processCommand()
	{
		Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.TRACE);
		try{
			Thread.sleep(getSleepInterval());	
		}catch (InterruptedException int_exce){
			throw new RuntimeException("Service interrpted. Exiting.", 
										int_exce);
		}
	}
	
	public long getSleepInterval()
	{return sleepInterval;}

	public void setSleepInterval(long sleepInterval)
	{this.sleepInterval = sleepInterval;}
}
