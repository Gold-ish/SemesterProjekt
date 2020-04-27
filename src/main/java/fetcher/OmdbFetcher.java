package fetcher;

import com.google.gson.Gson;
import dto.OmdbApiDTO;
import java.io.IOException;
import utils.HttpUtils;

/**
 *
 * @author rando
 */
public class OmdbFetcher implements FetcherInterface{
    private String url;
    private OmdbApiDTO omdbApiDTO;
            
    public OmdbFetcher(String url) {
        this.url = url;
    }
    
    @Override
    public void doWork() throws IOException {
        Gson gson = new Gson();
        String omdbAPI = HttpUtils.fetchData(url);
        omdbApiDTO = gson.fromJson(omdbAPI, OmdbApiDTO.class);
        //System.out.println(omdbApiDTO);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OmdbApiDTO getOmdbApiDTO() {
        return omdbApiDTO;
    }

    public void setOmdbApiDTO(OmdbApiDTO omdbApiDTO) {
        this.omdbApiDTO = omdbApiDTO;
    }
    
}
