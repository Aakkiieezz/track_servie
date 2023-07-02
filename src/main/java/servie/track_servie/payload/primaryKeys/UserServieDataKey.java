package servie.track_servie.payload.primaryKeys;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserServieDataKey implements Serializable
{
    private User user;
    private Servie servie;
}
