package com.eone.bot;

public class Helper {

    public static int getWebServerPort(String portStr) {
        int port = 8080;
        if (portStr != null) {
            try {
                port = Integer.parseInt(portStr);
            } catch (Exception e) {
                System.out.println("Could not parse port: " + port);
            }
        }
        return port;
    }

}
