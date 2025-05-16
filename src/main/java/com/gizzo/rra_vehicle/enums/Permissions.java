package com.gizzo.rra_vehicle.enums;

public enum Permissions {

    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    STANDARD_READ("standard:read"),
    STANDARD_CREATE("standard:create");

    private final String permission;

    // Explicit constructor
    Permissions(String permission) {
        this.permission = permission;
    }

    // Explicit getter
    public String getPermission() {
        return permission;
    }
}
