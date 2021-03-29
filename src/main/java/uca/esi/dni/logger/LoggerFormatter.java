package uca.esi.dni.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggerFormatter extends Formatter {

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


    private String calcDate(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE., dd/MM/yyyy kk:mm");
        Date resultDate = new Date(millis);
        return dateFormat.format(resultDate);
    }
}
