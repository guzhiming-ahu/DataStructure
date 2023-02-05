package com.xtremeglory.utils;

import java.io.*;

public class BasicTestUtils {
    public static final String TEST_BASE_PATH = "./test/com/xtremeglory/";

    public static String[] loadTestCase(String path) {
        String[] lines = new String[100];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(BasicTestUtils.TEST_BASE_PATH + path)));

            int i = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                if (!line.startsWith("#")) {
                    line = line.replace(" ", "");
                    if (line.indexOf('#') != -1) {
                        lines[i++] = line.substring(0, line.indexOf('#'));
                    } else {
                        lines[i++] = line;
                    }
                }
            }
            lines[i] = "EOF";
        } catch (IOException e) {
            System.out.println("[ERROR] read file failed, no such file!");
        }
        return lines;
    }

    public static Integer[] toIntegerArray(String val) {
        String[] split = val.split(",");
        Integer[] integers = new Integer[split.length];
        for (int i = 0; i < split.length; ++i) {
            integers[i] = new Integer(split[i]);
        }
        return integers;
    }
}
