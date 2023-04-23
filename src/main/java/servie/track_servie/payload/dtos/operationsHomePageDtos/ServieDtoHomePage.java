package servie.track_servie.payload.dtos.operationsHomePageDtos;

import lombok.Data;

@Data
public class ServieDtoHomePage
{
    private Integer id;
    private String title;
    private Boolean watched;
    private String posterPath;
}
