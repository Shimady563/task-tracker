package com.shimady.tracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tracker_user")
public class User implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "tracker_user_id_seq",
            sequenceName = "tracker_user_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(generator = "tracker_user_id_seq")
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
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
