/**
 * Measurement units:
 * 
 * length: meters
 * time: seconds
 * weight: kilograms
 */
package org.rtevo.main;

import java.io.IOException;

import org.rtevo.common.Configuration;

/**
 * @author Jan Corazza
 * 
 */
public class Main {
    // TODO two optimizations: use threads for separating simulations into units
    // and then group each simulation in a thread together under a single engine

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Configuration config = new Configuration();
        Application app = new Application(config);
        app.start();
    }

}
