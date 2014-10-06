package org.provider.jspfimpl;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


//@ImportResource( { "classpath*:plugin_config.xml" } )
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "org.provider.jspfimpl" })
public class HostConfiguration {

	public HostConfiguration() {
		super();
	}

}
