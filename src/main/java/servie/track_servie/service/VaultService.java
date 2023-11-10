package servie.track_servie.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import servie.track_servie.entity.Series;
import servie.track_servie.entity.Servie;
import servie.track_servie.entity.User;
import servie.track_servie.entity.UserEpisodeData;
import servie.track_servie.entity.UserSeasonData;
import servie.track_servie.entity.UserServieData;
import servie.track_servie.exceptions.ResourceNotFoundException;
import servie.track_servie.payload.dtos.backupData.UserEpisodeBkpDto;
import servie.track_servie.payload.dtos.backupData.UserSeasonBkpDto;
import servie.track_servie.payload.dtos.backupData.UserServieBkpDto;
import servie.track_servie.payload.primaryKeys.ServieKey;
import servie.track_servie.payload.primaryKeys.UserSeasonDataKey;
import servie.track_servie.payload.primaryKeys.UserServieDataKey;
import servie.track_servie.repository.ServieRepository;
import servie.track_servie.repository.UserEpisodeDataRepository;
import servie.track_servie.repository.UserRepository;
import servie.track_servie.repository.UserSeasonDataRepository;
import servie.track_servie.repository.UserServieDataRepository;

@Slf4j
@Service
public class VaultService
{
	@Autowired
	private UserServieDataRepository userServieDataRepository;
	@Autowired
	private UserSeasonDataRepository userSeasonDataRepository;
	@Autowired
	private UserEpisodeDataRepository userEpisodeDataRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ServieRepository servieRepository;

