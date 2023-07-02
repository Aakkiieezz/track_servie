package servie.track_servie.payload.primaryKeys;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import servie.track_servie.entity.UserServieData;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSeasonDataKey implements Serializable
{
    private UserServieData userServieData;
    private Integer seasonNumber;
}
