package servie.track_servie.entities;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUser implements UserDetails
{
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean accountNonExpired = true;
    private Boolean accountNonLocked = true;
    private Boolean credentialsNonExpired = true;
    private Boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return this.authorities;
    }

    @Override
    public String getPassword()
    {
        return this.password;
    }

    @Override
    public String getUsername()
    {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return this.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return this.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return this.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled()
    {
        return this.getEnabled();
    }

    public AuthUser()
    {}

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities)
    {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
}
