package servie.track_servie.payload.dtos;

import lombok.Data;

@Data
public class ServieGenreMapping
{
    private Integer genreId;
    private Integer tmdbId;
    private String childtype;

    public ServieGenreMapping(Integer genreId, Integer tmdbId, String childtype)
    {
        this.genreId = genreId;
        this.tmdbId = tmdbId;
        this.childtype = childtype;
    }
}