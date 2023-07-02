package servie.track_servie.entity.vault;

import lombok.Data;

@Data
public class VaultUserSeries
{
    private Integer tmdbId;
    private String title;

    public VaultUserSeries(Integer tmdbId, String title)
    {
        this.tmdbId = tmdbId;
        this.title = title;
    }
}
