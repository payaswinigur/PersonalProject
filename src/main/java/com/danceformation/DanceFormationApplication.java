package com.danceformation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the DanceFormation REST API.
 *
 * <p>
 * This class sits in com.danceformation so that component scanning reaches
 * com.danceformation.api and com.danceformation.components below it. Spring
 * cannot scan from the default package, which is why the client classes were
 * moved into a named package first.
 * </p>
 */
@SpringBootApplication
public class DanceFormationApplication {

    /**
     * Starts the embedded web server.
     *
     * @param args
     *            command line arguments, passed through to Spring
     */
    public static void main(String[] args) {
        SpringApplication.run(DanceFormationApplication.class, args);
    }
}
