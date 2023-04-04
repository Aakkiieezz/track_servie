package servie.track_servie.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import servie.track_servie.entity.AuthUser;
import servie.track_servie.entity.User;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.service.JwtUserDetailsService;

@Configuration
@RequiredArgsConstructor
public class AppConfig
{
    private final JwtUserDetailsService myUserDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Bean
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
    // no bean of userDetailsService
    //     @Bean
    //   public UserDetailsService userDetailsService()
    //   {
    // User user = userRepository.findOneByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    // AuthUser authUser = new AuthUser();
    // authUser.setUsername(user.getEmail());
    // authUser.setPassword(user.getPassword());
    // return authUser;
    //   }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()
    {
        return (web) -> web.ignoring().requestMatchers("/css/**");
    }
}