	@Scheduled(fixedRate = Integer.MAX_VALUE)
	public void exportMasterData_mysqldump()
	{
		String username = "Akash";
		String password = "forever21MySQL";
		String databaseName = "track_servie";
		String dumpCommand = "mysqldump -u "+username+" -p"+password+" "+databaseName;
		try
		{
			ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", dumpCommand);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
			String bkpFileName = "MasterBkp_"+LocalDateTime.now().format(formatter)+".sql";
			processBuilder.redirectOutput(ProcessBuilder.Redirect.to(new File(bkpFileName)));
			Process process = processBuilder.start();
			int exitCode = process.waitFor();
			if(exitCode==0)
				System.out.println("Backup created successfully.");
			else
				System.err.println("Error creating backup.");
		}
		catch(IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	// @Scheduled(fixedRate = Integer.MAX_VALUE)
	// EXPORTING [SERVIE + SEASON + EPISODE] together 
	public void exportUserRawData() throws IOException
	{
		Integer userId = 1;
		log.info("Exporting data of user id {}", userId);
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		List<UserServieData> userServieDatas = userServieDataRepository.findAllByUser(user);
		String serviesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_servies.csv";
		String SeasonsFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_seasons.csv";
		String episodesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/watched_episodes.csv";
		try(CSVWriter ServieWriter = new CSVWriter(new FileWriter(serviesFilePath));
				CSVWriter SeasonWriter = new CSVWriter(new FileWriter(SeasonsFilePath));
				CSVWriter EpisodeWriter = new CSVWriter(new FileWriter(episodesFilePath)))
		{
			String[] servieHeader = {"TmdbId", "Servie Type", "Movie Watched", "Backdrop Path", "Poster Path", "Notes"};
			ServieWriter.writeNext(servieHeader);
			String[] seasonHeader = {"TmdbId", "Servie Type", "Season Number", "Poster Path", "Notes"};
			SeasonWriter.writeNext(seasonHeader);
			String[] episodeHeader = {"TmdbId", "Servie Type", "Season Number", "Episode Number", "Watched", "Notes"};
			EpisodeWriter.writeNext(episodeHeader);
			for(UserServieData userServieData : userServieDatas)
			{
				String tmdbId = userServieData.getServie().getTmdbId().toString();
				String childtype = userServieData.getServie().getChildtype();
				if(userServieData.getServie() instanceof Series)
				{
					List<UserSeasonData> userSeasonDatas = userServieData.getSeasons();
					for(UserSeasonData userSeasonData : userSeasonDatas)
					{
						String seasonNo = userSeasonData.getSeasonNo().toString();
						List<UserEpisodeData> userEpisodeDatas = userSeasonData.getEpisodes();
						for(UserEpisodeData userEpisodeData : userEpisodeDatas)
						{
							String[] episodeRow = {tmdbId, childtype, seasonNo, userEpisodeData.getEpisodeNo().toString(), Boolean.toString(userEpisodeData.getWatched()), userEpisodeData.getNotes()};
							EpisodeWriter.writeNext(episodeRow);
						}
						String[] seasonRow = {tmdbId, childtype, seasonNo, userSeasonData.getPosterPath(), userSeasonData.getNotes()};
						SeasonWriter.writeNext(seasonRow);
					}
				}
				String[] servieRow = {tmdbId, childtype, Boolean.toString(userServieData.getMovieWatched()), userServieData.getBackdropPath(), userServieData.getPosterPath(), userServieData.getNotes()};
				ServieWriter.writeNext(servieRow);
			}
		}
		log.info("    Finished exporting data of user id {}", userId);
	}

	@Scheduled(fixedRate = Integer.MAX_VALUE)
	// EXPORTING [SERVIE + SEASON + EPISODE] separately
	public void exportAllUserRawData() throws IOException
	{
		Integer userId = 1;
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		log.info("Exporting data of user id {}", userId);
		exportUserServieRawData(user);
		exportUserSeasonRawData(user);
		exportUserEpRawData(user);
	}

	private void exportUserServieRawData(User user) throws IOException
	{
		log.info("    Exporting servies");
		List<UserServieBkpDto> bkpDtos = userServieDataRepository.getMagicServieData(user);
		String serviesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/bkp_servies.csv";
		try(CSVWriter ServieWriter = new CSVWriter(new FileWriter(serviesFilePath)))
		{
			String[] servieHeader = {"TmdbId", "Servie Type", "Title", "Movie Watched", "Backdrop Path", "Poster Path", "Notes"};
			ServieWriter.writeNext(servieHeader);
			for(UserServieBkpDto dto : bkpDtos)
			{
				String tmdbId = dto.getTmdbId().toString();
				String childtype = dto.getChildtype();
				String title = dto.getTitle();
				String movieWatched = Boolean.toString(dto.getMovieWatched());
				String backdropPath = dto.getBackdropPath();
				String posterPath = dto.getPosterPath();
				String notes = dto.getNotes();
				String[] servieRow = {tmdbId, childtype, title, movieWatched, backdropPath, posterPath, notes};
				ServieWriter.writeNext(servieRow);
			}
			log.info("    Finished Exporting {} servies", bkpDtos.size());
		}
	}

	private void exportUserSeasonRawData(User user) throws IOException
	{
		log.info("    Exporting seasons");
		List<UserSeasonBkpDto> bkpDtos = userServieDataRepository.getMagicSeasonData(user);
		String SeasonsFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/bkp_seasons.csv";
		try(CSVWriter SeasonWriter = new CSVWriter(new FileWriter(SeasonsFilePath)))
		{
			String[] seasonHeader = {"TmdbId", "Servie Type", "Title", "Season Number", "Poster Path", "Notes"};
			SeasonWriter.writeNext(seasonHeader);
			for(UserSeasonBkpDto dto : bkpDtos)
			{
				String tmdbId = dto.getTmdbId().toString();
				String childtype = dto.getChildtype();
				String title = dto.getTitle();
				String seasonNo = dto.getSeasonNo().toString();
				String posterPath = dto.getPosterPath();
				String notes = dto.getNotes();
				String[] seasonRow = {tmdbId, childtype, title, seasonNo, posterPath, notes};
				SeasonWriter.writeNext(seasonRow);
			}
			log.info("    Finished Exporting {} seasons", bkpDtos.size());
		}
	}

	private void exportUserEpRawData(User user) throws IOException
	{
		log.info("    Exporting episodes");
		List<UserEpisodeBkpDto> bkpDtos = userServieDataRepository.getMagicEpData(user);
		String episodesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/bkp_episodes.csv";
		try(CSVWriter EpisodeWriter = new CSVWriter(new FileWriter(episodesFilePath)))
		{
			String[] seasonHeader = {"TmdbId", "Title", "Season Number", "Episode Number", "Watched", "Notes"};
			EpisodeWriter.writeNext(seasonHeader);
			for(UserEpisodeBkpDto dto : bkpDtos)
			{
				String tmdbId = dto.getTmdbId().toString();
				String title = dto.getTitle();
				String seasonNo = dto.getSeasonNo().toString();
				String epNo = dto.getEpisodeNo().toString();
				String watched = Boolean.toString(dto.getWatched());
				String notes = dto.getNotes();
				String[] episodeRow = {tmdbId, title, seasonNo, epNo, watched, notes};
				EpisodeWriter.writeNext(episodeRow);
			}
			log.info("    Finished Exporting {} episodes", bkpDtos.size());
		}
	}

	// @Scheduled(fixedRate = Integer.MAX_VALUE)
	@Transactional // should be jakarta or of springFramework ???
	public void importAllUserRawData() throws IOException, CsvException
	{
		Integer userId = 1;
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
		log.info("Importing data of user id {}", userId);
		importUserServiesData(user);
		importUserSeasonsData(user);
		importUserEpisodesData(user);
		log.info("Finished importing", userId);
	}

	private void importUserServiesData(User user) throws IOException, CsvException
	{
		log.info("    Importing servies");
		String serviesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/bkp_servies.csv";
		CSVReader reader = new CSVReader(new FileReader(serviesFilePath));
		reader.skip(1);
		List<String[]> rows = reader.readAll();
		List<UserServieData> userServieDatas = new ArrayList<>();
		for(String[] row : rows)
		{
			// "TmdbId","Servie Type","Title","Movie Watched","Backdrop Path","Poster Path","Notes"
			Integer tmdbId = Integer.parseInt(row[0]);
			String childtype = row[1];
			Boolean movieWatched = Boolean.valueOf(row[3]);
			String backdropPath = row[4].isEmpty()? null : row[4];
			String posterPath = row[5].isEmpty()? null : row[5];
			String notes = row[6].isEmpty()? null : row[6];
			Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
			UserServieData userServieData = new UserServieData();
			userServieData.setUser(user);
			userServieData.setServie(servie);
			userServieData.setMovieWatched(movieWatched);
			userServieData.setBackdropPath(backdropPath);
			userServieData.setPosterPath(posterPath);
			userServieData.setNotes(notes);
			userServieDatas.add(userServieData);
		}
		userServieDataRepository.saveAll(userServieDatas);
		log.info("    Finished Importing {} servies", userServieDatas.size());
	}

	private void importUserSeasonsData(User user) throws IOException, CsvException
	{
		log.info("    Importing seasons");
		String SeasonsFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/bkp_seasons.csv";
		CSVReader reader = new CSVReader(new FileReader(SeasonsFilePath));
		reader.skip(1);
		List<String[]> rows = reader.readAll();
		List<UserSeasonData> userSeasonDatas = new ArrayList<>();
		for(String[] row : rows)
		{
			// "TmdbId","Servie Type","Title","Season Number","Poster Path","Notes"
			Integer tmdbId = Integer.parseInt(row[0]);
			String childtype = row[1];
			Integer seasonNumber = Integer.parseInt(row[3]);
			String posterPath = row[4];
			String notes = row[5];
			Servie servie = servieRepository.findById(new ServieKey(childtype, tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey(childtype, tmdbId).toString()));
			UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseThrow(() -> new ResourceNotFoundException("UserServieData", "UserServieDataKey", new UserServieDataKey(user, servie).toString()));
			UserSeasonData userSeasonData = new UserSeasonData();
			userSeasonData.setUserServieData(userServieData);
			userSeasonData.setSeasonNo(seasonNumber);
			userSeasonData.setPosterPath(posterPath);
			userSeasonData.setNotes(notes);
			userSeasonDatas.add(userSeasonData);
		}
		userSeasonDataRepository.saveAll(userSeasonDatas);
		log.info("    Finished Importing {} seasons", userSeasonDatas.size());
	}

	private void importUserEpisodesData(User user) throws IOException, CsvException
	{
		log.info("    Importing episodes");
		String EpisodesFilePath = "/home/aakkiieezz/Coding/track_servie/user_backups/bkp_episodes.csv";
		CSVReader reader = new CSVReader(new FileReader(EpisodesFilePath));
		reader.skip(1);
		List<String[]> rows = reader.readAll();
		List<UserEpisodeData> userEpisodeDatas = new ArrayList<>();
		for(String[] row : rows)
		{
			// "TmdbId","Title","Season Number","Episode Number","Watched","Notes"
			Integer tmdbId = Integer.parseInt(row[0]);
			Integer seasonNo = Integer.parseInt(row[2]);
			Integer episodeNo = Integer.parseInt(row[3]);
			Boolean watched = Boolean.valueOf(row[4]);
			String notes = row[5].isEmpty()? null : row[5];
			Servie servie = servieRepository.findById(new ServieKey("tv", tmdbId)).orElseThrow(() -> new ResourceNotFoundException("Servie", "ServieKey", new ServieKey("tv", tmdbId).toString()));
			UserServieData userServieData = userServieDataRepository.findById(new UserServieDataKey(user, servie)).orElseThrow(() -> new ResourceNotFoundException("UserServieData", "UserServieDataKey", new UserServieDataKey(user, servie).toString()));
			UserSeasonData userSeasonData = userSeasonDataRepository.findById(new UserSeasonDataKey(userServieData, seasonNo)).orElseThrow(() -> new ResourceNotFoundException("UserServieData", "UserSeasonDataKey", new UserSeasonDataKey(userServieData, seasonNo).toString()));
			UserEpisodeData userEpisodeData = new UserEpisodeData();
			userEpisodeData.setUserSeasonData(userSeasonData);
			userEpisodeData.setEpisodeNo(episodeNo);
			userEpisodeData.setWatched(watched);
			userEpisodeData.setNotes(notes);
			userEpisodeDatas.add(userEpisodeData);
		}
		userEpisodeDataRepository.saveAll(userEpisodeDatas);
		log.info("    Finished Importing {} episodes", userEpisodeDatas.size());
	}
}