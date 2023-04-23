package servie.track_servie.payload.dtos.operationsImage.EpisodePageDtos;

import java.util.List;
import lombok.Data;
import servie.track_servie.payload.dtos.operationsImage.Image;

@Data
public class EpisodeStillsDto
{
    private List<Image> stills;
}