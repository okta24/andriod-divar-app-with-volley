package com.shahruie.www.divar;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shahruie.www.divar.app.AppConfig;
import com.shahruie.www.divar.app.AppController;
import com.shahruie.www.divar.fragments.OneFragment;
import com.shahruie.www.divar.helper.DatabaseAccess;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MR on 05/10/2017.
 */
public class Add  extends AppCompatActivity implements View.OnClickListener {
    EditText et_title, et_description, et_phone, et_email;
    Button BtnCategory,BtnCity;
    ImageView ImgDemo ;
    private List<Adv> advList=new ArrayList<Adv>() ;
    public static CoordinatorLayout coordinatorLayout;


    private static final int REQUEST_CAMERA =1;
    private static final int SELECT_FILE =2;
    private Uri imageToUploadUri;
    int city_id,parent_cat_id,sub_cat_id;
    ArrayAdapter<String> c_adapter;

    String title , description ,email,email0,phone, phone0,userChoosenTask;

    StringRequest AddAdvReq;

    private int PICK_IMAGE_REQUEST = 1000;
    private Bitmap adv_bitmap;
    private String img_name="";
    private int mode;
    private String id="0";
    private TextView add_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        defineVolleyCodes();
        defineViews();
    }
    private void set_drasft(Boolean draft){
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = getApplicationContext().getSharedPreferences("adv",MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean("draft",draft);
        editor.commit();
    }

    //=======================================
    private void defineVolleyCodes() {
        OneFragment.showDialog();
        AddAdvReq = new StringRequest(Request.Method.POST, AppConfig.ADV_ADD_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        OneFragment.hideDialog();
                        if (mode == 1) {
                            try {
                                JSONObject json = new JSONObject(response);
                                Snackbar snackbar=Snackbar.make (Add.coordinatorLayout,
                                        json.getString("result"), Snackbar.LENGTH_LONG);
                                snackbar.show ();
                                if(json.getString("code").equals("1")){
                                    set_drasft(false);
                                    finish();
                                }
                                if(json.getString("code").equals("0")){
                                    Log.d("er",json.getString("result"));
                                }
                            } catch (JSONException e) {
                                Toast.makeText(Add.this, response, Toast.LENGTH_SHORT).show();
                                Log.d("div",response);
                                e.printStackTrace();
                                Log.d("er", e.getMessage());
                            }

                        } else {

                            try {
                                JSONObject json = new JSONObject(response);
                                Snackbar snackbar=Snackbar.make (Add.coordinatorLayout,
                                        json.getString("result"), Snackbar.LENGTH_LONG);
                                snackbar.show ();
                                if(json.getString("code").equals("1")){
                                    finish();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(Add.this, response, Toast.LENGTH_SHORT).show();
                                Log.d("div",response);
                                e.printStackTrace();
                                Log.d("er", e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error!=null && error.getMessage() !=null){
                            Toast.makeText(getApplicationContext(),"error VOLLEY "+error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();

                        }
                        Toast.makeText(Add.this, error.toString(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        OneFragment.hideDialog();
                        if (error.networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                // Show timeout error message
                                Toast.makeText(Add.this,
                                        "خطا دوباره تلاش کنید!",
                                        Toast.LENGTH_SHORT).show();

                            }
                            if (error.getClass().equals(NoConnectionError.class)) {
                                // Show timeout error message
                                Toast.makeText(Add.this,
                                        "اتصال اینترنتی وجود ندارد!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String , String>  params = new HashMap<String,String>();
                //add POST parametersss

                String img ="";
                BitmapDrawable drawable = (BitmapDrawable) ImgDemo.getDrawable();
                if(drawable!=null){
                    adv_bitmap = drawable.getBitmap();
                    img=getStringImage(adv_bitmap);
                    img_name=img;}

                if((mode==0)&&(!img.equals(img_name)))
                    img_name=img;
                if(mode==0) {
                    img_name = img_name.replace(AppConfig.IMG_base_url, "");
                    if(img_name.equals("default.jpg")){img_name="";}
                }
                Log.d("img:",img_name);
                params.put("title",title);
                params.put("description",description);
                params.put("phone",phone);
                params.put("email",email);
                params.put("category",sub_cat_id+"");
                params.put("city",city_id+"");
               params.put("img",img_name);
                params.put("mode",mode+"");
                params.put("id",id);


                return params;
            }
        };
    }


private Boolean has_draft(){

    if ((title.length()==0)&&(phone.length()==0)&&(email.length()==0)
            &&(description.length()==0)){
        return false;
    }
    return true;
}
    //==================================================
    private   void  show_city_list(){
        final Dialog city_dialog = new Dialog(Add.this);
        city_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        city_dialog.setContentView(R.layout.city_list_dialog);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Add.this);
        databaseAccess.open();
        List<String> citys = databaseAccess.get_city_name();
        databaseAccess.close();

        c_adapter = new ArrayAdapter<String>(Add.this,R.layout.city_list_row,R.id.city_name, citys);
        ListView city_list=(ListView)city_dialog.findViewById(R.id.city_list);
        city_list.setAdapter(c_adapter);

        city_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city_id=++position;
                TextView city_name=(TextView)view.findViewById(R.id.city_name);
                BtnCity.setText(city_name.getText().toString());
                city_dialog.dismiss();
            }
        });
        EditText et_city_search = (EditText) city_dialog.findViewById(R.id.et_city_search);
        et_city_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                c_adapter.getFilter().filter(s);
            }
        });

        city_dialog.show();
        city_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {


            }
        });

    }

    //=======================================
    private void defineViews() {

        et_title = (EditText)findViewById(R.id.et_title);
        et_description = (EditText)findViewById(R.id.et_description);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_email = (EditText)findViewById(R.id.et_email);

        et_title.setFilters(new InputFilter[] { filter });
        et_description.setFilters(new InputFilter[] { filter });


        BtnCategory=(Button)findViewById(R.id.btn_category);
        BtnCity=(Button)findViewById(R.id.btn_city);
        CoordinatorLayout coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        Button btn_send = (Button) toolbarTop.findViewById(R.id.btnsendadv);
        ImageView btn_back = (ImageView) toolbarTop.findViewById(R.id.btnback);
        ImageView btndel = (ImageView) toolbarTop.findViewById(R.id.btndeladv);
        ImageView btn_save = (ImageView) toolbarTop.findViewById(R.id.btnsaveadv);
        add_title=(TextView)toolbarTop.findViewById(R.id.add_title);
        add_title.setText("آگهی جدید");
        ImgDemo = (ImageView) findViewById(R.id.adv_img);
        //ImgDemo.setImageResource(R.drawable.default_img);
        Button btnloadimg = (Button) findViewById(R.id.btn_img);
        btn_save.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btndel.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        BtnCity.setOnClickListener(this);
        BtnCategory.setOnClickListener(this);
        btnloadimg.setOnClickListener(this);
        Intent intent=getIntent();
        mode=intent.getIntExtra("mode",3);
        if(mode==0){
            add_title.setText("ویرایش آگهی");
            et_title.setText(intent.getStringExtra("title"));
            et_description.setText(intent.getStringExtra("des"));
            et_phone.setText(intent.getStringExtra("phone"));
            et_email.setText(intent.getStringExtra("email"));
            city_id= Integer.parseInt(intent.getStringExtra("cat"));
            sub_cat_id=Integer.parseInt(intent.getStringExtra("city"));
            img_name=intent.getStringExtra("img");
            id=intent.getStringExtra("id");
            phone0=intent.getStringExtra("phone");
            email0=intent.getStringExtra("email");

        }else{
            SharedPreferences pref;
            pref = getApplicationContext().getSharedPreferences("adv",MODE_PRIVATE);
            if(pref.getBoolean("draft",false)){
                set_by_pref();
            }
        }

    }

    private void set_by_pref() {
        SharedPreferences pref;
        pref = getApplicationContext().getSharedPreferences("adv",MODE_PRIVATE);
        et_title.setText(pref.getString("title",""));
        et_phone.setText(pref.getString("phone",""));
        et_email.setText(pref.getString("email",""));
        et_description.setText(pref.getString("description",""));
        city_id=Integer.parseInt(pref.getString("city_id",""));
        sub_cat_id=Integer.parseInt(pref.getString("sub_cat_id",""));
        mode=1;
        add_title.setText("آگهی جدید");
    }
    //=======================================


    private void ChooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select"), PICK_IMAGE_REQUEST);
    }

    //convert image to base 64 String
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private String blockCharacterSet = "<>/~#^|%&*!";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnsendadv:
                title = et_title.getText().toString();
                description = et_description.getText().toString();
                phone = et_phone.getText().toString();
                email = et_email.getText().toString();
                Log.d("ti:",title);
                Log.d("des:",description);
                Log.d("em:",email);
                Log.d("ph:",phone);

                Log.d("mo:",mode+"");
                Log.d("id:",id);
                Log.d("ci:",city_id+"");
                Log.d("su:",sub_cat_id+"");
                Log.d("img:",img_name);
                if(validate_form())
                    AppController.getInstance().addToRequestQueue(AddAdvReq);
                break;
            case R.id.btnsaveadv:
               save_adv();
                finish();
                break;
            case R.id.btndeladv:
                set_drasft(false);
                Toast.makeText(Add.this,"آگهی حذف شد",Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.btnback:
                finish();
                save_adv();
                break;
            case R.id.btn_img:
                selectImage();
                break;
            case R.id.btn_city:
                show_city_list();
                break;

            case R.id.btn_category:
                show_category_list();
                break;

        }
    }

    private void save_adv() {
        title = et_title.getText().toString();
        description = et_description.getText().toString();
        phone = et_phone.getText().toString();
        email = et_email.getText().toString();
        if(has_draft()) {
            SharedPreferences pref;
            SharedPreferences.Editor editor;
            pref = getApplicationContext().getSharedPreferences("adv", MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("title", title);
            editor.putString("description", description);
            editor.putString("phone", phone);
            editor.putString("email", email);
            editor.putString("city_id", city_id + "");
            editor.putString("sub_cat_id", sub_cat_id + "");
            editor.putBoolean("draft", true);
            editor.commit();
            Toast.makeText(Add.this, "آگهی ذخیره شد", Toast.LENGTH_LONG).show();
        }
    }

    //==================================================
    private   void  show_category_list(){
        final Dialog category_dialog = new Dialog(Add.this);
        category_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        category_dialog.setContentView(R.layout.category_list_dialog);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Add.this);
        databaseAccess.open();
        List<String> parent_cat = databaseAccess.get_parent_category_name();
        databaseAccess.close();

        final ListView sub_cat_list=(ListView) category_dialog.findViewById(R.id.sub_cat_list);

        final ArrayAdapter<String> p_adapter = new ArrayAdapter<String>(Add.this,android.R.layout.simple_list_item_1, parent_cat);
        final ListView parent_cat_list=(ListView) category_dialog.findViewById(R.id.parent_cat_list);
        parent_cat_list.setAdapter(p_adapter);

        parent_cat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent_cat_id=++position;
                parent_cat_list.setVisibility(View.GONE);

                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Add.this);
                databaseAccess.open();
                List<String> sub_cat = databaseAccess.get_sub_category_name(parent_cat_id);
                databaseAccess.close();

                ArrayAdapter<String> s_adapter = new ArrayAdapter<String>(Add.this,android.R.layout.simple_list_item_1, sub_cat);
                sub_cat_list.setAdapter(s_adapter);
                sub_cat_list.setVisibility(view.VISIBLE);

               //
            }
        });

        sub_cat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sub_cat_id=++position;
                TextView cat=(TextView)view.findViewById(android.R.id.text1);
                BtnCategory.setText(cat.getText().toString());
                category_dialog.dismiss();
            }
        });
        EditText et_city_search = (EditText) category_dialog.findViewById(R.id.et_city_search);
        et_city_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                p_adapter.getFilter().filter(s);
            }
        });

        category_dialog.show();
    }
    //===================================
    private boolean validate_form(){

        if(city_id==0){
            LinearLayout ll_city=(LinearLayout)findViewById(R.id.ll_city);
            TextView tv_city=(TextView)findViewById(R.id.tv_city);
            tv_city.setText("لطفا شهر مورد نظرتان را انتخاب کنید!");
            ll_city.setBackgroundColor(ResourcesCompat
                    .getColor(getResources(),R.color.erorr,null));
            return false;
        }
        if(sub_cat_id==0){
            LinearLayout ll_sub_cat=(LinearLayout)findViewById(R.id.ll_sub_cat);
            TextView tv_sub_cat =(TextView)findViewById(R.id.tv_sub_cat);
            tv_sub_cat.setText("لطفا موضوع آگهی را مشخص کنید!");
            ll_sub_cat.setBackgroundColor(ResourcesCompat
                    .getColor(getResources(),R.color.erorr,null));
            return false;
        }

        if(title.length()==0){
            et_title.setHint("عنوان آگهی نمی تواند خالی باشد!");
            et_title.setBackgroundColor(ResourcesCompat
                .getColor(getResources(),R.color.erorr,null));
            return false;
        }

        if(et_description.length()==0){
            et_description.setHint("توضیحات نمی تواند خالی باشد!");
            et_description.setBackgroundColor(ResourcesCompat
                    .getColor(getResources(),R.color.erorr,null));
            return false;
        }
        if(phone.length()==0){
            et_phone.setHint("شماره تماس نمی تواند خالی باشد!");
            et_phone.setBackgroundColor(ResourcesCompat
                    .getColor(getResources(),R.color.erorr,null));
            return false;
        }


        return true;
    }
