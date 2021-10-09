package blehblu.com;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherAct extends AppCompatActivity {
    private TextView cityW,tempW,weatherConditionW,humidityWeather,maxTempWeather,minTempWeather,pressureWeather,windWeather;
    private ImageView imageViewWeather;
    private Button search;
    private EditText location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityW=findViewById(R.id.cityW);
        tempW=findViewById(R.id.tempW);
        weatherConditionW=findViewById(R.id.weatherConditionW);
        humidityWeather=findViewById(R.id.humidityWeather);
        maxTempWeather=findViewById(R.id.maxTempWeather);
        minTempWeather=findViewById(R.id.minTempWeather);
        pressureWeather=findViewById(R.id.pressureWeather);
        windWeather=findViewById(R.id.windWeather);
        imageViewWeather=findViewById(R.id.imageViewWeather);
        search=findViewById(R.id.search);
        location=findViewById(R.id.citySearch);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName=location.getText().toString();
            getWeather(cityName);
            location.setText("");
            }
        });
    }
    public void getWeather(String name){
        WeatherAPI weatherAPI= RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call=weatherAPI.getWeatherWithCity(name);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                if(response.isSuccessful()) {
                    cityW.setText(""+response.body().getName() + ", " + response.body().getSys().getCountry());
                    tempW.setText(""+response.body().getMain().getTemp() + "°C");
                    weatherConditionW.setText(""+response.body().getWeather().get(0).getDescription());
                    humidityWeather.setText(""+response.body().getMain().getHumidity()+"%");
                    maxTempWeather.setText(""+response.body().getMain().getTempMax() + "°C");
                    minTempWeather.setText(""+response.body().getMain().getTempMin() + "°C");
                    pressureWeather.setText(""+response.body().getMain().getPressure());
                    windWeather.setText(""+ response.body().getWind().getSpeed());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                            .placeholder(R.drawable.ic_launcher_background).into(imageViewWeather);
                }
                else
                {
                    Toast.makeText(WeatherAct.this,"City not found",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });

    }
}