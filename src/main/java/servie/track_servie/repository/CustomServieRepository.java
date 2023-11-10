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
public class CustomServieRepository /* implements CustomServieRepository */
{
	@PersistenceContext
	private EntityManager entityManager;

	// @Override
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
		// TypedQuery<ServieDtoHomePage> typedQuery = entityManager.createQuery(query);
		// typedQuery.setFirstResult((int) pageable.getOffset()); // Offset is the starting index
		// typedQuery.setMaxResults(pageable.getPageSize()); // PageSize is the number of items per page
		// List<ServieDtoHomePage> results = typedQuery.getResultList();
		// CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		// Root<Servie> servieRoot = countQuery.from(Servie.class); // Use the Servie entity for counting
		// countQuery.select(cb.count(servieRoot));
		// countQuery.where(predicate);
		// Long totalElements = entityManager.createQuery(countQuery).getSingleResult();
		// Page<ServieDtoHomePage> page = new PageImpl<>(results, pageable, totalElements);
		// return page;
		// 
		// CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		// Root<UserServieData> countUsd = countQuery.from(UserServieData.class);
		// Join<UserServieData, Servie> countServie = countUsd.join("servie", JoinType.INNER);
		// countQuery.select(cb.count(countServie.get("tmdbId")));
		// countQuery.where(predicate);
		// Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();
		// // 
		// if(pageable.getSort().isSorted())
		// {
		// 	List<Order> orders = new ArrayList<>();
		// 	for(Sort.Order order : pageable.getSort())
		// 		if("title".equals(order.getProperty()))
		// 			if(order.isAscending())
		// 				orders.add(cb.asc(servie.get("title")));
		// 			else
		// 				orders.add(cb.desc(servie.get("title")));
		// 	query.orderBy(orders);
		// }
		// // 
		// TypedQuery<ServieDtoHomePage> typedQuery = entityManager.createQuery(query);
		// typedQuery.setFirstResult((int) pageable.getOffset());
		// typedQuery.setMaxResults(pageable.getPageSize());
		// return new PageImpl<>(typedQuery.getResultList(), pageable, totalRecords);
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		countQuery.select(cb.count(countQuery.from(Servie.class)));
		Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();
		TypedQuery<ServieDtoHomePage> typedQuery = entityManager.createQuery(query);
		typedQuery.setFirstResult((int) pageable.getOffset());
		typedQuery.setMaxResults(pageable.getPageSize());
		return new PageImpl<>(typedQuery.getResultList(), pageable, totalRecords);
	}
}
