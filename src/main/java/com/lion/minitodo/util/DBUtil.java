package com.lion.minitodo.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {
    private static String URL;
    private static String USER;
    private static String PASS;

    static {
        try {
            Properties p = new Properties();
            try (InputStream in =
                         DBUtil.class.getClassLoader().getResourceAsStream("app.properties")) {
                if (in == null) throw new IllegalStateException("app.properties not found");
                p.load(in);
            }
            URL  = p.getProperty("db.url");
            USER = p.getProperty("db.user");
            PASS = p.getProperty("db.pass");
        } catch (Exception e) {
            throw new RuntimeException("DB config load failed", e);
        }
    }

    /** 매 호출 시 새로운 커넥션을 열어 반환 */
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}