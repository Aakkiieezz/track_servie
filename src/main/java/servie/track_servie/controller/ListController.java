package servie.track_servie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.service.ListService;

@Controller
@RequestMapping("/track-servie/list")
public class ListController
{
	@Autowired
	private ListService listService;
	@Autowired
	private UserRepository userRepository;

	@GetMapping("{tmdbId}")
	public String addToWatchlist(@RequestParam(value = "childtype", required = true) String childtype, @PathVariable Integer tmdbId, @AuthenticationPrincipal UserDetails userDetails)
	{
		Integer userId = userRepository.findByEmail(userDetails.getUsername()).get().getId();
		listService.addToWatchlist(userId, tmdbId, childtype);
		return "redirect:/track-servie/servies/watchlist";
	}
}
