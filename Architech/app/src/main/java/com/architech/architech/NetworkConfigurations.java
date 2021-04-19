package com.architech.architech;

public class NetworkConfigurations {

    public static String ipAddress;
    public static String portNumber;

    public static String guestUser = "dsjgkhasdguh8h72134";//to ensure not fuss over anybody entering "guest" as username


    public static void setIpAddress(String ipAddress) {
        NetworkConfigurations.ipAddress = ipAddress;
    }

    public static String getIpAddress() {
        return ipAddress;
    }

    public static String getPortNumber() {
        return portNumber;
    }

    public static void setPortNumber(String portNumber) {
        NetworkConfigurations.portNumber = portNumber;
    }

    public static String getGuestUser() {
        return guestUser;
    }

    public static void setGuestUser(String guestUser) {
        NetworkConfigurations.guestUser = guestUser;
    }
}
