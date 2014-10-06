package org.provider.springplugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/host")
public class HostController {
	
	private final CommandProcessingService commandProcessingService;

	@Autowired
	public HostController(final CommandProcessingService commandProcessingService) {
		super();
		this.commandProcessingService = commandProcessingService;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/createclient", method = RequestMethod.GET)
	public ResponseEntity getClients() {
		this.commandProcessingService.processAndLogCommand("CREATECLIENT");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateclient", method = RequestMethod.GET)
	public ResponseEntity getOffices() {
		this.commandProcessingService.processAndLogCommand("UPDATECLIENT");
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
