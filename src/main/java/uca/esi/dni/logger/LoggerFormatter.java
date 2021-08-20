package uca.esi.dni.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The type Logger formatter.
 */
public class LoggerFormatter extends Formatter {

    /**
     * Format string.
     *
     * @param rec the rec
     * @return the string
     */
    public String format(LogRecord rec) {

        return "[" +
                calcDate(rec.getMillis()) +
                "]::[" +
                rec.getLevel() +
                "] (" +
                rec.getSourceClassName() +
                ") :: " +
                rec.getMessage() +
                "\n";
    }


    /**
     * Calc date string.
     *
     * @param millis the millis
     * @return the string
     */
    private String calcDate(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE., dd/MM/yyyy kk:mm");
        Date resultDate = new Date(millis);
        return dateFormat.format(resultDate);
    }
}
