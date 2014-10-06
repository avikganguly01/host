package org.provider.monitoringaspect;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
@ImportResource({ "classpath*:monitorAppContext.xml" })
@ComponentScan(basePackages = { "org.provider.monitoringaspect" })
public class MonitorConfiguration {

	public MonitorConfiguration() {
		super();
	}

}
