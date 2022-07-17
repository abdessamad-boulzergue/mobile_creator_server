package com.creator.utils;

import java.util.Properties;

public class XProperties extends Properties {

    private static final String START_CONST = "{";
    private static final String END_CONST = "}";

    // The maximum depth for recursive substitution
    // of constants within property values
    // (e.g., A={B} .. B={C} .. C={D} .. etc.)
    private static final int MAX_SUBST_DEPTH = 5;

    public XProperties() {
        super();
    }

    public XProperties(Properties defaults) {
        super(defaults);
    }
    public String getProperty(String key) {
        return getProperty(key, 0);
    }

    private String getProperty(String key, int level) {

        String value = super.getProperty(key);
        if (value != null) {

            // Get the index of the first constant, if any
            int beginIndex = 0;
            int startName = value.indexOf(START_CONST, beginIndex);

            while (startName != -1) {
                if (level + 1 > MAX_SUBST_DEPTH) {
                    // Exceeded MAX_SUBST_DEPTH
                    // Return the value as is
                    return value;
                }

                int endName = value.indexOf(END_CONST, startName);
                if (endName == -1) {
                    // Terminating symbol not found
                    // Return the value as is
                    return value;
                }

                String constName = value.substring(startName + 1, endName);
                String constValue = getProperty(constName, level + 1);

                if (constValue == null) {
                    // Property name not found
                    // Return the value as is
                    return value;
                }

                // Insert the constant value into the
                // original property value
                String newValue = (startName > 0) ? value.substring(0,
                        startName) : "";
                newValue += constValue;

                // Start checking for constants at this index
                beginIndex = newValue.length();

                // Append the remainder of the value
                newValue += value.substring(endName + 1);

                value = newValue;

                // Look for the next constant
                startName = value.indexOf(START_CONST, beginIndex);
            }
        }

        // Return the value as is
        return value;
    }

}
