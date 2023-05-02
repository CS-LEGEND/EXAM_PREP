package com.mo.examprep;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import android.content.pm.PackageManager;

public class HomeActivity extends AppCompatActivity {
	
	Parcelable state;
	private HashMap<String, Object> UpdatifyMap = new HashMap<>();
	private HashMap<String, Object> updateResponse = new HashMap<>();
	
	private LinearLayout bg_layout;
	private LinearLayout title_bg;
	private ScrollView vscroll1;
	private LinearLayout dev;
	private LinearLayout back_bg;
	private LinearLayout title;
	private LinearLayout share_bg;
	private TextView title_tv;
	private TextView subtitle_tv;
	private LinearLayout background;
	private LinearLayout content_bg;
	private LinearLayout qbb;
	private LinearLayout pqb;
	private TextView t1;
	private TextView st1;
	private TextView t2;
	private TextView st2;
	private TextView textview1;
	
	private Intent toMain = new Intent();
	private RequestNetwork checkUpdate;
	private RequestNetwork.RequestListener _checkUpdate_request_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		bg_layout = findViewById(R.id.bg_layout);
		title_bg = findViewById(R.id.title_bg);
		vscroll1 = findViewById(R.id.vscroll1);
		dev = findViewById(R.id.dev);
		back_bg = findViewById(R.id.back_bg);
		title = findViewById(R.id.title);
		share_bg = findViewById(R.id.share_bg);
		title_tv = findViewById(R.id.title_tv);
		subtitle_tv = findViewById(R.id.subtitle_tv);
		background = findViewById(R.id.background);
		content_bg = findViewById(R.id.content_bg);
		qbb = findViewById(R.id.qbb);
		pqb = findViewById(R.id.pqb);
		t1 = findViewById(R.id.t1);
		st1 = findViewById(R.id.st1);
		t2 = findViewById(R.id.t2);
		st2 = findViewById(R.id.st2);
		textview1 = findViewById(R.id.textview1);
		checkUpdate = new RequestNetwork(this);
		
		qbb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				toMain.setClass(getApplicationContext(), BankActivity.class);
				startActivity(toMain);
			}
		});
		
		pqb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				toMain.setClass(getApplicationContext(), MainActivity.class);
				startActivity(toMain);
			}
		});
		
		_checkUpdate_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				_updateDialog(_response);
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	
	private void initializeLogic() {
		_rippleRoundStroke(qbb, "#006E85", "#40263238", 50, 1, "#000000");
		_rippleRoundStroke(pqb, "#006E85", "#4036464F", 50, 1, "#000000");
		checkUpdate.startRequestNetwork(RequestNetworkController.GET, "https://raw.githubusercontent.com/CS-LEGEND/exam-prep-upapi/main/api.json", "", _checkUpdate_request_listener);
	}
	
	public void _rippleRoundStroke(final View _view, final String _focus, final String _pressed, final double _round, final double _stroke, final String _strokeclr) {
		android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
		GG.setColor(Color.parseColor(_focus));
		GG.setCornerRadius((float)_round);
		GG.setStroke((int) _stroke,
		Color.parseColor("#" + _strokeclr.replace("#", "")));
		android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
		_view.setBackground(RE);
	}
	
	
	public void _updateDialog(final String _response) {
		boolean resCon = true;
		
		try {
			updateResponse = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
		} catch (Exception e) {
			resCon = false;
		}
		if (resCon) {
			try {
				android.content.pm.PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
					
				String currVersion = packageInfo.versionName;
				
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
			if (updateResponse.get("isLive").toString().equals("true") && !updateResponse.get("versionName").toString().equals("currVersion")) {
				final AlertDialog upDialog = new AlertDialog.Builder(HomeActivity.this).create();
				View inflate = getLayoutInflater().inflate(R.layout.update_dialog,null); 
				upDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				upDialog.setView(inflate);
				TextView t1 = (TextView) inflate.findViewById(R.id.t1);
				
				TextView t2 = (TextView) inflate.findViewById(R.id.t2);
				
				TextView b1 = (TextView) inflate.findViewById(R.id.b1);
				
				TextView b2 = (TextView) inflate.findViewById(R.id.b2);
				
				LinearLayout bg = (LinearLayout) inflate.findViewById(R.id.bg);
				t1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
				t2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_light.ttf"), 0);
				b1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
				b2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
				t1.setText(updateResponse.get("updateTitle").toString());
				t2.setText(updateResponse.get("updateSubTitle").toString());
				b1.setText(updateResponse.get("whatNewBtn").toString());
				b2.setText(updateResponse.get("updateBtn").toString());
				_rippleRoundStroke(bg, "#FFFFFF", "#000000", 15, 0, "#000000");
				_rippleRoundStroke(b1, "#F5F5F5", "#E0E0E0", 15, 0, "#000000");
				_rippleRoundStroke(b2, "#006E85", "#40FFFFFF", 15, 0, "#000000");
				b1.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						upDialog.dismiss();
						try {
							Intent whatNewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateResponse.get("whatNewLink").toString()));
							startActivity(whatNewIntent);
														}
						catch(Exception e) {
							SketchwareUtil.showMessage(getApplicationContext(), (e.toString()));
														}
					}
				});
				b2.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						upDialog.dismiss();
						try {
							Intent updateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateResponse.get("updateLink").toString()));
							startActivity(updateIntent);
														}
						catch(Exception e) {
							SketchwareUtil.showMessage(getApplicationContext(), (e.toString()));
														}
						
					}
				});
				boolean isCancelable = false;
				
				if (updateResponse.get("isCancelable").toString().equals("true"))
				 isCancelable = true;
				
				upDialog.setCancelable(isCancelable);
				upDialog.show();
			}
		}
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}