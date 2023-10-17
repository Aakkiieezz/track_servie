package servie.track_servie.entity.credits;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class SeasonCredits
{
    @Id
    @JsonProperty("id")
    private Integer id;
    // ---------------------------------------------------------------
    @JsonProperty("cast")
    private Set<SeasonCast> cast;
    // ---------------------------------------------------------------
    // @JsonProperty("crew")
    // private List<SeasonCast> crew;
}
