package servie.track_servie.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ResponseDtoHomePage;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos.SearchResultDtoSearchPage;
import servie.track_servie.payload.dtos.operationsServiePageDtos.ServieDtoServiePage;
import servie.track_servie.service.ServieService;

@Controller
@RequestMapping("/track-servie/servies")
public class ServieController
{
	@Autowired
	private ServieService servieService;
	@Value("${user-id}")
	private Integer userId;

	@GetMapping("login")
	public String showLoginForm()
	{
		return "login";
	}

	// Returns HomePage containing all Servies from the database which matches the filter
	@GetMapping("")
	public String getServiesByFilter(@RequestParam(value = "type", defaultValue = "") String type, @RequestParam(value = "watched", required = false) Boolean watched, @RequestParam(value = "genreIds", required = false) List<Integer> genreIds, @RequestParam(value = "languages", required = false) List<String> languages, @RequestParam(value = "statuses", required = false) List<String> statuses, @RequestParam(value = "startYear", defaultValue = "") Integer startYear, @RequestParam(value = "endYear", defaultValue = "") Integer endYear, @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "sortBy", defaultValue = "title") String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir, Model model)
	{
		ResponseDtoHomePage response = servieService.getServiesByFilter(userId, type, watched, genreIds, languages, statuses, startYear, endYear, pageNumber, sortBy, sortDir);
		model.addAttribute("response", response);
		return "HomePage";
	}

	// Returns SearchPage containing all searched Servies from 3rd party api
	@GetMapping("search")
	public String searchServies(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "query", required = true) String servieName,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, Model model, HttpServletRequest request)
	{
		SearchResultDtoSearchPage response = servieService.searchServies(userId, type, servieName, year, pageNumber);
		model.addAttribute("response", response);
		model.addAttribute("query", servieName);
		model.addAttribute("type", type);
		return "SearchPage";
	}

	// Returns SeriesPage containing selected Series from HomePage
	@GetMapping("{tmdbId}")
	public String getServie(@RequestParam(value = "type", required = true) String type, @PathVariable Integer tmdbId, Model model)
	{
		ServieDtoServiePage servie = servieService.getServie(userId, type, tmdbId);
		model.addAttribute("servie", servie);
		return "ServiePage";
	}

	// Removes specific Series(including all its Seasons,Episodes,...) from the database
	// ??? why cannot use @DeleteMapping, when using 'Delete' getting { status: 405, error: "Method Not Allowed", trace: "Request method 'DELETE' not supported" } in postman
	/*
	 * In browser - Whitelabel Error Page This application has no explicit mapping
	 * for /error, so you are seeing this as a fallback. There was an unexpected
	 * error (type=Bad Request, status=400) Failed to convert value of type
	 * 'java.lang.String' to required type 'java.lang.Integer'; nested exception is
	 * java.lang.NumberFormatException: For input string: "remove"
	 * org.springframework.web.method.annotation.
	 * MethodArgumentTypeMismatchException: Failed to convert value of type
	 * 'java.lang.String' to required type 'java.lang.Integer'; nested exception is
	 * java.lang.NumberFormatException: For input string: "remove"
	 */
	@PostMapping("remove")
	@ResponseBody
	public Map<String, Object> removeServie(@RequestBody Map<String, Object> payload)
	{
		Integer tmdbId = Integer.parseInt((String) payload.get("tmdbId"));
		String childtype = (String) payload.get("childtype");
		servieService.removeServie(tmdbId, childtype);
		return Collections.singletonMap("success", true);
	}

	// Toggle 1 - Hyperlink
	// Toggles the watch button of Series located on HomePage
	@GetMapping("{tmdbId}/toggleback")
	public String toggleSeriesWatch(@PathVariable Integer tmdbId, @RequestParam(value = "type", required = true) String type)
	{
		servieService.toggleServieWatch(userId, type, tmdbId);
		// Albummm album = albumService.getFirstFalse();
		// album.setDone(true);
		// albumRepository.save(album);
		// Optional<Albummm> album2 = albumService.getById(album.getId() + 1);
		// if(album2.isPresent())
		// {
		//     Albummm album3 = album2.get();
		//     return "redirect:/track-servie/servies/search?query="+album3.getName()+"&year="+album3.getYear()+"&type=movie";
		// }
		// else
		return "redirect:/track-servie/servies";
	}

	// Toggle 2 - Javascript/AJAX
	@PostMapping("js_toggleback")
	@ResponseBody
	public Map<String, Object> updateWatched(@RequestBody Map<String, Object> payload)
	{
		Integer tmdbId = Integer.parseInt((String) payload.get("tmdbId"));
		String childtype = (String) payload.get("childtype");
		servieService.toggleServieWatch(userId, childtype, tmdbId);
		return Collections.singletonMap("success", true);
	}

	// Toggles the watch button of Series located on SeriesPage
	@GetMapping("{tmdbId}/toggle")
	public String toggleSerWatch(@PathVariable Integer tmdbId, @RequestParam(value = "type", required = true) String type)
	{
		servieService.toggleServieWatch(userId, type, tmdbId);
		return "redirect:/track-servie/servies/"+tmdbId+"?type="+type;
	}

	@GetMapping("{tmdbId}/backdrops")
	public String getServieBackdrops(@RequestParam(value = "type", required = true) String type, @PathVariable Integer tmdbId, Model model)
	{
		List<Image> backdrops = servieService.getServieBackdrops(type, tmdbId);
		model.addAttribute("backdrops", backdrops);
		model.addAttribute("tmdbId", tmdbId);
		model.addAttribute("type", type);
		return "ImageSearchSeriesPage2";
	}

	// Redirects to SeriesPage with changed Series Poster
	@GetMapping("{tmdbId}/backdropChange")
	public String changeBackdrop(@RequestParam(value = "type", required = true) String type, @PathVariable Integer tmdbId, @RequestParam(value = "filePath", defaultValue = "") String filePath, Model model)
	{
		servieService.changeBackdrop(userId, type, tmdbId, filePath);
		return "redirect:/track-servie/servies/"+tmdbId+"?type="+type;
	}

	// Returns ImageSearchSeriesPage containing list of Posters(for specific Series)
	@GetMapping("{tmdbId}/posters")
	public String getSeriesPosters(@RequestParam(value = "type", required = true) String type, @PathVariable Integer tmdbId, Model model)
	{
		List<Image> posters = servieService.getServiePosters(type, tmdbId);
		model.addAttribute("posters", posters);
		model.addAttribute("tmdbId", tmdbId);
		model.addAttribute("type", type);
		return "ImageSearchSeriesPage";
	}

	// Redirects to SeriesPage with changed Series Poster
	@GetMapping("{tmdbId}/posterChange")
	public String changePoster(@RequestParam(value = "type", required = true) String type, @PathVariable Integer tmdbId, @RequestParam(value = "filePath", defaultValue = "") String filePath, Model model)
	{
		servieService.changePoster(userId, type, tmdbId, filePath);
		return "redirect:/track-servie/servies"; // ??? why can't we return the HomePage directly instead
	}
}
