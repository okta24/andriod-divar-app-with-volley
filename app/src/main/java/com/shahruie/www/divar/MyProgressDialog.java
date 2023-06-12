package com.shahruie.www.divar;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by MR on 08/14/2017.
 */
public class MyProgressDialog extends AlertDialog {
    public MyProgressDialog(Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.dialog_progress);
    }
}