package servie.track_servie.exception;

public class GenreNotFoundException extends RuntimeException
{
    String resourceName;
    String fieldName;
    long fieldValue;

    public GenreNotFoundException(String resourceName, String fieldName, long fieldValue)
    {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
