package servie.track_servie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import servie.track_servie.payload.dtos.personPageDtos.PersonPageResponseDto;
import servie.track_servie.service.PersonService;

@Controller
@RequestMapping("/track-servie/person/{personId}")
public class PersonController
{
	@Autowired
	private PersonService personService;
	@Value("${user-id}")
	private Integer userId;

	@GetMapping("")
	public String getPerson(@PathVariable Integer personId, Model model)
	{
		PersonPageResponseDto response = personService.getPerson(personId);
		model.addAttribute("response", response);
		return "PersonPage";
	}
}
