package servie.track_servie.service;

import jakarta.transaction.Transactional;
import servie.track_servie.entities.AuthUser;
import servie.track_servie.entities.User;
import servie.track_servie.repository.PermissionRepository;
import servie.track_servie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService
{
    @Autowired
    UserRepository userRepository;
    @Autowired
    PermissionRepository permissionRepository;

    @Override
    @Transactional
    public AuthUser loadUserByUsername(String email) throws UsernameNotFoundException
    {
        User user = userRepository.findOneByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found for the email: "+email));
        List<GrantedAuthority> authorities = user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).map(permission -> new SimpleGrantedAuthority(permission.getName())).collect(Collectors.toList());
        return new AuthUser(user.getEmail(), user.getPassword(), authorities);
    }
}