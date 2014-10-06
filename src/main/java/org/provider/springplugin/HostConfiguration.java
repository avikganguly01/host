package org.provider.springplugin;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


//@ImportResource( { "classpath*:plugin_config.xml" } )
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "org.provider.springplugin" })
public class HostConfiguration {

	public HostConfiguration() {
		super();
	}

}
