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
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import servie.track_servie.entity.Genre;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage;

@Repository
public class CustomServieRepository
{
	@PersistenceContext
	private EntityManager entityManager;

	public Page<ServieDtoHomePage> getTempDtosCB(User user, String childtype, List<String> languages, List<Genre> genres, List<String> statuses, Pageable pageable)
	{
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ServieDtoHomePage> query = cb.createQuery(ServieDtoHomePage.class);
		Root<UserServieData> usd = query.from(UserServieData.class);
		Join<UserServieData, Servie> servie = usd.join("servie", JoinType.INNER);
		query.select(cb.construct(
				ServieDtoHomePage.class,
				servie.get("imdbId"),
				servie.get("tmdbId"),
				servie.get("childtype"),
				servie.get("title"),
				cb.coalesce(usd.get("posterPath"), servie.get("posterPath")),
				servie.get("releaseDate"),
				servie.get("totalEpisodes"),
				servie.get("firstAirDate"),
				servie.get("lastAirDate"),
				usd.get("episodesWatched"),
				usd.get("completed")));
		Predicate predicate = cb.conjunction();
		predicate = cb.and(predicate, cb.equal(usd.get("user"), user));
		if(languages!=null && !languages.isEmpty())
			predicate = cb.and(predicate, servie.get("originalLanguage").in(languages));
		if(genres!=null && !genres.isEmpty())
		{
			List<Predicate> genrePredicates = new ArrayList<>();
			for(Genre genre : genres)
			{
				Expression<Set<Genre>> rootGenres = servie.get("genres");
				Predicate genreMatch = cb.isMember(genre, rootGenres);
				genrePredicates.add(genreMatch);
			}
			Predicate allGenresMatch = cb.and(genrePredicates.toArray(new Predicate[0]));
			predicate = cb.and(predicate, allGenresMatch);
		}
		if(statuses!=null && !statuses.isEmpty())
			predicate = cb.and(predicate, servie.get("status").in(statuses));
		if(childtype!=null && !childtype.equals(""))
			predicate = cb.and(predicate, cb.equal(servie.get("childtype"), childtype));
		query.where(predicate);
		if(pageable.getSort().isSorted())
		{
			List<Order> orders = new ArrayList<>();
			for(Sort.Order order : pageable.getSort())
				if("title".equals(order.getProperty()))
					if(order.isAscending())
						orders.add(cb.asc(servie.get("title")));
					else
						orders.add(cb.desc(servie.get("title")));
			query.orderBy(orders);
		}
		TypedQuery<ServieDtoHomePage> typedQuery = entityManager.createQuery(query);
		typedQuery.setFirstResult((int) pageable.getOffset()); // Offset is the starting index
		typedQuery.setMaxResults(pageable.getPageSize()); // PageSize is the number of items per page
		List<ServieDtoHomePage> results = typedQuery.getResultList();
		//
		// Another query just for counting total elements present after applying all filters
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<UserServieData> count_usd = countQuery.from(UserServieData.class);
		Join<UserServieData, Servie> count_servie = count_usd.join("servie", JoinType.INNER);
		countQuery.select(cb.count(count_usd));
		Predicate countPredicate = cb.conjunction();
		countPredicate = cb.and(countPredicate, cb.equal(count_usd.get("user"), user));
		if(languages!=null && !languages.isEmpty())
			countPredicate = cb.and(countPredicate, count_servie.get("originalLanguage").in(languages));
		if(genres!=null && !genres.isEmpty())
		{
			List<Predicate> genrePredicates = new ArrayList<>();
			for(Genre genre : genres)
			{
				Expression<Set<Genre>> rootGenres = count_servie.get("genres");
				Predicate genreMatch = cb.isMember(genre, rootGenres);
				genrePredicates.add(genreMatch);
			}
			Predicate allGenresMatch = cb.and(genrePredicates.toArray(new Predicate[0]));
			countPredicate = cb.and(countPredicate, allGenresMatch);
		}
		if(statuses!=null && !statuses.isEmpty())
			countPredicate = cb.and(countPredicate, count_servie.get("status").in(statuses));
		if(childtype!=null && !childtype.equals(""))
			countPredicate = cb.and(countPredicate, cb.equal(count_servie.get("childtype"), childtype));
		countQuery.where(countPredicate);
		Long totalElements = entityManager.createQuery(countQuery).getSingleResult();
		Page<ServieDtoHomePage> page = new PageImpl<>(results, pageable, totalElements);
		return page;
	}
}
