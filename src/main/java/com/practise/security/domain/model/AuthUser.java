package com.practise.security.domain.model;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
public class AuthUser implements UserDetails, CredentialsContainer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private String password;

  private boolean enabled = true;

  //  @ElementCollection(fetch = FetchType.EAGER)
  //  private List<String> roles = List.of("USER");

  public AuthUser(String email) {
    this.email = email;
  }

  public AuthUser() {}

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    //    return roles.stream().map(role -> (GrantedAuthority) () -> "ROLE_" + role).toList();
    return List.of(() -> "ROLE_ADMIN");
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
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
    return enabled;
  }

  @Override
  public void eraseCredentials() {
    this.password = null;
  }
}
