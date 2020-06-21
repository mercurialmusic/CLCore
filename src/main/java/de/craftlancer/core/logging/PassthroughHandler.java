package de.craftlancer.core.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A log handler that pushes the LogRecord to another specified Logger.
 */
public class PassthroughHandler extends Handler {
    private Logger logger;
    
    public PassthroughHandler(Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public void publish(LogRecord record) {
        logger.log(record);
    }
    
    @Override
    public void flush() {
        // nothing to do
    }
    
    @Override
    public void close() throws SecurityException {
        // nothing to do
    }
    
}
