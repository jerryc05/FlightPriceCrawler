package com.jerryc05;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class MyUtils {

    private MyUtils() {
    }

    public static void handleException(Exception e, Logger logger) {

        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        logger.warning(stringWriter::toString);
    }
}