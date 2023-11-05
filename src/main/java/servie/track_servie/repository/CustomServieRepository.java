package servie.track_servie.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import servie.track_servie.entity.Genre;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage;

public interface CustomServieRepository
{
	Page<ServieDtoHomePage> getTempDtosCB(String childtype, List<String> languages, List<Genre> genres, List<String> statuses, Pageable pageable);
}
