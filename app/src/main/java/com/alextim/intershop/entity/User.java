package com.alextim.intershop.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table("user_details")
@Getter
@Setter
@ToString(of = {"id", "username", "roles"})
@EqualsAndHashCode(of = "username")
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {

    @Id
    private long id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    private List<String> roles = new ArrayList<>();

    private Boolean accountNonLocked;

    private ZonedDateTime created = ZonedDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
