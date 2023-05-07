package servie.track_servie.config;

import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import servie.track_servie.payload.dtos.operationsHomePageDtos.ServieDtoHomePage;

// @Component
public class ServieDtoHomePageConverter // implements Converter<Map<String, Object>, ServieDtoHomePage>
{
    //     @Override
    //     public ServieDtoHomePage convert(Map<String, Object> map)
    //     {
    //         ServieDtoHomePage dto = new ServieDtoHomePage();
    //         dto.setTmdbId((Integer) map.get("tmdbId"));
    //         dto.setTitle((String) map.get("title"));
    //         dto.setPosterPath((String) map.get("posterPath"));
    //         return dto;
    //     }
}
