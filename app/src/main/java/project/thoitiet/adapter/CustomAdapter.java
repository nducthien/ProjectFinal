package project.thoitiet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.thoitiet.R;
import project.thoitiet.model.Weather;

/**
 * Created by Lenovo on 04-Dec-17.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Weather> arrayList;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.customlist, null);
        // gọi class Weather + tạo biến weather + lấy giá trị arrList.get
        Weather weather = arrayList.get(i);

        TextView tvNgay = (TextView) convertView.findViewById(R.id.tvNgay);
        TextView tvTrangThai = (TextView) convertView.findViewById(R.id.tvTrangThai);
        TextView tvMax = (TextView) convertView.findViewById(R.id.tvMaxTemp);
        TextView tvMin = (TextView) convertView.findViewById(R.id.tvMinTemp);
        ImageView imgTrangThai = (ImageView) convertView.findViewById(R.id.imgTrangThai);

        // sét cho các TextView các giá trị
        tvNgay.setText(weather.Day);
        tvTrangThai.setText(weather.Status);
        tvMax.setText(weather.MaxTemp + "°C");
        tvMin.setText(weather.MinTemp + "°C");

        Picasso.with(context).load("http://openweathermap.org/img/w/" + weather.Image + ".png").into(imgTrangThai);

        return convertView;
    }
}
