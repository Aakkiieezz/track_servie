package servie.track_servie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import servie.track_servie.entity.User;
import servie.track_servie.service.UserService;

@Controller
@RequestMapping("/track-servie/auth")
public class AuthController
{
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String getLoginForm()
	{
		return "LoginPage";
	}
	// @PostMapping("/login")
	// public String getLoginForm(@ModelAttribute User user)
	// {
	// 	return "LoginPage";
	// }

	@GetMapping("/register")
	public String register(Model model)
	{
		model.addAttribute("user", new User());
		return "RegistrationPage";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute User user)
	{
		userService.register(user);
		return "redirect:/track-servie/auth/login?success";
	}
}