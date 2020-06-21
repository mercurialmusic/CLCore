package de.craftlancer.core.logging;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Simple formatter for log records, resulting in
 * LEVEL yyyy-MM-dd HH:mm:ss Message
 */
public class SimpleLogFormatter extends Formatter {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        
        builder.append(record.getLevel());
        builder.append(" ");
        builder.append(DATE_FORMAT.format(record.getMillis()));
        builder.append(" ");
        builder.append(formatMessage(record));
        builder.append("\n");
        
        return builder.toString();
    }
}
