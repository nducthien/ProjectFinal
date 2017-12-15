package project.thoitiet;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int PICK_CITY = 1;

    TextView tvCity;
    TextView tvCountry;
    ImageView imgIcon;
    TextView tvTemp;
    TextView tvStatus;
    TextView tvHummidity;
    TextView tvCloud;
    TextView tvWind;
    TextView tvDay;

    EditText edtSearch;
    Button btnSearch;
    Button btnNext;

    String City = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Anhxa();
        GetCurrentWetherData("Ha noi");
    }

    private void Anhxa() {
        tvCity = findViewById(R.id.tvCity);
        tvCountry = findViewById(R.id.tvCountry);
        imgIcon = findViewById(R.id.imgIcon);
        tvTemp = findViewById(R.id.tvTemp);
        tvStatus = findViewById(R.id.tvStatus);
        tvHummidity = findViewById(R.id.tvHummidity);
        tvCloud = findViewById(R.id.tvCloud);
        tvWind = findViewById(R.id.tvWind);
        tvDay = findViewById(R.id.tvDay);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnNext = findViewById(R.id.btnNext);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                if (city.equals("")) {
                    City = "Ha noi";
                    GetCurrentWetherData(City);
                } else {
                    City = city;
                    GetCurrentWetherData(City);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(getBaseContext(), Weather7Day.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }

    private void GetCurrentWetherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=c45f73e836a719f5c3fd9dd292206ab1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // vì nó là Json Object
                        // giá trị của Json object nó được đổ trong respone nên truyền vào respon
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //tạo biến day lấy ngày tháng về
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name"); // tạo và gán value cho biến
                            tvCity.setText("Thành phố: " + name);

                            long l = Long.valueOf(day);
                            // từ mini giây chuyển về giây
                            Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat =
                                    new SimpleDateFormat("EEEE yyyy-MM-dd");
                            String Day = simpleDateFormat.format(date);

                            tvDay.setText(Day);
                            JSONArray jsonArrayWather = jsonObject.getJSONArray("weather");
                            // jsonOjWather lấy từ jsonArrWather và đi vào từ JsonObject
                            // nó chỉ có 1 JsonObject và kiểu truyền vào là int ta sẽ truyền vào phần từ đầu tiên
                            JSONObject jsonObjectWather = jsonArrayWather.getJSONObject(0);
                            String status = jsonObjectWather.getString("main");
                            String icon = jsonObjectWather.getString("icon");

                            Picasso.with(getBaseContext()).load("http://openweathermap.org/img/w/" + icon + ".png").into(imgIcon);
                            tvStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(nhietdo);
                            String nhietDo = String.valueOf(a.intValue());

                            tvTemp.setText(nhietDo + "°C");
                            tvHummidity.setText("Humidity: " + doam + "%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            tvWind.setText("Wind: " + gio + "m/s");

                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectCloud.getString("all");
                            tvCloud.setText("Cloudiness: " + may + "%");

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            tvCountry.setText("Quốc gia: " + country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ketqua", "sairoia");
                    }
                });
        // de no thuc hien request
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(intent, PICK_CITY);
            return true;
        }

        if (id == R.id.help) {
            AlertDialog.Builder aler = new AlertDialog.Builder(this);
            aler.setTitle("Thông báo!");
            aler.setIcon(R.drawable.adf);
            aler.setMessage("Can I Help You ?");

            aler.setPositiveButton("Hihi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getBaseContext(), "You are funny", Toast.LENGTH_LONG).show();

                }
            });
            aler.setNegativeButton("Relax", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Toast.makeText(getBaseContext(), "Oh ! Im sory", Toast.LENGTH_LONG).show();
                }
            });
            aler.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_like) {
            Intent myWebLink = new Intent();
            myWebLink.setData(Uri.parse("https://github.com/nducthien/ProjectFinal"));
            startActivity(myWebLink);
        } else if (id == R.id.nav_help) {
            Intent myWebLink1 = new Intent();
            myWebLink1.setData(Uri
                    .parse("https://play.google.com/store/apps/details?id=com.bvl.weatherapp&hl=vi"));
            startActivity(myWebLink1);

        } else if (id == R.id.nav_settings) {
            Intent setting = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(setting);

        } else if (id == R.id.nav_about) {
            Intent about = new Intent(getBaseContext(), AboutActivity.class);
            startActivity(about);

        } else if (id == R.id.nav_share) {
            Intent shareInten = new Intent();
            shareInten.setAction(Intent.ACTION_SEND);
            shareInten.putExtra(Intent.EXTRA_TEXT, "Hey, download this app!");
            shareInten.setType("text/plain");
            startActivity(shareInten);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
