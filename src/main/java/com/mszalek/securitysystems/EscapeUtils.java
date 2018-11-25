package com.mszalek.securitysystems;

import java.io.StringWriter;
import java.util.HashMap;

public class EscapeUtils {

    public static final HashMap m = new HashMap();

    static {
        m.put('&', "&amp;");
        m.put('<', "&lt;");
        m.put('>', "&gt;");
        m.put('"', "&quot;");
        m.put('\'', "&#x27;");
        m.put('/', "&#x2F;");
    }

    public static String escape(String str) {
        StringWriter writer = new StringWriter();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            String entityName = (String) m.get(c);
            if (entityName == null) {
                writer.write(c);
            } else {
                writer.write(entityName);
            }
        }
        return writer.toString();
    }
}