package org.provider.jpfimpl;

import org.springframework.boot.SpringApplication;

public class HostMain {

    public HostMain() {
        super();
    }

    public static void main(String[] args) {
        SpringApplication.run(HostConfiguration.class, args);
    }

}
