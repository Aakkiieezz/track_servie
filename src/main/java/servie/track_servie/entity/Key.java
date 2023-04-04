package servie.track_servie.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Key implements Serializable
{
    private Integer tmdbId;
    private String childtype;

    @Override
    public boolean equals(Object o)
    {
        if(this==o)
            return true;
        if(!(o instanceof Key))
            return false;
        Key key = (Key) o;
        return Objects.equals(getTmdbId(), key.getTmdbId()) && Objects.equals(getChildtype(), key.getChildtype());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getTmdbId(), getChildtype());
    }
}