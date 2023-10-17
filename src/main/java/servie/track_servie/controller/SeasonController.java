package servie.track_servie.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsSeasonPageDtos.SeasonDtoSeasonPage;
import servie.track_servie.service.EpisodeService;
import servie.track_servie.service.SeasonService;

@Controller
@RequestMapping("/track-servie/servies/{tmdbId}/Season/{seasonNo}")
public class SeasonController
{
	@Autowired
	SeasonService seasonService;
	@Autowired
	EpisodeService episodeService;
	@Value("${user-id}")
	private Integer userId;

	// Returns SeasonPage containing selected Season from SeriesPage
	@GetMapping("")
	public String getSeason(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo, Model model)
	{
		SeasonDtoSeasonPage season = seasonService.getSeason(tmdbId, seasonNo);
		model.addAttribute("season", season);
		return "SeasonPage";
	}

	// Toggles the watch button of Season located on SeriesPage
	@GetMapping("toggleback")
	public String toggleSeasonWatch(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo)
	{
		seasonService.toggleSeasonWatch(userId, tmdbId, seasonNo);
		return "redirect:/track-servie/servies/"+tmdbId+"?type=tv";
	}

	// Toggles the watch button of Season located on SeasonPage
	@GetMapping("toggle")
	public String toggleSeWatch(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo)
	{
		seasonService.toggleSeasonWatch(userId, tmdbId, seasonNo);
		return "redirect:/track-servie/servies/"+tmdbId+"/Season/"+seasonNo;
	}

	// Toggles the watch button of multiple Episodes located on SeasonPage
	@GetMapping("toggleepisodes")
	public String toggleMultipleEpisodeWatch(@PathVariable Integer tmdbId,
			@PathVariable Integer seasonNo,
			@RequestParam(value = "watch", required = true) String watch,
			@RequestParam(value = "fromEpisodeNumber", required = true) String fromEpisodeNumber,
			@RequestParam(value = "toEpisodeNumber", required = true) String toEpisodeNumber)
	{
		Integer episodeNoInt = Integer.parseInt(fromEpisodeNumber);
		Integer toEpisodeNumberInt = Integer.parseInt(toEpisodeNumber);
		boolean watchValue = Boolean.parseBoolean(watch);
		episodeService.toggleMultipleEpisodeWatch(userId, tmdbId, seasonNo, episodeNoInt, toEpisodeNumberInt, watchValue);
		return "redirect:/track-servie/servies/"+tmdbId+"/Season/"+seasonNo;
	}

	// Returns ImageSearchSeasonPage containing list of Posters(for specific Season)
	@GetMapping("posters")
	public String getSeasonImages(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo, Model model)
	{
		List<Image> images = seasonService.getSeasonImages(tmdbId, seasonNo);
		model.addAttribute("images", images);
		model.addAttribute("tmdbId", tmdbId);
		model.addAttribute("seasonNo", seasonNo);
		return "ImageSearchSeasonPage";
	}

	// Redirects to SeasonPage with changed Season Poster
	@GetMapping("posterChange")
	public String changeImage(@PathVariable Integer tmdbId, @PathVariable Integer seasonNo, @RequestParam(value = "filePath", defaultValue = "") String filePath, Model model)
	{
		seasonService.changeImage(tmdbId, seasonNo, filePath);
		// return "redirect:/track-servie/servies/"+tmdbId;
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("redirect:/track-servie/servies/{tmdbId}")
				.queryParam("type", "tv");
		return builder.buildAndExpand(tmdbId).toUriString();
	}
}
