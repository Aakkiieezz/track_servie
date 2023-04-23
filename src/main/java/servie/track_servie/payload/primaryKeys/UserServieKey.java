package servie.track_servie.payload.primaryKeys;

import java.io.Serializable;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserServieKey
{
    @Column(name = "user_id")
    private Integer UserId;
    // ---------------------------------------------------------------
    @Column(name = "imdb_id")
    private String imdbId;
    // ---------------------------------------------------------------
    // @Override
    // public boolean equals(Object o)
    // {
    //     if(this==o)
    //         return true;
    //     if(!(o instanceof ServieKey))
    //         return false;
    //     ServieKey key = (ServieKey) o;
    //     return Objects.equals(getTmdbId(), key.getTmdbId()) && Objects.equals(getChildtype(), key.getChildtype());
    // }
    // @Override
    // public int hashCode()
    // {
    //     return Objects.hash(getTmdbId(), getChildtype());
    // }
}