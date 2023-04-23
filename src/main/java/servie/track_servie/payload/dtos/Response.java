package servie.track_servie.payload.dtos;

import java.util.Map;
import lombok.Data;

@Data
public class Response
{
    private String message;
    private Map<String, Object> data;

    public Response()
    {}

    public Response(String message)
    {
        this.message = message;
    }

    public Response(String message, Map<String, Object> data)
    {
        this.message = message;
        this.data = data;
    }
}