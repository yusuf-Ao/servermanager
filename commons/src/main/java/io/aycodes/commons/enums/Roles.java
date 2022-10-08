package io.aycodes.commons.util;

public enum Roles {
    ADMIN("ADMIN"),
    USER("USER");

    private final String roles;

    Roles(final String roles) {
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }
}
