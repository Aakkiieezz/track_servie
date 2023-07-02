package servie.track_servie.entity.credits;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import servie.track_servie.entity.Cast;

@Data
public class ServieCredits
{
    // @JsonProperty("id")
    // private Integer id;
    // ---------------------------------------------------------------
    @JsonProperty("cast")
    private List<Cast> cast;
    // ---------------------------------------------------------------
    // @JsonProperty("crew")
    // private List<Crew> crew;
}
