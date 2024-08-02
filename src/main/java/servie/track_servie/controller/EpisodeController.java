package servie.track_servie.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import servie.track_servie.payload.dtos.episodePageDtos.EpisodeDtoEpisodePage;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.service.EpisodeService;

@Controller
@RequestMapping("/track-servie/servies/{tmdbId}/Season/{seasonNo}/Episode/{episodeNo}")
public class EpisodeController
{
	@Autowired
	EpisodeService episodeService;
	@Autowired
	private UserRepository userRepository;

	// Returns EpisodePage containing selected Episode from SeasonPage
	@GetMapping("")
	public String getEpisode(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo, @PathVariable Integer episodeNo, Model model)
	{
		EpisodeDtoEpisodePage episode = episodeService.getEpisode(tmdbId, seasonNo, episodeNo);
		model.addAttribute("episode", episode);
		return "EpisodePage";
	}
	// Returns SearchEpisodePage containing selected Episode from SearchSeasonPage
	// @GetMapping("search")
	// public String searchEpisode(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo, @PathVariable Integer episodeNo, Model model)
	// {
	//     EpisodeDtoSearchEpisodePage episodeDto = episodeService.searchEpisode(tmdbId, seasonNo, episodeNo);
	//     model.addAttribute("episode", episodeDto);
	//     return "SearchEpisodePage";
	// }

	// Toggles the watch button of Episode located on SeasonPage
	@GetMapping("toggleback")
	public String toggleEpisodeWatch(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo, @PathVariable Integer episodeNo, @AuthenticationPrincipal UserDetails userDetails)
	{
		Integer userId = userRepository.findByEmail(userDetails.getUsername()).get().getId();
		episodeService.toggleEpisodeWatch(userId, tmdbId, seasonNo, episodeNo);
		return "redirect:/track-servie/servies/"+tmdbId+"/Season/"+seasonNo;
	}

	// Toggles the watch button of Episode located on EpisodePage
	@GetMapping("toggle")
	public String toggleEpWatch(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo, @PathVariable Integer episodeNo, @AuthenticationPrincipal UserDetails userDetails)
	{
		Integer userId = userRepository.findByEmail(userDetails.getUsername()).get().getId();
		episodeService.toggleEpisodeWatch(userId, tmdbId, seasonNo, episodeNo);
		return "redirect:/track-servie/servies/"+tmdbId+"/Season/"+seasonNo+"/Episode/"+episodeNo;
	}

	// Returns ImageSearchEpisodePage containing list of Stills(for Episode)
	@GetMapping("stills")
	public String getEpisodeImages(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo, @PathVariable Integer episodeNo, Model model)
	{
		List<Image> images = episodeService.getEpisodeImages(tmdbId, seasonNo, episodeNo);
		model.addAttribute("images", images);
		model.addAttribute("tmdbId", tmdbId);
		model.addAttribute("seasonNo", seasonNo);
		model.addAttribute("episodeNo", episodeNo);
		return "ImageSearchEpisodePage";
	}

	// Redirects to EpisodePage with changed Episode Still
	@GetMapping("stillChange")
	public String changeImage(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo, @PathVariable Integer episodeNo, @RequestParam(value = "filePath", defaultValue = "") String filePath, Model model)
	{
		episodeService.changeImage(tmdbId, seasonNo, episodeNo, filePath);
		return "redirect:/track-servie/servies/"+tmdbId+"/Season/"+seasonNo+"/Episode/"+episodeNo;
	}
}