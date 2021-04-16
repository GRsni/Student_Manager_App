package uca.esi.dni.logger;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {

    private AppLogger() {
    }

    private static FileHandler logFile;

    public static void setup() {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        try {
            logFile = new FileHandler("Log.txt", true);

        } catch (IOException e) {
            //If the logger fails, reload the app
        }
        LoggerFormatter formatter = new LoggerFormatter();
        logFile.setFormatter(formatter);
        logger.addHandler(logFile);
    }
}
