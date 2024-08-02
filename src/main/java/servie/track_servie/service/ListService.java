package servie.track_servie.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;
import servie.track_servie.entity.WatchList;
import servie.track_servie.exceptions.ResourceNotFoundException;
import servie.track_servie.payload.primaryKeys.ServieKey;
import servie.track_servie.repository.ListRepository;
import servie.track_servie.repository.ServieRepository;
import servie.track_servie.repository.UserRepository;

@Service
public class ListService
{
	@Autowired
	private ListRepository listRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ServieRepository servieRepository;
	@Autowired
	private ServieService servieService;

	public void addToWatchlist(@NonNull Integer userId, Integer tmdbId, String childtype)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseGet(() -> servieService.addServie(childtype, tmdbId));
		Optional<WatchList> watchlistOptional = listRepository.findByUserAndServie(user, servie);
		if(watchlistOptional.isPresent())
			listRepository.delete(watchlistOptional.get());
		else
		{
			WatchList watchList = new WatchList();
			watchList.setServie(servie);
			watchList.setUser(user);
			listRepository.save(watchList);
		}
	}
}