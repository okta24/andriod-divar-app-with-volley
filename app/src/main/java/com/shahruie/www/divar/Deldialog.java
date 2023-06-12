package com.shahruie.www.divar;

/**
 * Created by MR on 06/02/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import java.util.HashMap;
import java.util.Map;

public class Deldialog extends Dialog implements
        View.OnClickListener {

    private static MyProgressDialog pDialog;
    private String msg;

    public Deldialog(Context con, String id) {
        super(con,android.R.style.Theme_Translucent_NoTitleBar);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.c=con;
        this.id=id;

    }

    private static final String TAG = New_Main2.class.getSimpleName();
    public String id;
    public Context c;
    public TextView close,del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.deldialog);
        close=(TextView) findViewById(R.id.btnclose);
        del=(TextView) findViewById(R.id.btndel);
        close.setOnClickListener(this);
        del.setOnClickListener(this);
    }
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btnclose:
                dismiss();
                break;
            case R.id.btndel:
                del_adv();
                break;
            default:
                break;
        }

    }

    private void del_adv() {

            String tag_string_req = "req_delete";
        pDialog=new MyProgressDialog(c);
        //pDialog.setMessage("please waite");
            showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_DEL_ADV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(c, response, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c, error.toString(), Toast.LENGTH_SHORT).show();
                        dismiss();
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