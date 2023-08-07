package servie.track_servie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import servie.track_servie.payload.dtos.operationsSearch.SeriesPageDtos.SeriesDtoSearchSeriesPage;
import servie.track_servie.service.SeriesService;

@Controller
@RequestMapping("/track-servie/servies")
public class SeriesController
{
    @Autowired
    private SeriesService seriesService;

    // Returns SearchSeriesPage containing selected Series from SearchPage
    @GetMapping("{tmdbId}/search")
    public String searchSeries(@PathVariable Integer tmdbId, Model model)
    {
        SeriesDtoSearchSeriesPage seriesDto = seriesService.searchSeries(tmdbId);
        model.addAttribute("series", seriesDto);
        return "SearchSeriesPage";
    }
}