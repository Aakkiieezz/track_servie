package servie.track_servie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import servie.track_servie.payload.dtos.LoginRequest;
import servie.track_servie.utils.jwt.JwtUtils;

@Controller
@RequestMapping("auth")
public class AuthController
{
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtUtils jwtUtils;

	@GetMapping("login")
	public String showLoginPage(Model model)
	{
		model.addAttribute("loginForm", new LoginRequest());
		return "LoginPage";
	}

	@PostMapping("login")
	// @CrossOrigin(origins = "http://localhost:8080")
	public String authenticateUser(@ModelAttribute("loginForm") LoginRequest loginRequest, Model model)
	{
		Authentication authentication;
		try
		{
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		}
		catch(RuntimeException e)
		{
			// return new ResponseEntity<>(new Response("Invalid credentials."),
			// HttpStatus.FORBIDDEN);
			return "redirect:/track-servie/auth/login";
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwtToken = jwtUtils.generateJwtToken(authentication);
		// AuthUser userDetails = (AuthUser) authentication.getPrincipal();
		// List<String> roles =
		// userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
		// .collect(Collectors.toList());
		// Map<String, Object> responseData = new HashMap<>();
		// responseData.put("email", userDetails.getUsername());
		// responseData.put("roles", roles);
		// response.addHeader("Authorization", "Bearer " + jwtToken);
		// request.setAttribute("Authorization", "Bearer " + jwtToken);
		// Map<String, String> headers = new HashMap<>();
		// headers.put("Authorization", "Bearer " + jwtToken);
		// HttpServletRequestWrapper requestWrapper = new
		// CustomHttpServletRequestWrapper(request, headers);
		// redirectAttributes.addFlashAttribute("requestWrapper", requestWrapper);
		// redirectAttributes.addAttribute("type", "tv");
		// redirectAttributes.addAttribute("query", "breaking bad");
		// return "redirect:/track-servie/servies/search";
		model.addAttribute("jwtToken", jwtToken);
		return "redirect:/track-servie/servies";
	}
}
// HttpHeaders headers = new HttpHeaders();
// headers.set("Authorization", "Bearer " + jwtToken);
// CustomHttpServletResponseWrapper responseWrapper = new
// CustomHttpServletResponseWrapper(headers2, "Bearer " + jwtToken);
// redirectAttributes.addFlashAttribute("headers", responseWrapper);