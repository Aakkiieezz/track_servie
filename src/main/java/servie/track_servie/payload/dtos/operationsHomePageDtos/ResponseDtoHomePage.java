package servie.track_servie.payload.dtos.operationsHomePageDtos;

import java.util.List;
import lombok.Data;

@Data
public class ResponseDtoHomePage
{
    private List<ServieDtoHomePage> servies;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private Boolean lastPage;
}
