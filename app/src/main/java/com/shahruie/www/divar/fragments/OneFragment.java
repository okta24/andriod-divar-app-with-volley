package com.shahruie.www.divar.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shahruie.www.divar.Adv;
import com.shahruie.www.divar.DBHandler;
import com.shahruie.www.divar.MyProgressDialog;
import com.shahruie.www.divar.My_adv_dialog;
import com.shahruie.www.divar.New_Main2;
import com.shahruie.www.divar.R;
import com.shahruie.www.divar.RecyclerItemClickListener;
import com.shahruie.www.divar.RecyclerViewAdapter;
import com.shahruie.www.divar.ShowOneAdv;
import com.shahruie.www.divar.app.AppConfig;
import com.shahruie.www.divar.app.AppController;
import com.shahruie.www.divar.helper.EndlessRecyclerOnScrollListener;
import com.shahruie.www.divar.helper.EndlessRecyclerViewScrollListener;
import com.shahruie.www.divar.helper.SQLiteHandler;
import com.shahruie.www.divar.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OneFragment extends Fragment {

    public static final String TAG = New_Main2.class.getSimpleName();
    private static MyProgressDialog pDialog;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

     static List<Adv> advList=new ArrayList<Adv>();
    int city_id;
    static RecyclerView.Adapter recycle_adapter;

    public static CoordinatorLayout coordinatorLayout;

    public static RecyclerView recyclerview;
    TextView zeroitem;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pref = getActivity().getApplicationContext().getSharedPreferences("setting", getActivity().MODE_PRIVATE);
        editor = pref.edit();
        city_id = pref.getInt("city_id", 0);

        pDialog = new MyProgressDialog(getActivity());
        pDialog.setCancelable(false);
        View view= inflater.inflate(R.layout.fragment_one, container, false);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        recyclerview = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(linearLayoutManager);
        recycle_adapter = new RecyclerViewAdapter(getActivity(), advList, zeroitem);
        recyclerview.setAdapter(recycle_adapter);

        recyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String title = advList.get(position).getTitle();
                        String description = advList.get(position).getDescription();
                        String email = advList.get(position).getEmail();
                        String phone = advList.get(position).getPhone();
                        String adv_img_url = advList.get(position).getAdv_img();
                        String date = advList.get(position).getDate();
                        String id = advList.get(position).getId();


                        Intent myIntent = new Intent(getActivity(), ShowOneAdv.class);
                        myIntent.putExtra("title", title);
                        myIntent.putExtra("description", description);
                        myIntent.putExtra("phone", phone);
                        myIntent.putExtra("adv_img_url", adv_img_url);
                        myIntent.putExtra("date", date);
                        myIntent.putExtra("email", email);
                        myIntent.putExtra("id", id);

                        getActivity().startActivity(myIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        //==============================================
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerview.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    Log.i("Yaeye!", "end called");

                    pagination_get("1",lastInScreen+"");

                    Log.d("con:","getnew");
                    loading = true;
                }
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

            }
        });
        //=======================================
        pagination_get(1+"",1+"");
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        city_id=pref.getInt("city_id",0);
        if(city_id!=0){
        }
        else {
            Toast.makeText(getActivity(), "on fregemnt select city", Toast.LENGTH_LONG).show();
            recyclerview.setVisibility(View.GONE);
        }
            super.onResume();
    }
//=========================================================


    public static void get_All_Adv(final String city_id) {

        if(advList!=null)
        {advList.clear();
            recycle_adapter.notifyDataSetChanged();}

        if(recyclerview.getVisibility()==View.GONE)
            recyclerview.setVisibility(View.VISIBLE);

        String tag_string_req = "req_showall";
        pDialog.setMessage("please waite");
        showDialog();
        StringRequest jsonArrayRequest = new StringRequest
                (Request.Method.POST, AppConfig.URL_SHOWALL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG,"show_all"+ response.toString());
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

                        Log.e(TAG, "ShowAll Error: " + error.getMessage());
                        Snackbar snackbar=Snackbar.make (coordinatorLayout,
                                error.getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show ();
                        hideDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("city", city_id);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_string_req);

    }
    //=============================================================
    public static void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    //=====================================================================
    public static void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    //==============================================================
//=========================================================


    public static void pagination_get(final String city_id,final String current_page) {

        if(recyclerview.getVisibility()==View.GONE)
            recyclerview.setVisibility(View.VISIBLE);
        String tag_string_req = "req_showall";
        pDialog.setMessage("please waite");
        showDialog();
        StringRequest jsonArrayRequest = new StringRequest
                (Request.Method.POST, AppConfig.URL_SHOWALL_PAGE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG,"show_all"+ response.toString());
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

                        Log.e(TAG, "ShowAll Error: " + error.getMessage());
                        Snackbar snackbar=Snackbar.make (coordinatorLayout,
                                error.getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show ();
                        hideDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("city", city_id);
                params.put("current_page", current_page);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_string_req);

    }
}
