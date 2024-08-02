package servie.track_servie.payload.dtos;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import servie.track_servie.entity.Episode;
import servie.track_servie.entity.Genre;
import servie.track_servie.entity.Season;
import servie.track_servie.payload.dtos.episodePageDtos.EpisodeDtoEpisodePage;
import servie.track_servie.payload.dtos.operationsHomePageDtos.GenreDtoHomePage;
// import servie.track_servie.payload.dtos.operationsHomePageDtos.SeriesDtoHomePage;
import servie.track_servie.payload.dtos.operationsSeasonPageDtos.EpisodeDtoSeasonPage;
import servie.track_servie.payload.dtos.operationsSeasonPageDtos.SeasonDtoSeasonPage;
import servie.track_servie.payload.dtos.operationsServiePageDtos.GenreDtoServiePage;

@Component
public class EntityDtoConversion
{
	ModelMapper mapper = new ModelMapper();

	public GenreDtoHomePage genreToDtoDropdown(Genre genre)
	{
		return mapper.map(genre, GenreDtoHomePage.class);
	}
	// Series Page conversions
	// public SeriesDtoSeriesPage seriesToDto(Series series)
	// {
	//     SeriesDtoSeriesPage seriesDto = mapper.map(series, SeriesDtoSeriesPage.class);
	//     List<Season> seasons = series.getSeasons();
	//     List<SeasonDtoSeriesPage> seasonDtos = seasons.stream().map(season -> this.seasonToDtoSeriesPage(season)).collect(Collectors.toList());
	//     seriesDto.setSeasons(seasonDtos);
	//     Set<Genre> genres = series.getGenres();
	//     Set<GenreDtoSeriesPage> genreDtosSeriesPage = genres.stream().map(genre -> this.genreToDtoSeriesPage(genre)).collect(Collectors.toSet());
	//     seriesDto.setGenres(genreDtosSeriesPage);
	//     return seriesDto;
	// }
	// public SeasonDtoSeriesPage seasonToDtoSeriesPage(Season season)
	// {
	//     return mapper.map(season, SeasonDtoSeriesPage.class);
	// }

	public GenreDtoServiePage genreToDtoSeriesPage(Genre genre)
	{
		return mapper.map(genre, GenreDtoServiePage.class);
	}

	// Season Page conversions
	public SeasonDtoSeasonPage seasonToDtoSeasonPage(Season season)
	{
		SeasonDtoSeasonPage seasonDto = mapper.map(season, SeasonDtoSeasonPage.class);
		// List<Episode> episodes = season.getEpisodes();
		// List<EpisodeDtoSeasonPage> episodeDtos = episodes.stream().map(episode -> this.episodeToDtoSeasonPage(episode)).collect(Collectors.toList());
		// seasonDto.setEpisodes(episodeDtos);
		return seasonDto;
	}

	public EpisodeDtoSeasonPage episodeToDtoSeasonPage(Episode episode)
	{
		return mapper.map(episode, EpisodeDtoSeasonPage.class);
	}

	// Episode Page conversions
	public EpisodeDtoEpisodePage episodeToDtoEpisodePage(Episode episode)
	{
		return mapper.map(episode, EpisodeDtoEpisodePage.class);
	}
}
