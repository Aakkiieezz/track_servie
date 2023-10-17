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
    public Set<Integer> genreMapper(Set<Integer> tmdbGenreIds)
    {
        Set<Integer> trackServieGenreIds = new HashSet<>();
        for(Integer tmdbGenreId : tmdbGenreIds)
        {
            if(tmdbGenreId==28)
                trackServieGenreIds.add(1);
            else if(tmdbGenreId==12)
                trackServieGenreIds.add(2);
            else if(tmdbGenreId==10759)
                trackServieGenreIds.addAll(Arrays.asList(Integer.valueOf(1), Integer.valueOf(2)));
            else if(tmdbGenreId==16)
                trackServieGenreIds.add(3);
            else if(tmdbGenreId==35)
                trackServieGenreIds.add(4);
            else if(tmdbGenreId==80)
                trackServieGenreIds.add(5);
            else if(tmdbGenreId==99)
                trackServieGenreIds.add(6);
            else if(tmdbGenreId==18)
                trackServieGenreIds.add(7);
            else if(tmdbGenreId==10751)
                trackServieGenreIds.add(8);
            else if(tmdbGenreId==14)
                trackServieGenreIds.add(9);
            else if(tmdbGenreId==10765)
                trackServieGenreIds.addAll(Arrays.asList(Integer.valueOf(9), Integer.valueOf(19)));
            else if(tmdbGenreId==36)
                trackServieGenreIds.add(10);
            else if(tmdbGenreId==27)
                trackServieGenreIds.add(11);
            else if(tmdbGenreId==10762)
                trackServieGenreIds.add(12);
            else if(tmdbGenreId==10402)
                trackServieGenreIds.add(13);
            else if(tmdbGenreId==9648)
                trackServieGenreIds.add(14);
            else if(tmdbGenreId==10763)
                trackServieGenreIds.add(15);
            else if(tmdbGenreId==10768)
                trackServieGenreIds.addAll(Arrays.asList(Integer.valueOf(16), Integer.valueOf(24)));
            else if(tmdbGenreId==10764)
                trackServieGenreIds.add(17);
            else if(tmdbGenreId==10749)
                trackServieGenreIds.add(18);
            else if(tmdbGenreId==878)
                trackServieGenreIds.add(19);
            else if(tmdbGenreId==10766)
                trackServieGenreIds.add(20);
            else if(tmdbGenreId==10767)
                trackServieGenreIds.add(21);
            else if(tmdbGenreId==10770)
                trackServieGenreIds.add(22);
            else if(tmdbGenreId==53)
                trackServieGenreIds.add(23);
            else if(tmdbGenreId==10752)
                trackServieGenreIds.add(24);
            else if(tmdbGenreId==37)
                trackServieGenreIds.add(25);
        }
        return trackServieGenreIds;
    }

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
