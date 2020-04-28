package dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rando
 */
public class MovieListDTO {

    private List<MovieDTO> movieDTOs;

    public MovieListDTO(List<MovieDTO> m) {
        List<MovieDTO> movieList = new ArrayList();
        m.forEach((movieDTO) -> {
            movieList.add(new MovieDTO(movieDTO));
        });
    }

    public List<MovieDTO> getMovieDTOs() {
        return movieDTOs;
    }

}
