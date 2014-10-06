package org.provider.monitoringaspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MainController {
	
	private final CommandProcessingService commandProcessingService;

	@Autowired
	public MainController(final CommandProcessingService commandProcessingService) {
		super();
		this.commandProcessingService = commandProcessingService;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity getClients() {
		this.commandProcessingService.processAndLogCommand("CLIENT");
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
