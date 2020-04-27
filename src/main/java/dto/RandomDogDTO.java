package dto;

/**
 *
 * @author rando
 */
public class RandomDogDTO {

    private final String url;

    public RandomDogDTO(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "RandomDogDTO{" + "url=" + url + '}';
    }

}
