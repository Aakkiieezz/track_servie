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
import jakarta.servlet.http.HttpSession;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ResponseDtoHomePage;
import servie.track_servie.payload.dtos.operationsImage.Image;
import servie.track_servie.payload.dtos.operationsSearch.SearchPageDtos.SearchResultDtoSearchPage;
import servie.track_servie.entities.Servie;
import servie.track_servie.service.ServieService;

@Controller
@RequestMapping("/api/servies")
public class ServieController
{
    @Autowired
    private ServieService servieService;
    @Value("${userId}")
    private Integer userId;

    @GetMapping("login")
    public String showLoginForm()
    {
        return "login";
    }

    // Returns HomePage containing all Servies from the database which matches the filter
    @GetMapping("")
    public String getServiesByFilter(@RequestParam(value = "type", defaultValue = "") String type, @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "24") int pageSize, @RequestParam(value = "sortBy", defaultValue = "title") String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir, @RequestParam(value = "genreIds", defaultValue = "") List<Integer> genreIds, @RequestParam(value = "watched", defaultValue = "") Boolean watched, Model model)
    {
        ResponseDtoHomePage response = servieService.getServiesByFilter(userId, type, pageNumber, pageSize, sortBy, sortDir, genreIds, watched);
        // List<GenreDtoHomePage> genres = genreService.getGenres();
        // model.addAttribute("genres", genres);
        model.addAttribute("response", response);
        return "HomePage";
    }

    // ---------------------------------------------------------------
    // Returns SearchPage containing all searched Servies from 3rd party api
    // ??? what happens when required is true and no default is given
    @GetMapping("search")
    // @CrossOrigin(origins = "http://localhost:8080")
    public String searchServies(@RequestParam(value = "type", required = true) String type, @RequestParam(value = "query", required = true) String servieName, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, Model model/*
                                                                                                                                                                                                                                                   * , @RequestHeader HttpHeaders headers
                                                                                                                                                                                                                                                   */, HttpServletRequest request)
    {
        // checking if token exists in header
        // String authorizationHeader = headers.getFirst("Authorization");
        // System.out.println(authorizationHeader);
        // checking if token exists in header
        // System.out.println(request.getHeader("Authorization"));
        SearchResultDtoSearchPage response = servieService.searchServies(type, servieName, pageNumber);
        model.addAttribute("response", response);
        // should the type be inside the model attribute "searchList" ?
        model.addAttribute("query", servieName);
        model.addAttribute("type", type);
        // return new ResponseEntity<>("Not a thymeleaf template, just a String",
        // HttpStatus.OK);
        // request.setAttribute("isthymeleaf", true);
        // model.addAttribute("thymeleafServletRequest", true);
        return "SearchPage";
    }

    // Adds Servie into the database after getting data from 3rd party api
    // ??? why cannot use @PostMapping, when using 'Post' getting { status: 405, error: "Method Not Allowed", trace: "Request method 'POST' not supported" } in postman
    /*
     * In browser - Whitelabel Error Page This application has no explicit mapping
     * for /error, so you are seeing this as a fallback. There was an unexpected
     * error (type=Bad Request, status=400) Failed to convert value of type
     * 'java.lang.String' to required type 'java.lang.Integer'; nested exception is
     * java.lang.NumberFormatException: For input string: "add"
     * org.springframework.web.method.annotation.
     * MethodArgumentTypeMismatchException: Failed to convert value of type
     * 'java.lang.String' to required type 'java.lang.Integer'; nested exception is
     * java.lang.NumberFormatException: For input string: "add"}
     */
    @GetMapping("add")
    public String addServie(@RequestParam(value = "type", required = true) String type, @RequestParam(value = "id", required = true) Integer tmdbId, HttpSession session)
    {
        servieService.addServie(userId, type, tmdbId);
        session.setAttribute("msg", "Item Added Successfully...!");
        return "redirect:/api/servies";
    }

    // Returns SeriesPage containing selected Series from HomePage
    @GetMapping("{tmdbId}")
    public String getServie(@RequestParam(value = "type", required = true) String type, @PathVariable Integer tmdbId, Model model)
    {
        Servie servie = servieService.getServie(type, tmdbId);
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
        String imdbId = (String) payload.get("imdbId");
        servieService.removeServie(imdbId);
        return Collections.singletonMap("success", true);
    }

    // Toggles the watch button of Series located on HomePage
    @GetMapping("{tmdbId}/toggleback")
    public String toggleSeriesWatch(@PathVariable Integer tmdbId, @RequestParam(value = "type", required = true) String type)
    {
        servieService.toggleServieWatch(userId, type, tmdbId);
        return "redirect:/api/servies";
    }

    // trial - to delete
    @PostMapping("tog")
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
        return "redirect:/api/servies/"+tmdbId+"?type="+type;
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
        servieService.changeBackdrop(type, tmdbId, filePath);
        return "redirect:/api/servies/"+tmdbId+"?type="+type;
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
        servieService.changePoster(type, tmdbId, filePath);
        return "redirect:/api/servies"; // ??? why can't we return the HomePage directly instead
    }
}
