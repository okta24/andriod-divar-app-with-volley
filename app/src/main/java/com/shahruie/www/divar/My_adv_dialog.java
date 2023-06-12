package com.shahruie.www.divar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shahruie.www.divar.app.AppConfig;
import com.shahruie.www.divar.app.AppController;
import com.shahruie.www.divar.fragments.FourFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MR on 08/15/2017.
 */
public class My_adv_dialog extends Dialog implements
        View.OnClickListener {

    private static final String TAG = New_Main2.class.getSimpleName();
    private static MyProgressDialog pDialog;
    private List<Adv> advList=new ArrayList<Adv>() ;

    public My_adv_dialog(Context con) {
        super(con,android.R.style.Theme_Translucent_NoTitleBar);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.c=con;
    }

    public Context c;
    public TextView close;
    EditText et_phone,et_email;
    Button phone,email;
    public String str="";
    TextView error_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_adv_dialog);
        close=(TextView) findViewById(R.id.close);
        phone=(Button) findViewById(R.id.phone_login);
        email=(Button) findViewById(R.id.email_login);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_email=(EditText)findViewById(R.id.et_email);
        error_txt=(TextView)findViewById(R.id.error_txt);
        pDialog=new MyProgressDialog(c);
        error_txt.setVisibility(View.GONE);
        close.setOnClickListener(this);
        email.setOnClickListener(this);
        phone.setOnClickListener(this);

    }
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.close:
                dismiss();
                break;
            case R.id.phone_login:
                str=et_phone.getText().toString();
                if(str.length()>0)
                    phone_login();
                else {
                    error_txt.setVisibility(View.VISIBLE);
                    error_txt.setText("لطفا شماره تماس را وارد نمایید");
                }
                break;
            case R.id.email_login:
                str=et_email.getText().toString();
                if(str.length()>0)
                email_login();
                else {
                    error_txt.setVisibility(View.VISIBLE);
                    error_txt.setText("لطفا ایمیل خود را وارد نمایید");
                }
                break;
            default:
                break;
        }

    }

    private void phone_login() {

        String tag_string_req = "req_phone_login";
        pDialog.setMessage("please waite");
        showDialog();
        StringRequest jsonArrayRequest = new StringRequest
                (Request.Method.POST, AppConfig.URL_PHONE_LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG,"Phone_Login"+ response.toString());
                                hideDialog();
                                error_txt.setVisibility(View.GONE);
                                try {
                                    JSONObject json = new JSONObject(response);
                                    //JSONObject json=jarray.getJSONObject(0);
                                    Log.d("code",json.getString("code"));
                                    if(( json.getString("code")).equals("0")){
                                        error_txt.setVisibility(View.VISIBLE);
                                        error_txt.setText(json.getString("result"));
                                    }else{
                                    // String error = jObj.getString("error");
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
                                        adv.setAdv_img(AppConfig.IMG_base_url+obj.getString("img"));
                                        adv.setCategory(obj.getString("category_id"));
                                       // Log.d("gfhf",obj.getString("img"));
                                        advList.add(adv);
                                        Log.d("lidt",advList.get(0).getAdv_img());
                                        Log.d("success","full adv");

                                    }
                                        SharedPreferences pref;
                                        SharedPreferences.Editor editor;
                                        pref = c.getApplicationContext().getSharedPreferences("setting", c.MODE_PRIVATE);
                                        editor = pref.edit();
                                        editor.putBoolean("Is_login",true);
                                        editor.putString("Login_by","phone");
                                        editor.putString("phone",str);
                                        editor.commit();
                                        Intent intent=new Intent(c,My_Adv_List.class);
                                        c.startActivity(intent);
                                        dismiss();
                                    }
                                }catch (JSONException e){ e.printStackTrace();
                                    Log.d("er",e.getMessage());

                                    error_txt.setText(e.getMessage());
                                    error_txt.setVisibility(View.VISIBLE);
                                     }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "Login Error: " + error.getMessage());
                        error_txt.setText(error.getMessage());
                        Log.d("er2",error.getMessage());
                        error_txt.setVisibility(View.VISIBLE);
                        hideDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone",str);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_string_req);

    }


    private void email_login() {

        String tag_string_req = "req_email_login";
        //pDialog.setMessage("please waite");
        showDialog();
        StringRequest jsonArrayRequest = new StringRequest
                (Request.Method.POST, AppConfig.URL_EMAIL_LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG,"email_Login"+ response.toString());
                                hideDialog();
                                error_txt.setVisibility(View.GONE);
                                try {
                                    JSONObject json = new JSONObject(response);
                                    Log.d("code",json.getString("code"));
                                    if(( json.getString("code")).equals("0")){
                                        error_txt.setVisibility(View.VISIBLE);
                                        error_txt.setText(json.getString("result"));
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
                                            //.d("lidt",advList.toString());
                                        }

                                        SharedPreferences pref;
                                        SharedPreferences.Editor editor;
                                        pref = c.getApplicationContext().getSharedPreferences("setting", c.MODE_PRIVATE);
                                        editor = pref.edit();
                                        editor.putBoolean("Is_login",true);
                                        editor.putString("Login_by","email");
                                        editor.putString("email",str);
                                        editor.commit();
                                            Intent intent=new Intent(c,My_Adv_List.class);
                                            c.startActivity(intent);
                                            dismiss();

                                           // Log.d("li2",FourFragment.adv_list.toString());



                                    }
                                }catch (JSONException e){ e.printStackTrace();
                                    Log.d("er",e.getMessage());

                                    error_txt.setText(e.getMessage());
                                    error_txt.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "Login Error: " + error.getMessage());
                        error_txt.setText(error.getMessage());
                        Log.d("er2",error.getMessage());
                        error_txt.setVisibility(View.VISIBLE);
                        hideDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",str);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_string_req);

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