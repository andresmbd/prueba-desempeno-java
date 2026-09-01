
package com.mycompany.tallerexpress.conexiondb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/tallerexpress_db";
    private static final String USER= "postgres";
    private static final String PASS = "postgres";

     public static Connection conectarDB() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASS);
    }
}
