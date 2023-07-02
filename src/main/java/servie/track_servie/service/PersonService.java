package servie.track_servie.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import servie.track_servie.entity.Cast;
import servie.track_servie.entity.people.Person;
import servie.track_servie.repository.PersonRepository;

@Service
public class PersonService
{
    @Autowired
    private PersonRepository personRepository;

    public void saveAllPersonsFromCast(List<Cast> casts)
    {
        for(Cast cast : casts)
        {
            Optional<Person> optionalPerson = personRepository.findById(cast.getPersonId());
            if(optionalPerson.isEmpty())
            {
                Person person = new Person();
                person.setId(cast.getPersonId());
                person.setGender(cast.getPersonGender());
                person.setName(cast.getPersonName());
                person.setProfilePath(cast.getPersonProfilePath());
                personRepository.save(person);
            }
        }
    }
}