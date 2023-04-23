package servie.track_servie.payload.dtos.operationsImage.SeriesPageDtos;

import java.util.List;
import lombok.Data;
import servie.track_servie.payload.dtos.operationsImage.Image;

@Data
public class SeriesBackdropsDto
{
    private List<Image> backdrops;
}
