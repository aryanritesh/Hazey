package blehblu.com;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView city,temp,weatherCondition,humidity,maxTemp,minTemp,pressure,wind,
            humidityText,maxTempText,minTempText,pressureText,windText,details;
    private ImageView imageView;
    private FloatingActionButton fab;
    LocationManager locationManager;
    LocationListener locationListener;
    double lat, lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=findViewById(R.id.city);
        temp=findViewById(R.id.temp);
        weatherCondition=findViewById(R.id.weatherCondition);
        humidity=findViewById(R.id.humidityT);
        maxTemp=findViewById(R.id.maxTempT);
        minTemp=findViewById(R.id.minTempT);
        pressure=findViewById(R.id.pressureT);
        wind=findViewById(R.id.windT);
        imageView=findViewById(R.id.imageView);
        humidityText=findViewById(R.id.humidity);
        maxTempText=findViewById(R.id.maxTemp);
        minTempText=findViewById(R.id.minTemp);
        pressureText=findViewById(R.id.pressure);
        windText=findViewById(R.id.wind);
        details=findViewById(R.id.details1);
        fab=findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,WeatherAct.class);
                startActivity(intent);
            }
        });
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
              lat=location.getLatitude();
              lon=location.getLongitude();
              Log.e("lat :",String.valueOf(lat));
              Log.e("lon :",String.valueOf(lon));
              getWeather(lat,lon);
            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
           != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1 && permissions.length>0 && ContextCompat.checkSelfPermission(this,
          Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,50,locationListener);
        }
    }
    public void getWeather(double lat,double lon){
        WeatherAPI weatherAPI= RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call=weatherAPI.getWeatherWithLocation(lat,lon);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                imageView.setVisibility(View.VISIBLE);
                details.setVisibility(View.VISIBLE);
                humidityText.setVisibility(View.VISIBLE);
                maxTempText.setVisibility(View.VISIBLE);
                minTempText.setVisibility(View.VISIBLE);
                pressureText.setVisibility(View.VISIBLE);
                windText.setVisibility(View.VISIBLE);
                city.setText(response.body().getName()+", "+response.body().getSys().getCountry());
                temp.setText(response.body().getMain().getTemp()+"°C");
                weatherCondition.setText(response.body().getWeather().get(0).getDescription());
                humidity.setText(""+response.body().getMain().getHumidity()+"%");
                maxTemp.setText(""+response.body().getMain().getTempMax()+"°C");
                minTemp.setText(""+response.body().getMain().getTempMin()+"°C");
                pressure.setText(""+response.body().getMain().getPressure());
                wind.setText(""+response.body().getWind().getSpeed());

                String iconCode=response.body().getWeather().get(0).getIcon();
                Picasso.get().load("https://openweathermap.org/img/wn/"+iconCode+"@2x.png")
                        .placeholder(R.drawable.ic_launcher_foreground).into(imageView);


            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });

    }
}