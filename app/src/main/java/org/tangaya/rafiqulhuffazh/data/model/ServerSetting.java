package org.tangaya.rafiqulhuffazh.data.model;

public class ServerSetting {

    private static String hostname;
    private static String port;

    private static String STATUS_ENDPOINT;
    private static String RECOGNITION_ENDPOINT;

    public static void setHostname(String hostname_) {
        hostname = hostname_;
    }

    public static void setPort(String port_) {
        port = port_;
    }

    public static String getHostname() {
        return hostname;
    }

    public static String getPort() {
        return port;
    }

    public static void applySetting() {
        setRecognitionEndpoint();
        setStatusEndpoint();
    }

    private static void setStatusEndpoint() {
        STATUS_ENDPOINT = "ws://"+hostname+":"+port+"/client/ws/status";
    }

    public static void setRecognitionEndpoint() {
        RECOGNITION_ENDPOINT = "ws://"+hostname+":"+port+"/client/ws/speech";
    }

    public static String getStatusEndpoint() {
        return STATUS_ENDPOINT;
    }

    public static String getRecognitionEndpoint() {
        return RECOGNITION_ENDPOINT;
    }
}
