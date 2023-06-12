package com.shahruie.www.divar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shahruie.www.divar.app.AppConfig;
import com.shahruie.www.divar.app.AppController;
import com.shahruie.www.divar.fragments.FourFragment;
import com.shahruie.www.divar.fragments.OneFragment;
import com.shahruie.www.divar.helper.My_adv_card_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_Adv_List extends AppCompatActivity {

    private MyProgressDialog pDialog;
    private RecyclerView my_resycler;
    private LinearLayoutManager RecyclerViewLayoutManager;
    private My_adv_card_adapter recycle_adapter;
    public   static CoordinatorLayout coordinatorLayout;
    private String email0;
    private List<Adv> advList=new ArrayList<Adv>();


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String phone0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__adv__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.adv_toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("آگهی های من");
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.img_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pDialog = new MyProgressDialog(this);
        pDialog.setCancelable(false);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        my_resycler=(RecyclerView)findViewById(R.id.my_recycler);

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        my_resycler.setLayoutManager(RecyclerViewLayoutManager);
        recycle_adapter = new My_adv_card_adapter(My_Adv_List.this, advList);
        my_resycler.setAdapter(recycle_adapter);
        pref = getApplicationContext().getSharedPreferences("setting",MODE_PRIVATE);
        editor = pref.edit();

    }
    @Override
    public void onResume() {
        advList.clear();
        recycle_adapter.notifyDataSetChanged();
        String login_by=pref.getString("Login_by","");
        if(login_by.equals("phone")){
            phone0=pref.getString("phone","");
            refresh_by_phone();
        }else{
            if(login_by.equals("email")){
                email0=pref.getString("email","");
                refresh_by_email();
            }
        }
        super.onResume();
    }
//=========================================================
private void refresh_by_phone() {

    String tag_string_req = "req_phone_login";
    OneFragment.showDialog();
    StringRequest jsonArrayRequest = new StringRequest
            (Request.Method.POST, AppConfig.URL_PHONE_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(OneFragment.TAG,"phone_Login"+ response.toString());
                            OneFragment.hideDialog();
                            try {
                                JSONObject json = new JSONObject(response);
                                Log.d("code",json.getString("code"));
                                if(( json.getString("code")).equals("0")){
                                }else{
                                    JSONArray result=new JSONArray(json.getString("result"));
                                    for (int i =0 ; i < result.length();i++)
                                    {
                                        JSONObject obj = result.getJSONObject(i);
                                        Adv adv = new Adv();
                                        adv.setTitle(obj.getString("name"));
                                        adv.setDescription(obj.getString("description"));
                                        adv.setEmail(obj.getString("email"));
                                         adv.setDate(obj.getString("date"));
                                        adv.setId(obj.getString("id"));
                                        adv.setCity(obj.getString("city_id"));
                                        adv.setPhone(obj.getString("phone"));
                                        adv.setCategory(obj.getString("category_id"));
                                        adv.setAdv_img(AppConfig.IMG_base_url+obj.getString("img"));
                                        advList.add(adv);
                                        recycle_adapter.notifyDataSetChanged();

                                    }
                                }
                            }catch (JSONException e){ e.printStackTrace();
                                Log.d("er",e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e(OneFragment.TAG, "Login Error: " + error.getMessage());
                    Log.d("er2",error.getMessage());
                    OneFragment.hideDialog();
                }
            }) {

        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("phone",phone0);
            return params;
        }
    };
    AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_string_req);
}
//========================================
    private void refresh_by_email() {
        String tag_string_req = "req_email_login";
        OneFragment.showDialog();
        StringRequest jsonArrayRequest = new StringRequest
                (Request.Method.POST, AppConfig.URL_EMAIL_LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(OneFragment.TAG,"email_Login"+ response.toString());
                                OneFragment.hideDialog();
                                try {
                                    JSONObject json = new JSONObject(response);
                                    Log.d("code",json.getString("code"));
                                    if(( json.getString("code")).equals("0")){
                                    }else{
                                        JSONArray result=new JSONArray(json.getString("result"));
                                        for (int i =0 ; i < result.length();i++)
                                        {
                                            JSONObject obj = result.getJSONObject(i);
                                            Adv adv = new Adv();
                                            adv.setTitle(obj.getString("name"));
                                            adv.setDescription(obj.getString("description"));
                                            adv.setEmail(obj.getString("email"));
                                            adv.setDate(obj.getString("date"));
                                            adv.setId(obj.getString("id"));
                                            adv.setCity(obj.getString("city_id"));
                                            adv.setPhone(obj.getString("phone"));
                                            adv.setCategory(obj.getString("category_id"));
                                            adv.setAdv_img(AppConfig.IMG_base_url+obj.getString("img"));
                                            advList.add(adv);
                                            recycle_adapter.notifyDataSetChanged();

                                        }
                                    }
                                }catch (JSONException e){ e.printStackTrace();
                                    Log.d("er",e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
////mmhf
                        Log.e(OneFragment.TAG, "Login Error: " + error.getMessage());
                        Log.d("er2",error.getMessage());
                        OneFragment.hideDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email0);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_string_req);

    }
    //============================================


}
