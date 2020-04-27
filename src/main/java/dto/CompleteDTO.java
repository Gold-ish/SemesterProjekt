package dto;

/**
 *
 * @author rando
 */
public class CompleteDTO {

    private final String movieReleased;
    private final String movieRuntime;
    private final String movieTitle;
    private final String jokeId;
    private final String jokeValue;
    private final String kanyeRest;

    public CompleteDTO(OmdbApiDTO omdb, ChuckDTO cdto, KanyeRestDTO kanye) {
        this.movieReleased = omdb.getReleased();
        this.movieRuntime = omdb.getRuntime();
        this.movieTitle = omdb.getTitle();
        this.jokeId = cdto.getId();
        this.jokeValue = cdto.getValue();
        this.kanyeRest = kanye.getQuote();
    }

    public String getMovieReleased() {
        return movieReleased;
    }

    public String getMovieRuntime() {
        return movieRuntime;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getJokeId() {
        return jokeId;
    }

    public String getJokeValue() {
        return jokeValue;
    }

    public String getKanyeRest() {
        return kanyeRest;
    }

    @Override
    public String toString() {
        return "CompleteDTO{" + "movieReleased=" + movieReleased + ", movieRuntime=" + movieRuntime + ", movieTitle=" + movieTitle + ", jokeId=" + jokeId + ", jokeValue=" + jokeValue + ", kanyeRest=" + kanyeRest + '}';
    }

}
