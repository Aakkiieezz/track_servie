package servie.track_servie.payload.dtos.operationsImage.SeasonPageDtos;

import java.util.List;
import lombok.Data;
import servie.track_servie.payload.dtos.operationsImage.Image;

@Data
public class SeasonPostersDto
{
    private List<Image> posters;
}