//===================================================
    public void onBackPressed() {
            super.onBackPressed();
        finish();
        save_adv();
        }
    //=================================================
    private void selectImage() {
        final CharSequence[] items = {
                getApplicationContext().getResources().getString(R.string.takephoto),
                getApplicationContext().getResources().getString(R.string.gallerry),
                getApplicationContext().getResources().getString(R.string.ok) };
        AlertDialog.Builder builder = new AlertDialog.Builder(Add.this);
        builder.setTitle( getApplicationContext().getResources().getString(R.string.image));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Add.this);
                if (item==0) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (item==1) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (item==2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        imageToUploadUri = Uri.fromFile(destination);
        startActivityForResult(intent,REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_FILE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String s = getRealPathFromURI(selectedImageUri);
        ImgDemo.setImageBitmap(getBitmap(s));

        //imagepath = s;
    }
    public String getRealPathFromURI(Uri uri) {
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        cursor.close();
        return imagePath;
    }
    private void onCaptureImageResult(Intent data) {
        if(imageToUploadUri != null){
            Uri selectedImage = imageToUploadUri;
            getContentResolver().notifyChange(selectedImage, null);
            Bitmap reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
           // imagepath=selectedImage.getPath();
            if(reducedSizeBitmap != null){
                ImgDemo.setImageBitmap(reducedSizeBitmap);
            }else{
                Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
        }
    }
    private Bitmap getBitmap(String path) {
        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }


    }

