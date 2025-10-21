package com.example.db_setup.security.service;

import com.example.db_setup.model.Admin;
import com.example.db_setup.model.Player;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final Long id;
    @Getter
    private final String email;
    @Getter
    private final String password;

    public UserDetailsImpl(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static UserDetailsImpl build(Player player) {
        return new UserDetailsImpl(
                player.getID(),
                player.getEmail(),
                player.getPassword()
        );
    }

    public static UserDetailsImpl build(Admin admin) {
        return new UserDetailsImpl(
                admin.getId(),
                admin.getEmail(),
                admin.getPassword()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new LinkedHashSet<>();
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}