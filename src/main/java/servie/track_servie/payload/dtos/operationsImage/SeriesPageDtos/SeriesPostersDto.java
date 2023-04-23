package servie.track_servie.payload.dtos.operationsImage.SeriesPageDtos;

import java.util.List;
import lombok.Data;
import servie.track_servie.payload.dtos.operationsImage.Image;

@Data
public class SeriesPostersDto
{
    private List<Image> posters;
}
