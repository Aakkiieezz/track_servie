package servie.track_servie.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import servie.track_servie.dto.EntityDtoConversion;
import servie.track_servie.dto.operationsHomePageDtos.GenreDtoHomePage;
import servie.track_servie.entity.Genre;
import servie.track_servie.repository.GenreRepository;

@Service
public class GenreService
{
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private EntityDtoConversion converter;

    // Returns list of all Genres from the database
    public List<GenreDtoHomePage> getGenres()
    {
        List<Genre> genres = genreRepository.findAll();
        List<GenreDtoHomePage> genreDtos = genres.stream().map(genre -> this.converter.genreToDtoDropdown(genre)).collect(Collectors.toList());
        return genreDtos;
    }
}