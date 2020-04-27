package dto;

/**
 *
 * @author rando
 */
public class ChuckDTO {

    private final String id;
    private final String value;
    private final String url;

    public ChuckDTO(String id, String value, String url) {
        this.id = id;
        this.value = value;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "ICNDbDTO{" + "id=" + id + ", value=" + value + ", url=" + url + '}';
    }

}
