package servie.track_servie.payload.dtos.operationsSeriesPageDtos;

import lombok.Data;

@Data
public class GenreDtoSeriesPage
{
    private Integer id;
    private String name;

    public GenreDtoSeriesPage(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }
}