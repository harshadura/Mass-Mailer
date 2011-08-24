package com.universl.massmailer.db;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    private static Logger logger = Logger.getLogger(DBConnect.class);
    private static Connection con = null;
    private static String dbName = PropertyLoader.dbName;
    private static String dbUser = PropertyLoader.dbUser;
    private static String dbPass = PropertyLoader.dbPass;

    public Connection connect() {
        if (con == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + dbName + "?autoReconnect=true", dbUser, dbPass);

                if (!con.isClosed()) {
                  logger.info("Mysql Connected Successfully..");
                }
            } catch (Exception e) {
                  logger.error("Mysql not Connected.. : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return con;
    }
}
