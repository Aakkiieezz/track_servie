package servie.track_servie.dto.operationsHomePageDtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MovieDtoHomePage extends ServieDtoHomePage
{
    private String releaseDate;
}
