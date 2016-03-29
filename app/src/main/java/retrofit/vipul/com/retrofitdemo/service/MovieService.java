package retrofit.vipul.com.retrofitdemo.service;

import retrofit.vipul.com.retrofitdemo.model.MovieObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MovieService {
    @GET("trending")
    @Headers({
            "trakt-api-version: 2",
            "trakt-api-key: a0c9e1842e5b4bd2fa07bd814a13659b5e6561b9d556144a6ddb73c838844a6b"
    })
    Call<MovieObject[]> getMovies(@Query("page") int page, @Query("limit") int limit,
                                  @Query("extended") String extended);
}
