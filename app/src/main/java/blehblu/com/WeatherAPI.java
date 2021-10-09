package blehblu.com;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather?&appid=a46848ae4d9293b1c1a7096d73391e39&units=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat")double lat,@Query("lon")double lon);

    @GET("weather?&appid=a46848ae4d9293b1c1a7096d73391e39&units=metric")
    Call<OpenWeatherMap>getWeatherWithCity(@Query("q")String name);
}
