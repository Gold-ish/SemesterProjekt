package dto;

/**
 *
 * @author rando
 */
public class OmdbApiDTO {

    private final String Title;
    private final String Released;
    private final String Runtime;

    public OmdbApiDTO(String Title, String Released, String Runtime) {
        this.Title = Title;
        this.Released = Released;
        this.Runtime = Runtime;
    }

    public String getTitle() {
        return Title;
    }

    public String getReleased() {
        return Released;
    }

    public String getRuntime() {
        return Runtime;
    }

    @Override
    public String toString() {
        return "OmdbApiDTO{" + "Title=" + Title + ", Teleased=" + Released + ", Tuntime=" + Runtime + '}';
    }

}
