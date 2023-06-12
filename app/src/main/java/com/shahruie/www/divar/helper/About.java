	package com.shahruie.www.divar.helper;

	import android.app.Activity;
	import android.app.Dialog;
	import android.content.Intent;
	import android.net.Uri;
	import android.os.Bundle;
	import android.view.View;
	import android.view.Window;
	import android.widget.Button;
	import android.widget.ImageView;
	import android.widget.TextView;

	import com.shahruie.www.divar.R;

	public class About  extends Dialog implements
	View.OnClickListener {

	public About(Activity a) {
		super(a);
		this.c=a;

		// TODO Auto-generated constructor stub
	}

	public Activity c;
	public Dialog d;
	ImageView imcal,imflash,immonajat,img_addressban;
	Button close;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.dialog1);
	imcal=(ImageView)findViewById(R.id.imcal);
	imflash=(ImageView)findViewById(R.id.imflash);
		img_addressban=(ImageView)findViewById(R.id.addressban);
	immonajat=(ImageView)findViewById(R.id.immonajat);
	TextView text = (TextView) findViewById(R.id.tvguide1);
	text.setText(R.string.darbareh);
	close=(Button)findViewById(R.id.btok);
	close.setOnClickListener(this);
	imcal.setOnClickListener(this);
	imflash.setOnClickListener(this);
	immonajat.setOnClickListener(this);
		img_addressban.setOnClickListener(this);

	}
	@Override
	public void onClick(View arg0) {
	switch (arg0.getId()) {
	case R.id.btok:
		dismiss();
		break;

	case R.id.imcal:
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("bazaar://details?id=" + "com.Shahruie.calculator"));
		intent.setPackage("com.farsitel.bazaar");
		c.startActivity(intent);
		break;
	case R.id.imflash:
		Intent intent1 = new Intent(Intent.ACTION_VIEW);
		intent1.setData(Uri.parse("bazaar://details?id=" + "com.example.flashlight"));
		intent1.setPackage("com.farsitel.bazaar");
		c.startActivity(intent1);
		break;
	case R.id.immonajat:
		Intent intent2 = new Intent(Intent.ACTION_VIEW);
		intent2.setData(Uri.parse("bazaar://details?id=" + "com.shahruie.monajat"));
		intent2.setPackage("com.farsitel.bazaar");
		c.startActivity(intent2);
		break;
		case R.id.addressban:
			Intent intent3 = new Intent(Intent.ACTION_VIEW);
			intent3.setData(Uri.parse("bazaar://details?id=" + "com.shahruie.www.AddressBanDemo2"));
			intent3.setPackage("com.farsitel.bazaar");
			c.startActivity(intent3);
			break;
	default:
		break;
	}

	}
	}
