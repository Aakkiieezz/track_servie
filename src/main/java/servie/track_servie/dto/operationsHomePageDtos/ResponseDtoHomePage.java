package servie.track_servie.dto.operationsHomePageDtos;

import java.util.List;
import lombok.Data;
import servie.track_servie.entity.Servie;

@Data
public class ResponseDtoHomePage
{
    private List<Servie> servies;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private Boolean lastPage;
}
