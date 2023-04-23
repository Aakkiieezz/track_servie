package servie.track_servie.exceptions;

public class ImdbIdNotFoundException extends RuntimeException
{
    String resourceName;
    String fieldName;
    String fieldValue;

    public ImdbIdNotFoundException(String resourceName, String fieldName, String fieldValue)
    {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
