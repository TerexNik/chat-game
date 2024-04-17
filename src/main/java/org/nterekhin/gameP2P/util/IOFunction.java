package org.nterekhin.gameP2P.util;

import java.io.IOException;

/**
 * Wrapper interface for situations when we do not need to process exception
 */
@FunctionalInterface
public interface IOFunction {
    void execute() throws IOException;

    static void executeWithLogOnIOException(IOFunction function) {
        try {
            function.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}