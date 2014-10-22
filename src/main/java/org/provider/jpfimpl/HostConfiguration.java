package org.provider.jpfimpl;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@ImportResource({ "classpath*:appContext.xml" })
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "org.provider.jpfimpl" })
public class HostConfiguration {

    public HostConfiguration() {
        super();
    }

}
