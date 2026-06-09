package com.library.util;
import java.sql.*;
public class DBUtil {
    public static Connection getDBConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","library","library123");
        return con;
    }
}