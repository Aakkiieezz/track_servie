package servie.track_servie.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import servie.track_servie.payload.dtos.LanguageDto;
import servie.track_servie.service.LanguageService;

// WORKING -> BUT OF NO USE FOR NOW
@Controller
@RequestMapping("/track-servie/language")
public class LanguageController
{
	@Autowired
	private LanguageService languageService;

	@GetMapping("")
	public ResponseEntity<List<LanguageDto>> getAll()
	{
		List<LanguageDto> languages = languageService.getAll();
		for(LanguageDto language : languages)
			System.out.println(language.getIso()+" : "+language.getName());
		return new ResponseEntity<>(languages, HttpStatus.OK);
	}
}
