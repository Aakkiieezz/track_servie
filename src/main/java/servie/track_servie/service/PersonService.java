package servie.track_servie.service;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import lombok.NonNull;
import servie.track_servie.entity.Audits;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.entity.people.Person;
import servie.track_servie.enums.AuditType;
import servie.track_servie.payload.dtos.personPageDtos.PersonCastDto;
import servie.track_servie.payload.dtos.personPageDtos.PersonCreditsDto;
import servie.track_servie.payload.dtos.personPageDtos.PersonPageResponseDto;
import servie.track_servie.repository.AuditsRepository;
import servie.track_servie.repository.CustomRepository;
import servie.track_servie.repository.PersonRepository;

@Service
public class PersonService
{
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AuditsRepository auditsRepository;
	@Autowired
	private CustomRepository csri;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${tmdb.api.key}")
	private String apiKey;
	private HttpHeaders headers = new HttpHeaders();
	private HttpEntity<?> httpEntity = new HttpEntity<>(headers, (MultiValueMap<String, String>) null);

	public PersonPageResponseDto getPerson(@NonNull Integer personId)
	{
		Person person = personRepository.findById(personId).orElseGet(() -> addPerson(personId));
		PersonPageResponseDto response = new PersonPageResponseDto();
		response.setAdult(person.getAdult());
		response.setBiography(person.getBiography());
		response.setBirthPlace(person.getBirthPlace());
		response.setBirthday(person.getBirthday());
		response.setDeathday(person.getDeathday());
		response.setGender(person.getGender());
		response.setHomepage(person.getHomepage());
		response.setId(person.getId());
		response.setImdbId(person.getImdbId());
		response.setKnownForDepartment(person.getKnownForDepartment());
		response.setLastModified(person.getLastModified());
		response.setName(person.getName());
		response.setPopularity(person.getPopularity());
		response.setProfilePath(person.getProfilePath());
		List<PersonCastDto> ss = getPersonsCreditCastServies(personId);
		response.setServies(ss);
		return response;
	}

	private List<PersonCastDto> getPersonsCreditCastServies(Integer personId)
	{
		List<PersonCastDto> castedInServies = null;
		PersonCreditsDto personCreditsDto = new PersonCreditsDto();
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<PersonCreditsDto> personCreditsResponse = restTemplate.exchange(
				"https://api.themoviedb.org/3/person/"+personId+"/combined_credits?api_key="+apiKey,
				httpMethod,
				httpEntity,
				PersonCreditsDto.class);
		if(personCreditsResponse.getStatusCode()==HttpStatus.OK)
		{
			personCreditsDto = personCreditsResponse.getBody();
			if(personCreditsDto!=null)
			{
				castedInServies = personCreditsDto.getCast();
				List<Object[]> keys = castedInServies.stream()
						.map(personCastDto -> new Object[] {1, personCastDto.getChildtype(), personCastDto.getTmdbId()})
						.collect(Collectors.toList());
				List<UserServieData> userServiceDataList = csri.getUserServieData(keys);
				Map<Map.Entry<String, Integer>, UserServieData> userDataMap = new HashMap<>();
				userServiceDataList.forEach(userData -> userDataMap.put(new AbstractMap.SimpleEntry<>(userData.getServie().getChildtype(), userData.getServie().getTmdbId()), userData));
				castedInServies.forEach(personCastDto -> userDataMap.computeIfPresent(new AbstractMap.SimpleEntry<>(personCastDto.getChildtype(), personCastDto.getTmdbId()), (key, userData) ->
				{
					personCastDto.setCompleted(userData.getCompleted());
					return userData;
				}));
				castedInServies.sort(Comparator.comparing(PersonCastDto::getTitle));
			}
		}
		return castedInServies;
	}

	private Person addPerson(@NonNull Integer personId)
	{
		Audits audits = new Audits(AuditType.INSERTION, "Adding person", personId.toString());
		auditsRepository.save(audits);
		HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.GET);
		ResponseEntity<Person> personResponse = restTemplate.exchange(
				"https://api.themoviedb.org/3/person/"+personId+"?api_key="+apiKey,
				httpMethod,
				httpEntity,
				Person.class);
		if(personResponse.getStatusCode()==HttpStatus.OK)
		{
			Person person = personResponse.getBody();
			if(person!=null)
			{
				audits = new Audits(AuditType.INSERTION, "Person added successfully", personId.toString());
				auditsRepository.save(audits);
				person.setLastModified(LocalDateTime.now());
				return personRepository.save(person);
			}
		}
		return null;
	}
}
