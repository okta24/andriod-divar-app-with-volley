package com.shahruie.www.divar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shahruie.www.divar.app.AppConfig;
import com.shahruie.www.divar.app.AppController;
import com.shahruie.www.divar.helper.DatabaseAccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//
public class Adv_by_sub_cat extends AppCompatActivity {

    private static final String TAG = New_Main2.class.getSimpleName();

    public static CoordinatorLayout coordinatorLayout;

    private MyProgressDialog pDialog;
    public  static int  city_id=0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    List<Adv> alladv= new ArrayList<Adv>();
    public static List<Adv> advList= new ArrayList<Adv>();
    ArrayAdapter<String> adapter;

     int p_cat_id;
     TextView zeroitem;

    RecyclerView sub_resycler;
    RecyclerView.Adapter recycle_adapter;
    private LinearLayoutManager RecyclerViewLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_by_sub_cat);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.adv_toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.img_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        zeroitem= (TextView) findViewById(R.id.zeroitem);

        pref = getApplicationContext().getSharedPreferences("setting", MODE_PRIVATE);
        editor = pref.edit();

        pDialog = new MyProgressDialog(this);
        pDialog.setCancelable(false);

        final ListView sub_cat_list=(ListView)findViewById(R.id.f_sub_cat_list);

         sub_resycler=(RecyclerView)findViewById(R.id.sub_recycler);

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        sub_resycler.setLayoutManager(RecyclerViewLayoutManager);

        recycle_adapter = new RecyclerViewAdapter(Adv_by_sub_cat.this,advList,zeroitem);
        sub_resycler.setAdapter(recycle_adapter);

      //  sub_resycler.setVisibility(View.GONE);

        sub_resycler.addOnItemTouchListener(
                new RecyclerItemClickListener(Adv_by_sub_cat.this, sub_resycler ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                        String title = advList.get(position).getTitle();
                        String description = advList.get(position).getDescription();
                        String email = advList.get(position).getEmail();
                        String phone = advList.get(position).getPhone();
                        String adv_img_url = advList.get(position).getAdv_img();
                        String date = advList.get(position).getDate();
                        String id = advList.get(position).getId();


                        Intent myIntent = new Intent(Adv_by_sub_cat.this, ShowOneAdv.class);
                        myIntent.putExtra("title", title);
                        myIntent.putExtra("description", description);
                        myIntent.putExtra("phone",phone);
                        myIntent.putExtra("adv_img_url", adv_img_url);
                        myIntent.putExtra("date", date);
                        myIntent.putExtra("email", email);
                        myIntent.putExtra("id", id);

                        Adv_by_sub_cat.this.startActivity(myIntent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        Intent intent=getIntent();
        p_cat_id=intent.getIntExtra("p_cat_id",1);
        toolbar_title.setText(intent.getStringExtra("title"));
        Log.d("p_id ",p_cat_id+"");

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Adv_by_sub_cat.this);
        databaseAccess.open();
        List<String> sub_cat = databaseAccess.get_sub_category_name(p_cat_id);
        databaseAccess.close();
        ArrayAdapter<String> s_adapter = new ArrayAdapter<String>(Adv_by_sub_cat.this,R.layout.category_list_row,R.id.cat_title, sub_cat);
        sub_cat_list.setAdapter(s_adapter);

        sub_cat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        city_id=pref.getInt("city_id",0);
        Log.d("city",city_id+"");
        if(city_id!=0){
            get_All_Adv(city_id+"",p_cat_id+"");
        }
        else{
            sub_resycler.setVisibility(View.GONE);
            Toast.makeText(Adv_by_sub_cat.this,"select city",Toast.LENGTH_LONG).show();
        }
    }
//=========================================================

//=========================================================


    private void get_All_Adv(final String city_id, final String p_cat_id) {

        if(advList!=null){
            advList.clear();
            recycle_adapter.notifyDataSetChanged();}

        sub_resycler.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String tag_string_req = "req_showall";
        pDialog.setMessage("please waite");
        showDialog();
        StringRequest jsonArrayRequest = new StringRequest
                (Request.Method.POST, AppConfig.URL_SHOW_BY_P_CAT_ID,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG,"dgfdfgd"+ response.toString());
                                hideDialog();
                                try {
                                    JSONArray jarray = new JSONArray(response);
                                    for (int i =0 ; i < jarray.length()-1;i++)
                                    {
                                        JSONObject obj = jarray.getJSONObject(i);
                                        Adv adv = new Adv();
                                        adv.setTitle(obj.getString("name"));
                                        adv.setDescription(obj.getString("description"));
                                        adv.setEmail(obj.getString("email"));
                                        adv.setDate(obj.getString("date"));
                                        adv.setId(obj.getString("id"));
                                        adv.setCity(obj.getString("city_id"));
                                        adv.setPhone(obj.getString("phone"));
                                        adv.setCategory(obj.getString("category_id"));
                                        adv.setAdv_img(obj.getString("img"));


                                        advList.add(adv);
                                       recycle_adapter.notifyDataSetChanged();

                                    }
                                }catch (JSONException e){ e.printStackTrace();
                                    Snackbar snackbar=Snackbar.make (coordinatorLayout,
                                            e.getMessage() , Snackbar.LENGTH_LONG);
                                    snackbar.show ();}
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "sub_cat Error: " + error.getMessage());
                        Snackbar snackbar=Snackbar.make (coordinatorLayout,
                                error.getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show ();
                        hideDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("city", city_id);
                params.put("p_cat_id", p_cat_id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_string_req);

    }
    //=============================================================
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    //=====================================================================
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
//==============================================================
}
