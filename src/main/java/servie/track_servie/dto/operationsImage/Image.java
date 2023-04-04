package servie.track_servie.dto.operationsImage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Image
{
    @JsonProperty("aspect_ratio")
    private float aspectRatio;
    private String iso_639_1;
    private int height;
    @JsonProperty("file_path")
    private String filePath;
    @JsonProperty("vote_average")
    private float voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;
    private int width;
}