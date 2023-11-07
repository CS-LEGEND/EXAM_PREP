package com.mo.examprep;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import android.text.method.DigitsKeyListener;
import android.view.inputmethod.InputMethodManager;



public class BankActivity extends AppCompatActivity {
	
	private FloatingActionButton _fab;
	private boolean isAllShowed = false;
	private String item_text = "";
	int QBIndex;
	int QBPaddingTop;
	
	private ArrayList<HashMap<String, Object>> items = new ArrayList<>();
	private ArrayList<String> line = new ArrayList<>();
	private  ArrayList<Question> quiz = new ArrayList<>();
	private  ArrayList<Integer> correct_index = new ArrayList<>();
	private  ArrayList<Boolean> isShowed = new ArrayList<>();
	
	private LinearLayout bg_layout;
	private LinearLayout title_bg;
	private ListView listview1;
	private LinearLayout back_bg;
	private LinearLayout title;
	private LinearLayout share_bg;
	private ImageView back_img;
	private TextView title_tv;
	private TextView subtitle_tv;
	private ImageView more_img;
	
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.bank);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_fab = findViewById(R.id._fab);
		
		bg_layout = findViewById(R.id.bg_layout);
		title_bg = findViewById(R.id.title_bg);
		listview1 = findViewById(R.id.listview1);
		back_bg = findViewById(R.id.back_bg);
		title = findViewById(R.id.title);
		share_bg = findViewById(R.id.share_bg);
		back_img = findViewById(R.id.back_img);
		title_tv = findViewById(R.id.title_tv);
		subtitle_tv = findViewById(R.id.subtitle_tv);
		more_img = findViewById(R.id.more_img);
		preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		
		back_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				QBIndex = listview1.getFirstVisiblePosition();
				QBPaddingTop = (listview1.getChildAt(0) == null) ? 0 : listview1.getChildAt(0).getTop() - listview1.getPaddingTop();
				preferences.edit().putInt("lv_ind", QBIndex).commit();
				preferences.edit().putInt("lv_pad", QBPaddingTop).commit();
				finish();
			}
		});
		
		more_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				// PopupMenu Initialization
				PopupMenu popup = new PopupMenu(BankActivity.this, more_img);
				android.view.Menu menu = popup.getMenu();
				
				menu.add(item_text);
				
				// onItemClicked
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(android.view.MenuItem item) {
						switch (item.getTitle().toString()) {
							case "Show All Answers":
							for (int i = 0; i < quiz.size(); i++ )
							 isShowed.set(i, true);
							((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
							item_text = "Hide All Answers";
							return true;
							
							case "Hide All Answers":
							for (int i = 0; i < quiz.size(); i++ )
							 isShowed.set(i, false);
							((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
							item_text = "Show All Answers";
							return true;
							
							default:
							return false;
						}
					}
				});
				
				popup.show();
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_jumpToQuestionDialog();
			}
		});
	}
	
	private void initializeLogic() {
		readQuiz("CSFinalEx.csv", quiz, correct_index);
		for(int _repeat16 = 0; _repeat16 < (int)(quiz.size()); _repeat16++) {
			{
				HashMap<String, Object> _item = new HashMap<>();
				_item.put("".concat(String.valueOf((long)(_repeat16))), "");
				items.add(_item);
			}
			
		}
		listview1.setAdapter(new Listview1Adapter(items));
		for(int _repeat25 = 0; _repeat25 < (int)(quiz.size()); _repeat25++) {
			isShowed.add(false);
		}
		if (!preferences.getString("fto", "").equals("N")) {
			item_text = "Show All Answers";
		}
		else {
			listview1.setSelectionFromTop(preferences.getInt("lv_ind", 0), preferences.getInt("lv_pad", 0));
			for (int i = 0; i < quiz.size(); i++)
			isShowed.set(i, preferences.getBoolean("isSh_" + String.valueOf(i), false));
			item_text = preferences.getString("menuItem", "");
		}
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		preferences.edit().putString("fto", "N").commit();
		preferences.edit().putString("menuItem", item_text).commit();
		QBIndex = listview1.getFirstVisiblePosition();
		QBPaddingTop = (listview1.getChildAt(0) == null) ? 0 : listview1.getChildAt(0).getTop() - listview1.getPaddingTop();
		preferences.edit().putInt("lv_ind", QBIndex).commit();
		preferences.edit().putInt("lv_pad", QBPaddingTop).commit();
		for (int i = 0; i < quiz.size(); i++)
		 preferences.edit().putBoolean("isSh_" + String.valueOf(i), isShowed.get(i)).commit();
	}
	public String _readQouted(final String _line) {
		String qouted_text = "";
		try {
			qouted_text = _line.substring((int)(1), (int)(_line.length() - 3));
			
			if (qouted_text.charAt(qouted_text.length() - 1) == ',')
			 return qouted_text.substring(0, (qouted_text.length() -1));
			
			return qouted_text;
			
		} catch (StringIndexOutOfBoundsException e) {
			return "";
		}
	}
	
	
	public void _extra() {
	}
	
	public void readQuiz(final String _fname, final ArrayList<Question> _quiz , final ArrayList<Integer> _ci) {
		
		BufferedReader reader = null;
		try {
			    reader = new BufferedReader(
			        new InputStreamReader(getAssets().open(_fname))); 
			
			    // do reading, usually loop until end of file reading 
			    String mLine;
			    while ((mLine = reader.readLine()) != null) {
				       //process line
				       line.add(mLine);
				    }
		} catch (IOException e) {
			    //log the exception
		} finally {
			    if (reader != null) {
				         try {
					             reader.close();
					         } catch (IOException e) {
					             //log the exception
					         }
				    }
		}
		
		for (int i = 0; i < line.size(); i+=5) {
			Question tempq = new Question();
			tempq.qt = _readQouted(line.get((int)(i)));
			for (int j = i+1; j < i+5; j++) {
				Answer tempa = new Answer();
				tempa.at = _readQouted(line.get((int)(j)));
				if (line.get((int)(j)).substring(line.get((int)(j)).length() - 1).equals("1")) {
					tempa.isTrue = true;
				}
				tempq.choice.add(tempa);
			}
			_quiz.add(tempq);
		}
		for (int i = 0; i < quiz.size(); i++)
			{
					for (int j = 0; j < 4; j++)
					{
							if (_quiz.get(i).choice.get(j).isTrue)
								_ci.add(j);
					}
			}
	}
	
	{
	}
	
	
	public void _setBGColors(final View _ch1, final View _ch2, final View _ch3, final View _ch4, final TextView _tv1, final TextView _tv2, final TextView _tv3, final TextView _tv4) {
		_ch1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)25, (int)3, 0xFF006E85, 0xFFFFFFFF));
		_tv1.setTextColor(0xFF006E85);
		_ch2.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)25, (int)3, 0xFF006E85, 0xFFFFFFFF));
		_tv2.setTextColor(0xFF006E85);
		_ch3.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)25, (int)3, 0xFF006E85, 0xFFFFFFFF));
		_tv3.setTextColor(0xFF006E85);
		_ch4.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)25, (int)3, 0xFF006E85, 0xFFFFFFFF));
		_tv4.setTextColor(0xFF006E85);
	}
	
	
	public void _markAnswer(final View _ch, final TextView _tv) {
		_ch.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)25, (int)3, 0xFF006E85, 0xFF006E85));
		_tv.setTextColor(0xFFFFFFFF);
	}
	
	
	public void _unmarkAnswer(final View _ch, final TextView _tv) {
		_ch.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)25, (int)3, 0xFF006E85, 0xFFFFFFFF));
		_tv.setTextColor(0xFF006E85);
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
	
	
	public void _jumpToQuestionDialog() {
		
		final AlertDialog dialog = new AlertDialog.Builder(BankActivity.this).create();
		LayoutInflater inflater = getLayoutInflater();
		
		View convertView = (View) inflater.inflate(R.layout.jump_dialog, null);
		dialog.setView(convertView);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		TextView b1 = (TextView)
		convertView.findViewById(R.id.b1);
		TextView b2 = (TextView)
		convertView.findViewById(R.id.b2);
		TextView t1 = (TextView)
		convertView.findViewById(R.id.t1);
		TextView t2 = (TextView)
		convertView.findViewById(R.id.t2);
		final EditText qnum = (EditText)
		convertView.findViewById(R.id.qnum);
		LinearLayout bg = (LinearLayout)
		convertView.findViewById(R.id.bg);
		_setEditTextRange(qnum, 1, 357);
		_rippleRoundStroke(bg, "#FFFFFF", "#000000", 15, 0, "#000000");
		_rippleRoundStroke(b1, "#F5F5F5", "#EEEEEE", 15, 2.5d, "#EEEEEE");
		_rippleRoundStroke(b2, "#006E85", "#40FFFFFF", 15, 0, "#000000");
		qnum.requestFocus();
		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				dialog.dismiss();
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				if (qnum.getText().toString().isEmpty()) {
					SketchwareUtil.showMessage(getApplicationContext(), "please, input question number");
				}
				else {
					listview1.setSelection((int)Double.parseDouble(qnum.getText().toString()) - 1);
					dialog.dismiss();
				}
			}
		});
		dialog.show();
	}
	
	
	public void _setEditTextRange(final TextView _input, final double _min, final double _max) {
		_input.setInputType(InputType.TYPE_CLASS_NUMBER);
		_input.setFilters(new InputFilter[]
		    {
			        new InputFilterMinMax((int)_min, (int)_max), 
			        new InputFilter.LengthFilter(String.valueOf((int)_max).length())
			    });
		_input.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
	}
	
	public class Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.question, null);
			}
			
			final LinearLayout quiz_bg = _view.findViewById(R.id.quiz_bg);
			final LinearLayout question_bg = _view.findViewById(R.id.question_bg);
			final LinearLayout answers_bg = _view.findViewById(R.id.answers_bg);
			final LinearLayout btn_bg = _view.findViewById(R.id.btn_bg);
			final TextView qtext = _view.findViewById(R.id.qtext);
			final TextView ch1 = _view.findViewById(R.id.ch1);
			final TextView ch2 = _view.findViewById(R.id.ch2);
			final TextView ch3 = _view.findViewById(R.id.ch3);
			final TextView ch4 = _view.findViewById(R.id.ch4);
			final ImageView show = _view.findViewById(R.id.show);
			
			_setBGColors(ch1, ch2, ch3, ch4, ch1, ch2, ch3, ch4);
			qtext.setTextIsSelectable(true);
			ch1.setTextIsSelectable(true);
			ch2.setTextIsSelectable(true);
			ch3.setTextIsSelectable(true);
			ch4.setTextIsSelectable(true);
			qtext.setText(String.valueOf((long)(_position + 1)).concat(". ".concat(quiz.get(_position).qt)));
			ch1.setText(quiz.get(_position).choice.get(0).at);
			ch2.setText(quiz.get(_position).choice.get(1).at);
			ch3.setText(quiz.get(_position).choice.get(2).at);
			ch4.setText(quiz.get(_position).choice.get(3).at);
			if (isShowed.get(_position)) {
				if (correct_index.get(_position) == 0) {
					_markAnswer(ch1, ch1);
				}
				if (correct_index.get(_position) == 1) {
					_markAnswer(ch2, ch2);
				}
				if (correct_index.get(_position) == 2) {
					_markAnswer(ch3, ch3);
				}
				if (correct_index.get(_position) == 3) {
					_markAnswer(ch4, ch4);
				}
				show.setImageResource(R.drawable.design_ic_visibility_2);
			}
			else {
				show.setImageResource(R.drawable.design_ic_visibility_1);
			}
			show.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if (!isShowed.get(_position)) {
						if (correct_index.get(_position) == 0) {
							_markAnswer(ch1, ch1);
						}
						if (correct_index.get(_position) == 1) {
							_markAnswer(ch2, ch2);
						}
						if (correct_index.get(_position) == 2) {
							_markAnswer(ch3, ch3);
						}
						if (correct_index.get(_position) == 3) {
							_markAnswer(ch4, ch4);
						}
						show.setImageResource(R.drawable.design_ic_visibility_2);
						isShowed.set(_position, true);
					}
					else {
						if (correct_index.get(_position) == 0) {
							_unmarkAnswer(ch1, ch1);
						}
						if (correct_index.get(_position) == 1) {
							_unmarkAnswer(ch2, ch2);
						}
						if (correct_index.get(_position) == 2) {
							_unmarkAnswer(ch3, ch3);
						}
						if (correct_index.get(_position) == 3) {
							_unmarkAnswer(ch4, ch4);
						}
						show.setImageResource(R.drawable.design_ic_visibility_1);
						isShowed.set(_position, false);
					}
				}
			});
			
			return _view;
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
