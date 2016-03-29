package retrofit.vipul.com.retrofitdemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.vipul.com.retrofitdemo.R;
import retrofit.vipul.com.retrofitdemo.model.MovieObject;
import retrofit.vipul.com.retrofitdemo.service.MovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// http://square.github.io/retrofit/

public class MainActivity extends AppCompatActivity {

    private static final int CONNECTION_TIMEOUT = 600;
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://api-v2launch.trakt.tv/movies/")
                .client(getOkHttpClient(this))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = restAdapter.create(MovieService.class);

        Call<MovieObject[]> call = movieService.getMovies(1, 50, "full,images");

        call.enqueue(new Callback<MovieObject[]>() {
            @Override
            public void onResponse(Call<MovieObject[]> call, Response<MovieObject[]> response) {
                if (response.isSuccessful()) {
                    MovieObject[] movieObjects = response.body();
                    Log.i("vipul", String.valueOf(movieObjects.length));
                }
            }

            @Override
            public void onFailure(Call<MovieObject[]> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    // no connection
                }
            }
        });
    }

    private OkHttpClient getOkHttpClient(Context context) {
        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okClientBuilder.addInterceptor(httpLoggingInterceptor);
        okClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

        final File baseDir = context.getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            okClientBuilder.cache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
        }

        return okClientBuilder.build();
    }
}
