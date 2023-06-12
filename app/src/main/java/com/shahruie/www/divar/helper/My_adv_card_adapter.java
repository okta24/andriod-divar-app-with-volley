package com.shahruie.www.divar.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.shahruie.www.divar.Add;
import com.shahruie.www.divar.Adv;
import com.shahruie.www.divar.DBHandler;
import com.shahruie.www.divar.Deldialog;
import com.shahruie.www.divar.MyProgressDialog;
import com.shahruie.www.divar.My_Adv_List;
import com.shahruie.www.divar.R;
import com.shahruie.www.divar.app.AppConfig;
import com.shahruie.www.divar.app.AppController;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MR on 08/17/2017.
 */
public class My_adv_card_adapter extends RecyclerView.Adapter<My_adv_card_adapter.ViewHolder>  implements View.OnClickListener{
    private static MyProgressDialog pDialog;
    List<Adv> alladv;
    private final Context context;
    View view1;
    DBHandler db;
    String img_name;
    private String id;

    public My_adv_card_adapter(Context context, List<Adv> alladv) {
        db = new DBHandler(context);
        this.alladv = alladv;
        this.context=context;
        pDialog=new MyProgressDialog(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        view1  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_adv_card, viewGroup, false);

        return new ViewHolder(view1);
    }
    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int i) {
        Uri uri=Uri.parse(alladv.get(i).getAdv_img());
        Context context=Viewholder.adv_img.getContext();
        Picasso.with(context).load(uri).into(Viewholder.adv_img);
        Viewholder.txt_title.setText(alladv.get(i).getTitle());
        Viewholder.txt_phone.setText(alladv.get(i).getPhone());
        Viewholder.txt_description.setText(alladv.get(i).getDescription());
        Viewholder.txt_email.setText(alladv.get(i).getEmail());
        Viewholder.txt_cat.setText(alladv.get(i).getCategory());
        Viewholder.txt_city.setText(alladv.get(i).getCity());
        img_name=alladv.get(i).getAdv_img();
        id=alladv.get(i).getId();
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
        TextView txt_title, txt_cat,txt_city,txt_phone, txt_description,txt_date,txt_email,txt_id,txt_img,txt_price;
        ImageView adv_img;
        Button btn_edit,btn_del;
        public ViewHolder(View view) {
            super(view);
            context = view.getContext();

            adv_img = (ImageView) view.findViewById(R.id.adv_img5);
            txt_description = (TextView) view.findViewById(R.id.txt_description);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_phone = (TextView) view.findViewById(R.id.txt_phone);
            txt_email = (TextView) view.findViewById(R.id.txt_email);
            txt_cat = (TextView) view.findViewById(R.id.txt_cat);
            txt_city = (TextView) view.findViewById(R.id.txt_city);
            btn_del=(Button)view.findViewById(R.id.btn_del_adv);
            btn_edit=(Button)view.findViewById(R.id.btn_edit);

            //======================================
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,Add.class);
                    intent.putExtra("mode",0);
                    intent.putExtra("title",txt_title.getText().toString());
                    intent.putExtra("phone",txt_phone.getText().toString());
                    intent.putExtra("des",txt_description.getText().toString());
                    intent.putExtra("email",txt_email.getText().toString());
                    intent.putExtra("cat",txt_cat.getText().toString());
                    intent.putExtra("city",txt_city.getText().toString());
                    intent.putExtra("img",img_name);
                    intent.putExtra("id",id);
                    intent.putExtra("adv_list_id",getAdapterPosition());
                    context.startActivity(intent);
                }
            });

            //======================================================
            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Deldialog del_dialog=new Deldialog(context,id);
                    del_dialog.show();
                    del_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            notifyDataSetChanged ();
                        }
                    });*/
                    final Dialog del=new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
                    del.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    del.getWindow().getAttributes().windowAnimations=R.style.PauseDialogAnimation2;
                    del.setContentView(R.layout.deldialog);
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int h = displaymetrics.heightPixels;
                    int  w = displaymetrics.widthPixels;
                    Window window = del.getWindow();
                    window.setLayout(w-100, LinearLayout.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);
                    TextView btndel=(TextView)del.findViewById(R.id.btndel);
                    btndel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            del_adv();
                            del.dismiss();
                            alladv.remove(getAdapterPosition());
                            notifyDataSetChanged ();
                            if(alladv.isEmpty()) {}
                                //zeroitem.setVisibility(View.VISIBLE);

                        }

                    });

                    del.show();
                }

            });
            //=====================================
        }


    }
    private void del_adv() {

        String tag_string_req = "req_delete";
        pDialog=new MyProgressDialog(context);
        //pDialog.setMessage("please waite");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_DEL_ADV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Snackbar snackbar=Snackbar.make (My_Adv_List.coordinatorLayout,
                               response, Snackbar.LENGTH_LONG);
                        snackbar.show ();
                      //  Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }
    //=============================================================
    private static void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    //=====================================================================
    private static void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    //===========================================

}
