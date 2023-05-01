package servie.track_servie.payload.primaryKeys;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEpisodeKey implements Serializable
{
    private String episodeId;
    private Integer userId;
    private Integer tmdbId;
    private Integer seasonNumber;
}