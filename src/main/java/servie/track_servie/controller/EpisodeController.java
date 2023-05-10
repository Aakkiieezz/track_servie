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
import servie.track_servie.payload.dtos.operationsEpisodePageDtos.EpisodeDtoEpisodePage;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsSearch.EpisodePageDtos.EpisodeDtoSearchEpisodePage;
import servie.track_servie.service.EpisodeService;

@Controller
@RequestMapping("/api/series/{tmdbId}/Season/{seasonNumber}/Episode/{episodeNumber}")
public class EpisodeController
{
    @Autowired
    EpisodeService episodeService;
    @Value("${user-id}")
    private Integer userId;

    // Returns EpisodePage containing selected Episode from SeasonPage
    @GetMapping("")
    public String getEpisode(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, @PathVariable Integer episodeNumber, Model model)
    {
        EpisodeDtoEpisodePage episode = episodeService.getEpisode(tmdbId, seasonNumber, episodeNumber);
        model.addAttribute("episode", episode);
        return "EpisodePage";
    }

    // Returns SearchEpisodePage containing selected Episode from SearchSeasonPage
    @GetMapping("search")
    public String searchEpisode(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, @PathVariable Integer episodeNumber, Model model)
    {
        EpisodeDtoSearchEpisodePage episodeDto = episodeService.searchEpisode(tmdbId, seasonNumber, episodeNumber);
        model.addAttribute("episode", episodeDto);
        return "SearchEpisodePage";
    }

    // Toggles the watch button of Episode located on SeasonPage
    @GetMapping("toggleback")
    public String toggleEpisodeWatch(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, @PathVariable Integer episodeNumber)
    {
        episodeService.toggleEpisodeWatch(userId, tmdbId, seasonNumber, episodeNumber);
        return "redirect:/api/series/"+tmdbId+"/Season/"+seasonNumber;
    }

    // Toggles the watch button of Episode located on EpisodePage
    @GetMapping("toggle")
    public String toggleEpWatch(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, @PathVariable Integer episodeNumber)
    {
        episodeService.toggleEpisodeWatch(userId, tmdbId, seasonNumber, episodeNumber);
        return "redirect:/api/series/"+tmdbId+"/Season/"+seasonNumber+"/Episode/"+episodeNumber;
    }

    // Returns ImageSearchEpisodePage containing list of Stills(for Episode)
    @GetMapping("stills")
    public String getEpisodeImages(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, @PathVariable Integer episodeNumber, Model model)
    {
        List<Image> images = episodeService.getEpisodeImages(tmdbId, seasonNumber, episodeNumber);
        model.addAttribute("images", images);
        model.addAttribute("tmdbId", tmdbId);
        model.addAttribute("seasonNumber", seasonNumber);
        model.addAttribute("episodeNumber", episodeNumber);
        return "ImageSearchEpisodePage";
    }

    // Redirects to EpisodePage with changed Episode Still
    @GetMapping("stillChange")
    public String changeImage(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, @PathVariable Integer episodeNumber, @RequestParam(value = "filePath", defaultValue = "") String filePath, Model model)
    {
        episodeService.changeImage(tmdbId, seasonNumber, episodeNumber, filePath);
        return "redirect:/api/series/"+tmdbId+"/Season/"+seasonNumber+"/Episode/"+episodeNumber;
    }
}