package servie.track_servie.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import servie.track_servie.entity.tmdbExports.TmdbMovieExports;
import java.io.IOException;

public class TmdbMovieExportsDeserializer extends JsonDeserializer<TmdbMovieExports>
{
    @Override
    public TmdbMovieExports deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
    {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Boolean adult = node.get("adult").asBoolean();
        Integer id = node.get("id").asInt();
        String original_title = node.get("original_title").asText();
        Double popularity = node.get("popularity").asDouble();
        Boolean video = node.get("video").asBoolean();
        return new TmdbMovieExports(adult, id, original_title, popularity, video);
    }
}
