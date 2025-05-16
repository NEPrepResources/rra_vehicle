package com.gizzo.rra_vehicle.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum Roles {
    ADMIN(
            Set.of(
                    Permissions.ADMIN_CREATE,
                    Permissions.ADMIN_READ
            )
    ),
    STANDARD(
            Set.of(
                    Permissions.STANDARD_CREATE,
                    Permissions.STANDARD_READ
            )
    );

    private final Set<Permissions> permissions;

    Roles(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = permissions
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}