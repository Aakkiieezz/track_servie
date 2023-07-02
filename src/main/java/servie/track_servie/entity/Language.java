package servie.track_servie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Language
{
    @JsonProperty("iso_639_1")
    private String iso;
    // ---------------------------------------------------------------
    @JsonProperty("english_name")
    private String name;
}
