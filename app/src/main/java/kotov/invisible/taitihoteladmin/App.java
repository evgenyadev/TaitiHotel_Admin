package kotov.invisible.taitihoteladmin;

import android.app.Application;

import java.io.IOException;

import kotov.invisible.taitihoteladmin.MyUtils.PhoneInfo;
import kotov.invisible.taitihoteladmin.interfaces.TaitiApi;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    public static Retrofit retrofit;
    private static TaitiApi taitiApi;

    public static TaitiApi getApi() {
        return taitiApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String url = getResources().getString(kotov.invisible.taitihoteladmin.R.string.server_address);
        final String token = PhoneInfo.getUniqueId();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Token", token)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        taitiApi = retrofit.create(TaitiApi.class);
    }
}
