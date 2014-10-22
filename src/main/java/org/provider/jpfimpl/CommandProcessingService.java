package org.provider.jpfimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CommandProcessingService {

    @SuppressWarnings("unused")
    private final ApplicationContext applicationContext;

    @Autowired
    public CommandProcessingService(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void processAndLogCommand(final String command) {

    }

}
