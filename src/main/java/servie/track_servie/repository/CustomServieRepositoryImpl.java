package servie.track_servie.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import servie.track_servie.entity.Genre;
import servie.track_servie.entity.Servie;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage;

@Repository
public class CustomServieRepositoryImpl implements CustomServieRepository
{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<ServieDtoHomePage> getTempDtosCB(String childtype, List<String> languages, List<Genre> genres, List<String> statuses, Pageable pageable)
	{
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ServieDtoHomePage> query = cb.createQuery(ServieDtoHomePage.class);
		Root<Servie> root = query.from(Servie.class);
		// Join<Servie, UserServieData> usd = root.join("servie", JoinType.INNER);
		query.select(cb.construct(
				ServieDtoHomePage.class,
				root.get("imdbId"),
				root.get("tmdbId"),
				root.get("childtype"),
				root.get("title"),
				root.get("posterPath"),
				root.get("releaseDate"),
				cb.literal(1000), // totalEpisodes
				root.get("firstAirDate"),
				root.get("lastAirDate"),
				// usd.get("episodesWatched"),
				cb.literal(50), // episodesWatched
				cb.literal(false))); // completed
		Predicate predicate = cb.conjunction();
		if(languages!=null && !languages.isEmpty())
			predicate = cb.and(predicate, root.get("originalLanguage").in(languages));
		if(genres!=null && !genres.isEmpty())
		{
			List<Predicate> genrePredicates = new ArrayList<>();
			for(Genre genre : genres)
			{
				Expression<Set<Genre>> rootGenres = root.get("genres");
				Predicate genreMatch = cb.isMember(genre, rootGenres);
				genrePredicates.add(genreMatch);
			}
			Predicate allGenresMatch = cb.and(genrePredicates.toArray(new Predicate[0]));
			predicate = cb.and(predicate, allGenresMatch);
		}
		if(statuses!=null && !statuses.isEmpty())
			predicate = cb.and(predicate, root.get("status").in(statuses));
		if(childtype!=null && !childtype.equals(""))
			predicate = cb.and(predicate, cb.equal(root.get("childtype"), childtype));
		query.where(predicate);
		if(pageable.getSort().isSorted())
		{
			List<Order> orders = new ArrayList<>();
			for(Sort.Order order : pageable.getSort())
				if("title".equals(order.getProperty()))
					if(order.isAscending())
						orders.add(cb.asc(root.get("title")));
					else
						orders.add(cb.desc(root.get("title")));
			query.orderBy(orders);
		}
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		countQuery.select(cb.count(countQuery.from(Servie.class))); // Count all records
		Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();
		TypedQuery<ServieDtoHomePage> typedQuery = entityManager.createQuery(query);
		typedQuery.setFirstResult((int) pageable.getOffset());
		typedQuery.setMaxResults(pageable.getPageSize());
		return new PageImpl<>(typedQuery.getResultList(), pageable, totalRecords);
	}
}
