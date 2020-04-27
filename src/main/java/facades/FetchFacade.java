package facades;

import com.google.gson.Gson;
import dto.CompleteDTO;
import dto.RandomCatDTO;
import dto.RandomDogDTO;
import fetcher.ChuckFetcher;
import fetcher.FetcherInterface;
import fetcher.KanyeRestFetcher;
import fetcher.OmdbFetcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import utils.HttpUtils;

/**
 *
 * @author rando
 */
public class FetchFacade {

    private static FetchFacade instance;
    private Gson gson = new Gson();

    //Private Constructor to ensure Singleton
    private FetchFacade() {
    }

    public static FetchFacade getFetchFacade() {
        if (instance == null) {
            instance = new FetchFacade();
        }
        return instance;
    }
    
    public CompleteDTO runParalel() throws InterruptedException {
        OmdbFetcher omdbFetcher = new OmdbFetcher("http://www.omdbapi.com/?t=Game%20Of%20Thrones&Season1&apikey=6b10a5de");
        ChuckFetcher chuckFetcher = new ChuckFetcher("https://api.chucknorris.io/jokes/random");
        KanyeRestFetcher kanyerestFetcher = new KanyeRestFetcher("https://api.kanye.rest/");
        List<FetcherInterface> urls = new ArrayList();
        urls.add(omdbFetcher);
        urls.add(chuckFetcher);
        urls.add(kanyerestFetcher);
        ExecutorService workingJack = Executors.newFixedThreadPool(3);
        for (FetcherInterface fetch : urls) {
            Runnable task = () -> {
                try {
                    fetch.doWork();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            workingJack.submit(task);
        }
        workingJack.shutdown();
        workingJack.awaitTermination(15, TimeUnit.SECONDS);
        return new CompleteDTO(omdbFetcher.getOmdbApiDTO(), chuckFetcher.getChuckDTO(), kanyerestFetcher.getKanyeRestDto());
    }
    
    public RandomCatDTO getCatPic() throws IOException {
        String catAPI = HttpUtils.fetchData("https://aws.random.cat/meow");
        RandomCatDTO randomCatDTO = gson.fromJson(catAPI, RandomCatDTO.class);
        return randomCatDTO;
    }
    
    public RandomDogDTO getDogPic() throws IOException {
        String dogAPI = HttpUtils.fetchData("https://random.dog/woof.json");
        RandomDogDTO randomDogDTO = gson.fromJson(dogAPI, RandomDogDTO.class);
        return randomDogDTO;
    }
    
}
