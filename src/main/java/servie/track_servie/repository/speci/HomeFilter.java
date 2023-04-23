package servie.track_servie.repository.speci;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import servie.track_servie.entities.Genre;
import servie.track_servie.entities.Servie;

public class HomeFilter
{
    // public static Specification<Servie> withWatched(Boolean watched)
    // {
    //     return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("completed"), watched);
    // }
    // public static Specification<Servie> withGenres(List<Genre> genres)
    // {
    //     return null;
    // }
    // public Specification<Servie> getMoviesWithGenres(List<Genre> genreList) {
    //     return (Root<Servie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
    //         // Create a join between the movie and genre entities
    //         Join<Servie, Genre> genreJoin = root.join("genres");
    //         // Create a list of predicates for the genres in the genre list
    //         List<Predicate> genrePredicates = new ArrayList<>();
    //         for (String genre : genreList) {
    //             genrePredicates.add(criteriaBuilder.equal(genreJoin.get("name"), genre));
    //         }
    //         // Build the predicate to check if a movie has all the genres in the genre list
    //         Predicate allGenresPredicate = criteriaBuilder.and(genrePredicates.toArray(new Predicate[0]));
    //         // Add the allGenresPredicate to the WHERE clause of the query
    //         query.where(allGenresPredicate);
    //         return null;
    //     };
    // }
}
