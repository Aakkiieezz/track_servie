package servie.track_servie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import servie.track_servie.payload.primaryKeys.UserEpisodeDataKey;

@Data
@Entity
@NoArgsConstructor
@IdClass(UserEpisodeDataKey.class)
public class UserEpisodeData
{
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JoinColumn(name = "tmdb_id", referencedColumnName = "tmdb_id")
    @JoinColumn(name = "childtype", referencedColumnName = "childtype")
    @JoinColumn(name = "season_number", referencedColumnName = "season_number")
    private UserSeasonData userSeasonData;
    // ---------------------------------------------------------------
    @Id
    @Column(name = "episode_number")
    private Integer episodeNumber;
    // ---------------------------------------------------------------
    @Column(name = "watched", nullable = false)
    private Boolean watched = false;
    @Column(name = "notes")
    private String notes;

    public UserEpisodeData(UserSeasonData userSeasonData, Integer episodeNumber)
    {
        this.userSeasonData = userSeasonData;
        this.episodeNumber = episodeNumber;
    }

    public UserEpisodeData(/* UserSeasonData userSeasonData, */Integer episodeNumber, Boolean watched, String notes)
    {
        // this.userSeasonData = userSeasonData;
        this.episodeNumber = episodeNumber;
        this.watched = watched;
        this.notes = notes;
    }
}
