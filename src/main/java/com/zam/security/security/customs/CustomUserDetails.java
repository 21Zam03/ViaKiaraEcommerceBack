package com.zam.security.security.customs;

import com.zam.security.entities.RoleEntity;
import com.zam.security.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public record CustomUserDetails(UserEntity user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        Set<RoleEntity> roles = user.getRoleList();
        roles.forEach(role -> authorityList
                .add(new SimpleGrantedAuthority("ROLE_".concat(role.getName()))));
        roles.stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));
        return authorityList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNoExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNoLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialNoExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

}
