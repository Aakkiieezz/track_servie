package servie.track_servie.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import servie.track_servie.entity.Genre;

@Component
public class GenreUtils
{
    public Set<Genre> convertTmdbGenresToTrackServieGenres(Set<Genre> tmdbGenres)
    {
        Set<Genre> trackServieGenres = new HashSet<>();
        for(Genre genre : tmdbGenres)
        {
            Integer genreId = genre.getId();
            int updatedId = genre.getId();
            if(genreId==28)
                updatedId = 1;
            else if(genreId==12)
                updatedId = 2;
            else if(genreId==16)
                updatedId = 3;
            else if(genreId==35)
                updatedId = 4;
            else if(genreId==80)
                updatedId = 5;
            else if(genreId==99)
                updatedId = 6;
            else if(genreId==18)
                updatedId = 7;
            else if(genreId==10751)
                updatedId = 8;
            else if(genreId==14)
                updatedId = 9;
            else if(genreId==36)
                updatedId = 10;
            else if(genreId==27)
                updatedId = 11;
            else if(genreId==10762)
                updatedId = 12;
            else if(genreId==10402)
                updatedId = 13;
            else if(genreId==9648)
                updatedId = 14;
            else if(genreId==10763)
                updatedId = 15;
            else if(genreId==10764)
                updatedId = 17;
            else if(genreId==10749)
                updatedId = 18;
            else if(genreId==878)
                updatedId = 19;
            else if(genreId==10766)
                updatedId = 20;
            else if(genreId==10767)
                updatedId = 21;
            else if(genreId==10770)
                updatedId = 22;
            else if(genreId==53)
                updatedId = 23;
            else if(genreId==10752)
                updatedId = 24;
            else if(genreId==37)
                updatedId = 25;
            else if(genreId==10759)
            {
                trackServieGenres.addAll(new ArrayList<>(Arrays.asList(new Genre(1, "Action"), new Genre(2, "Adventure"))));
                continue;
            }
            else if(genreId==10765)
            {
                trackServieGenres.addAll(new ArrayList<>(Arrays.asList(new Genre(19, "Science Fiction"), new Genre(9, "Fantasy"))));
                continue;
            }
            else if(genreId==10768)
            {
                trackServieGenres.addAll(new ArrayList<>(Arrays.asList(new Genre(16, "Politics"), new Genre(24, "War"))));
                continue;
            }
            trackServieGenres.add(new Genre(updatedId, genre.getName()));
        }
        return trackServieGenres;
    }
}
