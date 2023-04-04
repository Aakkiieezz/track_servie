package servie.track_servie.dto.operationsSearch.SearchPageDtos;

import java.util.List;
import lombok.Data;

@Data
public class SearchResultDtoSearchPage
{
    // private int page;
    private List<SeriesDtoSearchPage> results;
    // private int total_pages;
    // private int total_results;
}