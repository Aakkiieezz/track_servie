package servie.track_servie.payload.dtos.operationsServiePageDtos;

import lombok.Data;

@Data
public class GenreDtoServiePage
{
    private Integer id;
    private String name;

    public GenreDtoServiePage(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }
}