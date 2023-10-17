package servie.track_servie.payload.primaryKeys;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import servie.track_servie.entity.UserSeasonData;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEpisodeDataKey implements Serializable
{
    private UserSeasonData userSeasonData;
    private Integer episodeNo;
}