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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsSearch.SeasonPageDtos.SeasonDtoSearchSeasonPage;
import servie.track_servie.payload.dtos.operationsSeasonPageDtos.SeasonDtoSeasonPage;
import servie.track_servie.service.SeasonService;

@Controller
@RequestMapping("/track-servie/servies/{tmdbId}/Season/{seasonNumber}")
public class SeasonController
{
    @Autowired
    SeasonService seasonService;
    @Value("${user-id}")
    private Integer userId;

    // Returns SeasonPage containing selected Season from SeriesPage
    @GetMapping("")
    public String getSeason(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, Model model)
    {
        SeasonDtoSeasonPage season = seasonService.getSeason(tmdbId, seasonNumber);
        model.addAttribute("season", season);
        return "SeasonPage";
    }
    // // Returns SearchSeasonPage containing selected Season from SearchSeriesPage
    // @GetMapping("search")
    // public String searchSeason(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, Model model)
    // {
    //     SeasonDtoSearchSeasonPage seasonDto = seasonService.searchSeason(tmdbId, seasonNumber);
    //     model.addAttribute("season", seasonDto);
    //     return "SearchSeasonPage";
    // }

    // Toggles the watch button of Season located on SeriesPage
    @GetMapping("toggleback")
    public String toggleSeasonWatch(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber)
    {
        seasonService.toggleSeasonWatch(userId, tmdbId, seasonNumber);
        return "redirect:/track-servie/servies/"+tmdbId+"?type=tv";
    }

    // Toggles the watch button of Season located on SeasonPage
    @GetMapping("toggle")
    public String toggleSeWatch(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber)
    {
        seasonService.toggleSeasonWatch(userId, tmdbId, seasonNumber);
        return "redirect:/track-servie/servies/"+tmdbId+"/Season/"+seasonNumber;
    }

    // Returns ImageSearchSeasonPage containing list of Posters(for specific Season)
    @GetMapping("posters")
    public String getSeasonImages(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, Model model)
    {
        List<Image> images = seasonService.getSeasonImages(tmdbId, seasonNumber);
        model.addAttribute("images", images);
        model.addAttribute("tmdbId", tmdbId);
        model.addAttribute("seasonNumber", seasonNumber);
        return "ImageSearchSeasonPage";
    }

    // Redirects to SeasonPage with changed Season Poster
    @GetMapping("posterChange")
    public String changeImage(@PathVariable Integer tmdbId, @PathVariable Integer seasonNumber, @RequestParam(value = "filePath", defaultValue = "") String filePath, Model model)
    {
        seasonService.changeImage(tmdbId, seasonNumber, filePath);
        // return "redirect:/track-servie/servies/"+tmdbId;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("redirect:/track-servie/servies/{tmdbId}")
                .queryParam("type", "tv");
        return builder.buildAndExpand(tmdbId).toUriString();
    }
}
