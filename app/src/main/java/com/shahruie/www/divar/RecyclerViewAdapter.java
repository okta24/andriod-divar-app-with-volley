package com.shahruie.www.divar;

/**
 * Created by MR on 02/21/2017.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shahruie.www.divar.app.AppController;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  implements View.OnClickListener{
    List<Adv> alladv;
    private final Context context;
    View view1;
    DBHandler db;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    TextView zeroitem;
    public RecyclerViewAdapter(Context context, List<Adv> alladv, TextView zeroitem) {
        db = new DBHandler(context);
        this.alladv = alladv;
        this.context=context;
        this.zeroitem=zeroitem;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        view1  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);

        return new ViewHolder(view1);
    }
    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int i) {
        Uri uri=Uri.parse(alladv.get(i).getAdv_img());
        Context context=Viewholder.adv_img.getContext();
        Picasso.with(context).load(uri).into(Viewholder.adv_img);
       // Viewholder.adv_img.setImageUrl(alladv.get(i).getAdv_img() , imageLoader);
        Viewholder.txt_title.setText(alladv.get(i).getTitle());
        Viewholder.txt_date.setText(alladv.get(i).getDate());
        Viewholder.txt_price.setText("200,000 تومان");

        }

    @Override
    public int getItemCount() {

        return alladv.size();
    }
    @Override
    public void onClick(View v) {
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        TextView txt_title, txt_phone, txt_description,txt_date,txt_email,txt_id,txt_img,txt_price;
        ImageView adv_img;
        public ViewHolder(View view) {
            super(view);
            context = view.getContext();

            adv_img = (ImageView) view.findViewById(R.id.adv_img);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_price = (TextView) view.findViewById(R.id.txt_price);
            /*if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            adv_img.setScaleType(NetworkImageView.ScaleType.FIT_XY);*/
                    }


    }
    }