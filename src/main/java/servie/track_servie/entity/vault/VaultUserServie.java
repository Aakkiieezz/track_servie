package servie.track_servie.entity.vault;

import lombok.Data;

@Data
public class VaultUserServie
{
    private Integer tmdbId;
    private String title;

    public VaultUserServie(Integer tmdbId, String title)
    {
        this.tmdbId = tmdbId;
        this.title = title;
    }
}
