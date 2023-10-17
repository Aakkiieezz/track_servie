package servie.track_servie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/track-servie/dashboard")
public class DashboardController
{
	@GetMapping("")
	public String trial()
	{
		return "abc";
	}
}