package servie.track_servie.payload.primaryKeys;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserServieKey implements Serializable
{
    private Integer userId;
    private Integer tmdbId;
    private String childtype;
}