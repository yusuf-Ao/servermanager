package io.aycodes.serverservice.constant;

public enum ServerStatus {
    SERVER_UP("SERVER_UP"),
    SERVER_DOWN("SERVER_DOWN"),
    NEW_SERVER("NEW_SERVER");

    private final String        status;

    ServerStatus(final String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
