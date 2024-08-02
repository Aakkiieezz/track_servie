package servie.track_servie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import servie.track_servie.service.ListService;

@Controller
@RequestMapping("/track-servie/list")
public class ListController
{
	@Autowired
	private ListService listService;
	@Value("${user-id}")
	private Integer userId;

	@GetMapping("{tmdbId}")
	public String addToWatchlist(@RequestParam(value = "childtype", required = true) String childtype, @PathVariable Integer tmdbId)
	{
		listService.addToWatchlist(userId, tmdbId, childtype);
		return "redirect:/track-servie/servies/watchlist";
	}
}
