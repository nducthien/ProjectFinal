package project.thoitiet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import project.thoitiet.adapter.CustomAdapter;
import project.thoitiet.model.Weather;

public class Weather7Day extends AppCompatActivity {

    private TextView tvTenThanhPho;
    private ListView lvList;

    CustomAdapter customAdapter;
    ArrayList<Weather> mangthoitiet;

    String tenThanhPho = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather7_day);

        Anhxa();

        // tạo biến intent và nhận giát trị về
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua", "truyenquachua : " + city);

        if (city.equals("")) { // nếu biến city truyền qua là rỗng
            tenThanhPho = "Ha noi";
            Get7DaysData(tenThanhPho);
        } else {  // >< nếu biến tenThanhPho da ton tai thì nó sẽ bằng biến city truyền qua
            tenThanhPho = city;
            Get7DaysData(tenThanhPho);
        }
    }

    private void Anhxa() {
        tvTenThanhPho = findViewById(R.id.tvTenThanhPho);
        lvList = findViewById(R.id.lvList);

        Get7DaysData(tenThanhPho);
        mangthoitiet = new ArrayList<>();
        customAdapter = new CustomAdapter(getBaseContext(), mangthoitiet);
        lvList.setAdapter(customAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void Get7DaysData(String data) {
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + data + "&units=metric&cnt=7&appid=c45f73e836a719f5c3fd9dd292206ab1";
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject1.getJSONObject("city");

                            // tạo biến name và nhận giá trị
                            String name = jsonObjectCity.getString("name");
                            // setText cho TextView
                            tvTenThanhPho.setText(name);

                            // tiếp đến là phần List Ngày nó là [] và nó đi từ {}
                            JSONArray jsonArrayList = jsonObject1.getJSONArray("list");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String ngay = jsonObjectList.getString("dt");

                                long l = Long.valueOf(ngay);
                                // từ mini giây chuyển về giây
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat =
                                        new SimpleDateFormat("EEEE yyyy-MM-dd");
                                String Day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                String max = jsonObjectTemp.getString("max");
                                String min = jsonObjectTemp.getString("min");

                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);
                                String nhietDoMax = String.valueOf(a.intValue());
                                String nhietDoMin = String.valueOf(b.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String trangThai = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                mangthoitiet.add(new Weather(Day, trangThai, icon, nhietDoMax, nhietDoMin));
                            }

                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);

    }
}