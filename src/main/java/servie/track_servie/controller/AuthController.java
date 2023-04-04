package servie.track_servie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servie.track_servie.dto.LoginRequest;
import servie.track_servie.dto.Response;
import servie.track_servie.entity.AuthUser;
import servie.track_servie.utils.jwt.JwtUtils;

@RestController
@RequestMapping("")
public class AuthController
{
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("login")
    public String showLoginForm()
    {
        return "login";
    }

    @PostMapping("/login") // @GetMapping("/login")
    // public String showLoginForm()
    // {
    //     return "PageA.html";
    // }
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest)
    {
        Authentication authentication;
        try
        {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        }
        catch(RuntimeException e)
        {
            return new ResponseEntity<>(new Response("Invalid credentials."), HttpStatus.FORBIDDEN);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        AuthUser userDetails = (AuthUser) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("jwtToken", jwtToken);
        responseData.put("email", userDetails.getUsername());
        responseData.put("roles", roles);
        return new ResponseEntity<>(new Response("User Logged in Successfully", responseData), HttpStatus.OK);
    }
}
