package servie.track_servie.exception;

public class TmdbIdNotFoundException extends RuntimeException
{
    String resourceName;
    String fieldName;
    Integer fieldValue;

    public TmdbIdNotFoundException(String resourceName, String fieldName, Integer fieldValue)
    {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
