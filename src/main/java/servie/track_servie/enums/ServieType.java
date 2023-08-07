package servie.track_servie.enums;

public enum ServieType
{
    MOVIE("movie"),
    SERIES("tv");

    private final String value;

    ServieType(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }
}
