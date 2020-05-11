/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fetch;

import dto.MovieDTO;
import errorhandling.MovieNotFoundException;
import facades.FetchFacade;
import java.io.IOException;

/**
 *
 * @author allan
 */
public class MovieFetchCall {
    
    private String url;
    private MovieDTO mdto;
    private boolean isCalled = false;

    public MovieFetchCall(String url) {
        this.url = url;
    }

    /*
  Connect to the URL and count the number of h1, h2, div and body Tags
     */
    public void doWork() throws MovieNotFoundException, IOException {
        if (isCalled) {
            return; //Tag values allready set
        }
        isCalled = true;
        mdto = FetchFacade.getFetchFacade().getMovieByIdSimple(url);
    }

    public String getUrl() {
        return url;
    }

    public MovieDTO getMdto() {
        return mdto;
    }
}
