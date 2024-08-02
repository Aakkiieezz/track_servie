package servie.track_servie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import servie.track_servie.payload.dtos.operationsMovieCollectionPageDtos.MovieCollectionPageDto;
import servie.track_servie.service.MovieCollectionService;

@Controller
@RequestMapping("/track-servie/servies/movie-collection/{id}")
public class MovieCollectionController
{
	@Autowired
	MovieCollectionService movieCollectionService;

	// Returns SeasonPage containing selected Season from SeriesPage
	@GetMapping("")
	public String getMovieCollection(@PathVariable Integer id, Model model)
	{
		MovieCollectionPageDto movieCollectionPage = movieCollectionService.getMovieCollection(id);
		model.addAttribute("movieCollection", movieCollectionPage);
		return "MovieCollectionPage";
	}
}
