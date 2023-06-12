package com.shahruie.www.divar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.shahruie.www.divar.fragments.FourFragment;

/**
 * Created by MR on 05/20/2017.
 */
public class Ok_No_Dialog extends Dialog implements
        View.OnClickListener {

    public Ok_No_Dialog(Context con, String tilte, String content) {
        super(con,android.R.style.Theme_Translucent_NoTitleBar);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.c=con;
        this.title=tilte;
        this.content=content;

            }
    String title,content;
    public Context c;
    public TextView btn_ok,close,txttitle,txtcontent;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ok_no_dialog);
         close=(TextView) findViewById(R.id.btnclose);
           btn_ok=(TextView) findViewById(R.id.btn_ok);
           txttitle=(TextView) findViewById(R.id.txttitle);
           txtcontent=(TextView) findViewById(R.id.txtcontent);
           txttitle.setText(title);
           txtcontent.setText(content);
        close.setOnClickListener(this);

           btn_ok.setOnClickListener(this);
    }
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btnclose:
                dismiss();
                break;
            case R.id.btn_ok:
                FourFragment.ok();
                dismiss();
                break;
            default:
                break;
        }

    }
}