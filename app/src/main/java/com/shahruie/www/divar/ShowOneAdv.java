package com.shahruie.www.divar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shahruie.www.divar.app.AppController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ShowOneAdv extends AppCompatActivity {
    TextView txttilte ,TxtDescription , TxtDate , TxtEmail,TxtPhone ;
    NetworkImageView ImgAdv;
    String title , description , email , phone , date , img_url;
    FloatingActionButton fav;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private String id;

    long dv;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_image);
        defineViews();
    }

    private void defineViews() {

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        date = intent.getStringExtra("date");
        img_url = intent.getStringExtra("adv_img_url");
        id = intent.getStringExtra("id");
        //Date convertedDate = formatter.parse("2016-04-13 22:00:01.0");
      //  System.out.print(convertedDate);
        String givenDateString = date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ///String formatedDate = formatter.format(givenDateString);
        try {
            Date convertedDate = (Date) formatter.parse(givenDateString.toString());
             dv = convertedDate.getTime();
            System.out.println("Date in milli :: " + dv);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        int date2=Integer.parseInt(date);
        // dv = Long.valueOf(date)*1000;// its need to be in milisecond
        Date df = new java.util.Date(dv);
        String vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);

        fav=(FloatingActionButton)findViewById(R.id.fav);
        txttilte = (TextView)findViewById(R.id.txt_title);
        TxtDescription= (TextView)findViewById(R.id.txt_description);
        TxtDate = (TextView)findViewById(R.id.txt_date);
        TxtEmail = (TextView)findViewById(R.id.txt_email);
        TxtPhone = (TextView)findViewById(R.id.txt_phone);
        ImgAdv = (NetworkImageView)findViewById(R.id.adv_img);


        txttilte.setText(title);
        TxtDescription.setText(description);
        TxtDate.setText(getTimeAgo(dv,ShowOneAdv.this));
        System.out.println("Date in milli :: " + TxtDate.getText().toString());

        TxtEmail.setText(email);
        TxtPhone.setText(phone);
        ImgAdv.setImageUrl(img_url,imageLoader);
        set_fab_icon();

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_fav();
            }
        });


    }





        public static String getTimeAgo(long time, Context ctx) {
            /*if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000;
            }*/
            long now = System.currentTimeMillis();
            System.out.println("Date now milli :: " +now);
            System.out.println("Date time milli :: " +time);

            if (time > now || time <= 0) {
                return null;
            }

            // TODO: localize
            final long diff = now - time;
            System.out.println("Date in diff :: " + diff);
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }

    }

    private void set_fab_icon() {
        SharedPreferences pref;
        pref = getApplicationContext().getSharedPreferences("favorite", MODE_PRIVATE);
        Boolean favorite=pref.getBoolean("fav_"+id,false);
        if(favorite){
            fav.setImageResource(R.drawable.ic_fav2);
        }
        else{
            fav.setImageResource(R.drawable.ic_fav);
        }
    }

    private void set_fav() {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = getApplicationContext().getSharedPreferences("favorite", MODE_PRIVATE);
        editor = pref.edit();
        Boolean favorite=pref.getBoolean("fav_"+id,false);
        if(favorite){
            editor.putBoolean("fav_"+id,false);
            fav.setImageResource(R.drawable.ic_fav);
            Toast.makeText(ShowOneAdv.this,"اگهی از لیست علاقه مندیها حذف شد!",Toast.LENGTH_LONG).show();
        }
        else{
            editor.putBoolean("fav_"+id,true);
            fav.setImageResource(R.drawable.ic_fav2);
            Toast.makeText(ShowOneAdv.this,"آگهی به لیست علاقه مندیها اضافه شد.",Toast.LENGTH_LONG).show();
        }
        editor.apply();
    }
}
