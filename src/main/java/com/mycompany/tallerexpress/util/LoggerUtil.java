
package com.mycompany.tallerexpress.util;


public class LoggerUtil {
    public static void logHttp(String metodo, String ruta, int statusCode, String msg){
        System.out.printf("HTTP LOG %s %s | Status: %d | Mensaje: %s%n", 
                metodo, ruta, statusCode, msg);
    }
}
