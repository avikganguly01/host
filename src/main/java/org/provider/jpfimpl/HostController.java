package org.provider.jpfimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jpf")
public class HostController {

    private final CommandProcessingService commandProcessingService;

    @Autowired
    public HostController(final CommandProcessingService commandProcessingService) {
        super();
        this.commandProcessingService = commandProcessingService;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public ResponseEntity getClients() {
        this.commandProcessingService.processAndLogCommand("CLIENT");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
