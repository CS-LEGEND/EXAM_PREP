package com.statistics.examprep;

import com.statistics.examprep.HomeActivity;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.textfield.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.regex.*;
import org.json.*;
import android.text.method.DigitsKeyListener;
import android.view.inputmethod.InputMethodManager;
//My Classes

class Answer {
	public String at = "";
	public boolean isTrue = false;
};

class Question {
	public String qt = "";
	public ArrayList<Answer> choice = new ArrayList<>();
};

class InputFilterMinMax implements InputFilter {

	private int min, max;

	public InputFilterMinMax(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		try {
			int input = Integer.parseInt(dest.toString() + source.toString());
			if (isInRange(min, max, input))
				return null;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return "";
	}

	private boolean isInRange(int a, int b, int c) {
		return b > a ? c >= a && c <= b : c >= b && c <= a;
	}
}

public class MainActivity extends AppCompatActivity {

	private double ch_num = 0;
	private double qindex = 0;
	private double ccount = 0;
	private double qcount = 0;
	private ArrayList<String> alpha = new ArrayList<String>(Arrays.asList("A", "B", "C", "D"));
	private ArrayList<Integer> indices = new ArrayList<Integer>();
	private double arrIndex = 0;
	private double qquantity = 0;
	private boolean isConfirmed = false;
	private boolean isSelected = false;
	private double ui_id = 0;
	private boolean rand = false;
	private boolean all = false;
	private boolean toSave = true;
	private boolean onReDia = false;
	private boolean rAnswers = false;
	private boolean chIndexSaved = false;
	private String sol = "";
	private boolean notExit = false;
	private boolean isExit = true;

	private ArrayList<String> line = new ArrayList<>();
	private ArrayList<Question> quiz = new ArrayList<>();
	private ArrayList<Integer> correct_index = new ArrayList<>();
	private ArrayList<Integer> ch_index = new ArrayList<>();

	private LinearLayout bg_layout;
	private LinearLayout title_bg;
	private ScrollView scroll_bg;
	private LinearLayout share_bg;
	private LinearLayout title;
	private LinearLayout info_bg;
	private ImageView back_image;
	private TextView title_tv;
	private TextView subtitle_tv;
	private ImageView more_img;
	private LinearLayout background;
	private LinearLayout sq_bg;
	private LinearLayout quiz_bg;
	private LinearLayout grading_bg;
	private TextView q_tv;
	private TextInputLayout til;
	private CheckBox all_ch;
	private CheckBox rand_ch;
	private CheckBox ra_ch;
	private TextView sq;
	private TextInputEditText qq;
	private LinearLayout question_bg;
	private LinearLayout answers_bg;
	private LinearLayout btn_bg;
	private TextView qtext;
	private TextView ch1;
	private TextView ch2;
	private TextView ch3;
	private TextView ch4;
	private TextView ca;
	private TextView nt;
	private TextView grading_text;
	private LinearLayout btns_bg;
	private TextView aq;
	private TextView mm;

	private SharedPreferences quiz_progress;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}

	private void initialize(Bundle _savedInstanceState) {
		bg_layout = findViewById(R.id.bg_layout);
		title_bg = findViewById(R.id.title_bg);
		scroll_bg = findViewById(R.id.scroll_bg);
		share_bg = findViewById(R.id.share_bg);
		title = findViewById(R.id.title);
		info_bg = findViewById(R.id.info_bg);
		back_image = findViewById(R.id.back_image);
		title_tv = findViewById(R.id.title_tv);
		subtitle_tv = findViewById(R.id.subtitle_tv);
		more_img = findViewById(R.id.more_img);
		background = findViewById(R.id.background);
		sq_bg = findViewById(R.id.sq_bg);
		quiz_bg = findViewById(R.id.quiz_bg);
		grading_bg = findViewById(R.id.grading_bg);
		q_tv = findViewById(R.id.q_tv);
		til = findViewById(R.id.til);
		all_ch = findViewById(R.id.all_ch);
		rand_ch = findViewById(R.id.rand_ch);
		ra_ch = findViewById(R.id.ra_ch);
		sq = findViewById(R.id.sq);
		qq = findViewById(R.id.qq);
		question_bg = findViewById(R.id.question_bg);
		answers_bg = findViewById(R.id.answers_bg);
		btn_bg = findViewById(R.id.btn_bg);
		qtext = findViewById(R.id.qtext);
		ch1 = findViewById(R.id.ch1);
		ch2 = findViewById(R.id.ch2);
		ch3 = findViewById(R.id.ch3);
		ch4 = findViewById(R.id.ch4);
		ca = findViewById(R.id.ca);
		nt = findViewById(R.id.nt);
		grading_text = findViewById(R.id.grading_text);
		btns_bg = findViewById(R.id.btns_bg);
		aq = findViewById(R.id.aq);
		mm = findViewById(R.id.mm);
		quiz_progress = getSharedPreferences("quiz", Activity.MODE_PRIVATE);
		prefs = getSharedPreferences("user_prefs", Activity.MODE_PRIVATE);

		back_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (ui_id == 1) {
					_onBackDialog("Wanna Stop Quiz?",
							"Do you want to stop taking this quiz and return to the main menu?", "Cancel", "Stop",
							"#006E85");
				} else {
					finish();
				}
			}
		});

		more_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				// PopupMenu Initialization
				PopupMenu popup = new PopupMenu(MainActivity.this, more_img);
				android.view.Menu menu = popup.getMenu();

				menu.add("Reset Quiz");
				menu.add("Stop Taking Quiz");

				// onItemClicked
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(android.view.MenuItem item) {
						switch (item.getTitle().toString()) {
						case "Reset Quiz":
							_resetQuizDialog();
							return true;
						case "Stop Taking Quiz":
							_onBackDialog("Wanna Stop Quiz?",
									"Do you want to stop taking this quiz and return to the main menu?", "Cancel",
									"Stop", "#006E85");
							return true;
						default:
							return false;
						}
					}
				});

				popup.show();
			}
		});

		all_ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2) {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					qq.setEnabled(false);
				} else {
					qq.setEnabled(true);
					qq.requestFocus();
					SketchwareUtil.showKeyboard(getApplicationContext());
				}
			}
		});

		sq.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (all_ch.isChecked() || !qq.getText().toString().equals("")) {
					_resetValues();
					_setUI(1);
					all = all_ch.isChecked();
					if (all) {
						qquantity = quiz.size();
					} else {
						qquantity = Double.parseDouble(qq.getText().toString());
					}
					rand = rand_ch.isChecked();
					rAnswers = ra_ch.isChecked();
					_startQuiz(qquantity, rand);
					KeyboardUtils.hideKeyboard(MainActivity.this);
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Please, input questions quantity");
				}
			}
		});

		ch1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!isConfirmed) {
					_unmarkChoice(ch_num);
					ch_num = 0;
					_markSelected(ch_num);
					isSelected = true;
				}
			}
		});

		ch2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!isConfirmed) {
					_unmarkChoice(ch_num);
					ch_num = 1;
					_markSelected(ch_num);
					isSelected = true;
				}
			}
		});

		ch3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!isConfirmed) {
					_unmarkChoice(ch_num);
					ch_num = 2;
					_markSelected(ch_num);
					isSelected = true;
				}
			}
		});

		ch4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!isConfirmed) {
					_unmarkChoice(ch_num);
					ch_num = 3;
					_markSelected(ch_num);
					isSelected = true;
				}
			}
		});

		nt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!isConfirmed) {
					if (isSelected) {
						if (rAnswers) {
							_markTrueOrFalse(ch_num,
									quiz.get((int) qindex).choice.get(ch_index.get((int) ch_num)).isTrue);
							if (quiz.get((int) qindex).choice.get(ch_index.get((int) ch_num)).isTrue) {
								ccount++;
							}
						} else {
							_markTrueOrFalse(ch_num, quiz.get((int) qindex).choice.get((int) ch_num).isTrue);
							if (quiz.get((int) qindex).choice.get((int) ch_num).isTrue) {
								ccount++;
							}
						}
						isConfirmed = true;
						if (qcount == qquantity) {
							nt.setText("Result");
						} else {
							nt.setText("Next");
						}
					} else {
						SketchwareUtil.showMessage(getApplicationContext(), "Please, choose an answer!");
					}
				} else {
					nt.setText("Confirm");
					isSelected = false;
					isConfirmed = false;
					if (qcount == qquantity) {
						_setUI(2);
						_quizGrading(qquantity);
					} else {
						_setBGColors();
						ca.setText("");
						if (rand_ch.isChecked()) {
							indices.remove((int) (arrIndex));
							arrIndex = SketchwareUtil.getRandom((int) (0), (int) (indices.size() - 1));
							qindex = indices.get((int) (arrIndex)).doubleValue();
						} else {
							qindex++;
						}
						qcount++;
						_setQuestion(qindex);
					}
				}
			}
		});

		aq.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_setUI(0);
				SketchwareUtil.showKeyboard(getApplicationContext());
			}
		});

		mm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});
	}

	private void initializeLogic() {
		readQuiz("CSFinalEx.csv", quiz, correct_index);
		q_tv.setText("How many questions do you want to practice (1-" + quiz.size() + ")?");
		_restoreUserPrefs();
		_setUI(0);
		if (quiz_progress.getString("isSaved", "").equals("Y")) {
			_progressDialog();
		}
	}

	@Override
	public void onBackPressed() {
		if (ui_id == 1) {
			_onBackDialog("Wanna Stop Quiz?", "Do you want to stop taking this quiz and return to the main menu?",
					"Cancel", "Stop", "#006E85");
		} else {
			finish();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		_saveUserPrefs();
		if ((ui_id == 1) && toSave) {
			_saveProgress();
			quiz_progress.edit().putString("isSaved", "Y").commit();
		} else {
			quiz_progress.edit().putString("isSaved", "N").commit();
		}
		if (onReDia) {
			quiz_progress.edit().putString("isSaved", "Y").commit();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (isExit && ((ui_id == 1) && toSave)) {
			SketchwareUtil.showMessage(getApplicationContext(), "Quiz paused, progress will be saved");
		}
	}

	public void _setBGColors() {
		ch1.setBackground(new GradientDrawable() {
			public GradientDrawable getIns(int a, int b, int c, int d) {
				this.setCornerRadius(a);
				this.setStroke(b, c);
				this.setColor(d);
				return this;
			}
		}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFFFFFFFF));
		ch1.setTextColor(0xFF006E85);
		ch2.setBackground(new GradientDrawable() {
			public GradientDrawable getIns(int a, int b, int c, int d) {
				this.setCornerRadius(a);
				this.setStroke(b, c);
				this.setColor(d);
				return this;
			}
		}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFFFFFFFF));
		ch2.setTextColor(0xFF006E85);
		ch3.setBackground(new GradientDrawable() {
			public GradientDrawable getIns(int a, int b, int c, int d) {
				this.setCornerRadius(a);
				this.setStroke(b, c);
				this.setColor(d);
				return this;
			}
		}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFFFFFFFF));
		ch3.setTextColor(0xFF006E85);
		ch4.setBackground(new GradientDrawable() {
			public GradientDrawable getIns(int a, int b, int c, int d) {
				this.setCornerRadius(a);
				this.setStroke(b, c);
				this.setColor(d);
				return this;
			}
		}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFFFFFFFF));
		ch4.setTextColor(0xFF006E85);
	}

	public void _markSelected(final double _cn) {
		if (_cn == 0) {
			ch1.setBackground(new GradientDrawable() {
				public GradientDrawable getIns(int a, int b, int c, int d) {
					this.setCornerRadius(a);
					this.setStroke(b, c);
					this.setColor(d);
					return this;
				}
			}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFF006E85));
			ch1.setTextColor(0xFFFFFFFF);
		} else {
			if (_cn == 1) {
				ch2.setBackground(new GradientDrawable() {
					public GradientDrawable getIns(int a, int b, int c, int d) {
						this.setCornerRadius(a);
						this.setStroke(b, c);
						this.setColor(d);
						return this;
					}
				}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFF006E85));
				ch2.setTextColor(0xFFFFFFFF);
			} else {
				if (_cn == 2) {
					ch3.setBackground(new GradientDrawable() {
						public GradientDrawable getIns(int a, int b, int c, int d) {
							this.setCornerRadius(a);
							this.setStroke(b, c);
							this.setColor(d);
							return this;
						}
					}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFF006E85));
					ch3.setTextColor(0xFFFFFFFF);
				} else {
					if (_cn == 3) {
						ch4.setBackground(new GradientDrawable() {
							public GradientDrawable getIns(int a, int b, int c, int d) {
								this.setCornerRadius(a);
								this.setStroke(b, c);
								this.setColor(d);
								return this;
							}
						}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFF006E85));
						ch4.setTextColor(0xFFFFFFFF);
					}
				}
			}
		}
	}

	public void _unmarkChoice(final double _cn) {
		if (_cn == 0) {
			ch1.setBackground(new GradientDrawable() {
				public GradientDrawable getIns(int a, int b, int c, int d) {
					this.setCornerRadius(a);
					this.setStroke(b, c);
					this.setColor(d);
					return this;
				}
			}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFFFFFFFF));
			ch1.setTextColor(0xFF006E85);
		} else {
			if (_cn == 1) {
				ch2.setBackground(new GradientDrawable() {
					public GradientDrawable getIns(int a, int b, int c, int d) {
						this.setCornerRadius(a);
						this.setStroke(b, c);
						this.setColor(d);
						return this;
					}
				}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFFFFFFFF));
				ch2.setTextColor(0xFF006E85);
			} else {
				if (_cn == 2) {
					ch3.setBackground(new GradientDrawable() {
						public GradientDrawable getIns(int a, int b, int c, int d) {
							this.setCornerRadius(a);
							this.setStroke(b, c);
							this.setColor(d);
							return this;
						}
					}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFFFFFFFF));
					ch3.setTextColor(0xFF006E85);
				} else {
					if (_cn == 3) {
						ch4.setBackground(new GradientDrawable() {
							public GradientDrawable getIns(int a, int b, int c, int d) {
								this.setCornerRadius(a);
								this.setStroke(b, c);
								this.setColor(d);
								return this;
							}
						}.getIns((int) 25, (int) 3, 0xFF006E85, 0xFFFFFFFF));
						ch4.setTextColor(0xFF006E85);
					}
				}
			}
		}
	}

	public String _readQouted(final String _line) {
		String qouted_text = "";
		try {
			qouted_text = _line.substring((int) (1), (int) (_line.length() - 3));

			if (qouted_text.charAt(qouted_text.length() - 1) == ',')
				return qouted_text.substring(0, (qouted_text.length() - 1));

			return qouted_text;

		} catch (StringIndexOutOfBoundsException e) {
			return "";
		}
	}

	public void _extra() {
	}

	public void readQuiz(final String _fname, final ArrayList<Question> _quiz, final ArrayList<Integer> _ci) {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getAssets().open(_fname)));

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

		for (int i = 0; i < line.size(); i += 5) {
			Question tempq = new Question();
			tempq.qt = _readQouted(line.get((int) (i)));
			for (int j = i + 1; j < i + 5; j++) {
				Answer tempa = new Answer();
				tempa.at = _readQouted(line.get((int) (j)));
				if (line.get((int) (j)).substring(line.get((int) (j)).length() - 1).equals("1")) {
					tempa.isTrue = true;
				}
				tempq.choice.add(tempa);
			}
			_quiz.add(tempq);
		}
		for (int i = 0; i < quiz.size(); i++) {
			for (int j = 0; j < 4; j++) {
				if (_quiz.get(i).choice.get(j).isTrue)
					_ci.add(j);
			}
		}
	}

	{
	}

	public void _setQuestion(final double _in) {
		qtext.setText(String.valueOf((long) (qcount)).concat(". ").concat(quiz.get((int) _in).qt));
		if (rAnswers) {
			ArrayList<Integer> aIndices = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
			int aIndex;
			if (chIndexSaved) {
				chIndexSaved = false;
			} else {
				ch_index.clear();
				for (int _repeat88 = 0; _repeat88 < (int) (4); _repeat88++) {
					aIndex = SketchwareUtil.getRandom((int) (0), (int) (aIndices.size() - 1));
					ch_index.add(aIndices.get(aIndex));
					aIndices.remove((int) (aIndex));
				}
			}
			ch1.setText(quiz.get((int) _in).choice.get(ch_index.get(0)).at);
			ch2.setText(quiz.get((int) _in).choice.get(ch_index.get(1)).at);
			ch3.setText(quiz.get((int) _in).choice.get(ch_index.get(2)).at);
			ch4.setText(quiz.get((int) _in).choice.get(ch_index.get(3)).at);
		} else {
			ch1.setText(quiz.get((int) _in).choice.get(0).at);
			ch2.setText(quiz.get((int) _in).choice.get(1).at);
			ch3.setText(quiz.get((int) _in).choice.get(2).at);
			ch4.setText(quiz.get((int) _in).choice.get(3).at);
		}
		// temporary soluation
		if(ch1.getText().toString().isEmpty())
			ch1.setVisibility(View.GONE);
		else
			ch1.setVisibility(View.VISIBLE);
		if(ch2.getText().toString().isEmpty())
			ch2.setVisibility(View.GONE);
		else
			ch2.setVisibility(View.VISIBLE);
		if(ch3.getText().toString().isEmpty())
			ch3.setVisibility(View.GONE);
		else
			ch3.setVisibility(View.VISIBLE);
		if(ch4.getText().toString().isEmpty())
			ch4.setVisibility(View.GONE);
		else
			ch4.setVisibility(View.VISIBLE);
	}

	public void _markTrueOrFalse(final double _cn, final boolean _isTrue) {
		if (_isTrue) {
			ca.setText("");
			if (_cn == 0) {
				ch1.setBackground(new GradientDrawable() {
					public GradientDrawable getIns(int a, int b) {
						this.setCornerRadius(a);
						this.setColor(b);
						return this;
					}
				}.getIns((int) 25, 0xFF388E3C));
			} else {
				if (_cn == 1) {
					ch2.setBackground(new GradientDrawable() {
						public GradientDrawable getIns(int a, int b) {
							this.setCornerRadius(a);
							this.setColor(b);
							return this;
						}
					}.getIns((int) 25, 0xFF388E3C));
				} else {
					if (_cn == 2) {
						ch3.setBackground(new GradientDrawable() {
							public GradientDrawable getIns(int a, int b) {
								this.setCornerRadius(a);
								this.setColor(b);
								return this;
							}
						}.getIns((int) 25, 0xFF388E3C));
					} else {
						if (_cn == 3) {
							ch4.setBackground(new GradientDrawable() {
								public GradientDrawable getIns(int a, int b) {
									this.setCornerRadius(a);
									this.setColor(b);
									return this;
								}
							}.getIns((int) 25, 0xFF388E3C));
						}
					}
				}
			}
		} else {
			ca.setPaintFlags(ca.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			ca.setText("Question Bank: " + String.valueOf((int) qindex + 1));
			if (_cn == 0) {
				ch1.setBackground(new GradientDrawable() {
					public GradientDrawable getIns(int a, int b) {
						this.setCornerRadius(a);
						this.setColor(b);
						return this;
					}
				}.getIns((int) 25, 0xFFDD2C00));
			} else {
				if (_cn == 1) {
					ch2.setBackground(new GradientDrawable() {
						public GradientDrawable getIns(int a, int b) {
							this.setCornerRadius(a);
							this.setColor(b);
							return this;
						}
					}.getIns((int) 25, 0xFFDD2C00));
				} else {
					if (_cn == 2) {
						ch3.setBackground(new GradientDrawable() {
							public GradientDrawable getIns(int a, int b) {
								this.setCornerRadius(a);
								this.setColor(b);
								return this;
							}
						}.getIns((int) 25, 0xFFDD2C00));
					} else {
						if (_cn == 3) {
							ch4.setBackground(new GradientDrawable() {
								public GradientDrawable getIns(int a, int b) {
									this.setCornerRadius(a);
									this.setColor(b);
									return this;
								}
							}.getIns((int) 25, 0xFFDD2C00));
						}
					}
				}
			}
			if (rAnswers) {
				if (ch_index.get(0) == correct_index.get((int) qindex)) {
					ch1.setBackground(new GradientDrawable() {
						public GradientDrawable getIns(int a, int b) {
							this.setCornerRadius(a);
							this.setColor(b);
							return this;
						}
					}.getIns((int) 25, 0xFF388E3C));
					ch1.setTextColor(0xFFFFFFFF);
				} else {
					if (ch_index.get(1) == correct_index.get((int) qindex)) {
						ch2.setBackground(new GradientDrawable() {
							public GradientDrawable getIns(int a, int b) {
								this.setCornerRadius(a);
								this.setColor(b);
								return this;
							}
						}.getIns((int) 25, 0xFF388E3C));
						ch2.setTextColor(0xFFFFFFFF);
					} else {
						if (ch_index.get(2) == correct_index.get((int) qindex)) {
							ch3.setBackground(new GradientDrawable() {
								public GradientDrawable getIns(int a, int b) {
									this.setCornerRadius(a);
									this.setColor(b);
									return this;
								}
							}.getIns((int) 25, 0xFF388E3C));
							ch3.setTextColor(0xFFFFFFFF);
						} else {
							if (ch_index.get(3) == correct_index.get((int) qindex)) {
								ch4.setBackground(new GradientDrawable() {
									public GradientDrawable getIns(int a, int b) {
										this.setCornerRadius(a);
										this.setColor(b);
										return this;
									}
								}.getIns((int) 25, 0xFF388E3C));
								ch4.setTextColor(0xFFFFFFFF);
							}
						}
					}
				}
			} else {
				if (correct_index.get((int) qindex) == 0) {
					ch1.setBackground(new GradientDrawable() {
						public GradientDrawable getIns(int a, int b) {
							this.setCornerRadius(a);
							this.setColor(b);
							return this;
						}
					}.getIns((int) 25, 0xFF388E3C));
					ch1.setTextColor(0xFFFFFFFF);
				} else {
					if (correct_index.get((int) qindex) == 1) {
						ch2.setBackground(new GradientDrawable() {
							public GradientDrawable getIns(int a, int b) {
								this.setCornerRadius(a);
								this.setColor(b);
								return this;
							}
						}.getIns((int) 25, 0xFF388E3C));
						ch2.setTextColor(0xFFFFFFFF);
					} else {
						if (correct_index.get((int) qindex) == 2) {
							ch3.setBackground(new GradientDrawable() {
								public GradientDrawable getIns(int a, int b) {
									this.setCornerRadius(a);
									this.setColor(b);
									return this;
								}
							}.getIns((int) 25, 0xFF388E3C));
							ch3.setTextColor(0xFFFFFFFF);
						} else {
							if (correct_index.get((int) qindex) == 3) {
								ch4.setBackground(new GradientDrawable() {
									public GradientDrawable getIns(int a, int b) {
										this.setCornerRadius(a);
										this.setColor(b);
										return this;
									}
								}.getIns((int) 25, 0xFF388E3C));
								ch4.setTextColor(0xFFFFFFFF);
							}
						}
					}
				}
			}
		}
	}

	public void _setUI(final double _ui_id) {
		if (_ui_id == 0) {
			more_img.setVisibility(View.GONE);
			sq_bg.setVisibility(View.VISIBLE);
			quiz_bg.setVisibility(View.GONE);
			grading_bg.setVisibility(View.GONE);
			sq.setBackground(new GradientDrawable() {
				public GradientDrawable getIns(int a, int b) {
					this.setCornerRadius(a);
					this.setColor(b);
					return this;
				}
			}.getIns((int) 90, 0xFF000000));
			title_tv.setText("Generate A Quiz");
			subtitle_tv.setText("Fill the following to start quiz");
			all_ch.setChecked(all);
			rand_ch.setChecked(rand);
			ra_ch.setChecked(rAnswers);
			if (!all && qquantity != 0) {
				qq.setText(String.valueOf((long) (qquantity)));
			}
			((EditText) qq).setSelection((int) qq.getText().toString().length(),
					(int) qq.getText().toString().length());
			qq.requestFocus();
			_setEditTextRange(qq, 1, 192);
			ui_id = 0;
		}
		if (_ui_id == 1) {
			more_img.setVisibility(View.VISIBLE);
			sq_bg.setVisibility(View.GONE);
			quiz_bg.setVisibility(View.VISIBLE);
			grading_bg.setVisibility(View.GONE);
			title_tv.setText("Practice Quiz");
			subtitle_tv.setText("Answer the following questions");
			_setBGColors();
			nt.setBackground(new GradientDrawable() {
				public GradientDrawable getIns(int a, int b) {
					this.setCornerRadius(a);
					this.setColor(b);
					return this;
				}
			}.getIns((int) 90, 0xFF000000));
			ui_id = 1;
		}
		if (_ui_id == 2) {
			more_img.setVisibility(View.GONE);
			sq_bg.setVisibility(View.GONE);
			quiz_bg.setVisibility(View.GONE);
			grading_bg.setVisibility(View.VISIBLE);
			mm.setBackground(new GradientDrawable() {
				public GradientDrawable getIns(int a, int b) {
					this.setCornerRadius(a);
					this.setColor(b);
					return this;
				}
			}.getIns((int) 90, 0xFF006E85));
			aq.setBackground(new GradientDrawable() {
				public GradientDrawable getIns(int a, int b) {
					this.setCornerRadius(a);
					this.setColor(b);
					return this;
				}
			}.getIns((int) 90, 0xFF000000));
			title_tv.setText("Quiz Done");
			subtitle_tv.setText("You've done taking practice quiz");
			ui_id = 2;
		}
	}

	public void _startQuiz(final double _quantity, final boolean _isRand) {
		if (_isRand) {
			indices.clear();
			for (int c = 0; c < quiz.size(); ++c) {
				indices.add(c);
			}
			arrIndex = SketchwareUtil.getRandom((int) (0), (int) (indices.size() - 1));
			qindex = indices.get((int) (arrIndex)).doubleValue();
		}
		qcount = 1;
		_setQuestion(qindex);
	}

	public void _quizGrading(final double _quantity) {
		_setUI(2);
		grading_text.setText("You've got ".concat(String.valueOf((long) (ccount))
				.concat(" correct answers out of ".concat(String.valueOf((long) (_quantity)).concat(" questions.")))));
		if (ccount >= (0.9 * _quantity)) {
			grading_text.setText(grading_text.getText().toString().concat("\n\nFantastic! You Are A Big Dafoor!"));
		} else {
			if (ccount >= (0.7 * _quantity)) {
				grading_text.setText(grading_text.getText().toString().concat("\n\nGreat job, keep going!"));
			} else {
				if (ccount >= (0.5 * _quantity)) {
					grading_text.setText(
							grading_text.getText().toString().concat("\n\nNot bad, you just need more practice!"));
				} else {
					grading_text.setText(
							grading_text.getText().toString().concat("\n\nNice try, study more and come back!"));
				}
			}
		}
	}

	public void _resetValues() {
		qindex = 0;
		ccount = 0;
		qcount = 0;
		ca.setText("");
		nt.setText("Confirm");
		isSelected = false;
		isConfirmed = false;
	}

	public void _setEditTextRange(final TextView _input, final double _min, final double _max) {
		_input.setInputType(InputType.TYPE_CLASS_NUMBER);
		_input.setFilters(new InputFilter[] { new InputFilterMinMax((int) _min, (int) _max),
				new InputFilter.LengthFilter(String.valueOf((int) _max).length()) });
		_input.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
	}

	public void _rippleRoundStroke(final View _view, final String _focus, final String _pressed, final double _round,
			final double _stroke, final String _strokeclr) {
		android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
		GG.setColor(Color.parseColor(_focus));
		GG.setCornerRadius((float) _round);
		GG.setStroke((int) _stroke, Color.parseColor("#" + _strokeclr.replace("#", "")));
		android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(
				new android.content.res.ColorStateList(new int[][] { new int[] {} },
						new int[] { Color.parseColor(_pressed) }),
				GG, null);
		_view.setBackground(RE);
	}

	public void _onBackDialog(final String _title, final String _desc, final String _btn1txt, final String _btn2txt,
			final String _mbc) {
		final AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this).create();
		View inflate = getLayoutInflater().inflate(R.layout.dialog, null);
		dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog1.setView(inflate);
		TextView t1 = (TextView) inflate.findViewById(R.id.t1);

		TextView t2 = (TextView) inflate.findViewById(R.id.t2);

		TextView b1 = (TextView) inflate.findViewById(R.id.b1);

		TextView b2 = (TextView) inflate.findViewById(R.id.b2);

		final CheckBox ch = (CheckBox) inflate.findViewById(R.id.ch);

		LinearLayout bg = (LinearLayout) inflate.findViewById(R.id.bg);
		t1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		t2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_light.ttf"), 0);
		b1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		b2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		t1.setText(_title);
		t2.setText(_desc);
		b1.setText(_btn1txt);
		b2.setText(_btn2txt);
		_rippleRoundStroke(bg, "#FFFFFF", "#000000", 15, 0, "#000000");
		_rippleRoundStroke(b1, "#F5F5F5", "#E0E0E0", 15, 0, "#000000");
		_rippleRoundStroke(b2, _mbc, "#40FFFFFF", 15, 0, "#000000");
		ch.setChecked(true);
		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog1.dismiss();
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog1.dismiss();
				if (ch.isChecked()) {
					toSave = true;
				} else {
					toSave = false;
				}
				isExit = false;
				finish();
			}
		});
		dialog1.setCancelable(true);
		dialog1.show();
	}

	public void _saveProgress() {
		quiz_progress.edit().putInt("qindex", (int) qindex).commit();
		quiz_progress.edit().putBoolean("isConfirmed", isConfirmed).commit();
		quiz_progress.edit().putBoolean("isSelected", isSelected).commit();
		quiz_progress.edit().putInt("indices_size", indices.size()).commit();
		quiz_progress.edit().putInt("arrIndex", (int) arrIndex).commit();
		if (rand) {
			for (int i = 0; i < indices.size(); i++)
				quiz_progress.edit().putInt("indices" + String.valueOf(i), indices.get(i)).commit();
		}
		if (rAnswers) {
			for (int i = 0; i < ch_index.size(); i++)
				quiz_progress.edit().putInt("ch_index" + String.valueOf(i), ch_index.get(i)).commit();
			quiz_progress.edit().putBoolean("chIndexSaved", true).commit();
		} else {
			quiz_progress.edit().putBoolean("chIndexSaved", false).commit();
		}
		quiz_progress.edit().putInt("qcount", (int) qcount).commit();
		quiz_progress.edit().putInt("ccount", (int) ccount).commit();
		if (isSelected) {
			quiz_progress.edit().putInt("ch_num", (int) ch_num).commit();
		}
	}

	public void _restoreProgress() {
		qindex = quiz_progress.getInt("qindex", 0);
		isConfirmed = quiz_progress.getBoolean("isConfirmed", false);
		isSelected = quiz_progress.getBoolean("isSelected", false);
		chIndexSaved = quiz_progress.getBoolean("chIndexSaved", false);
		if (rand) {
			for (int i = 0; i < quiz_progress.getInt("indices_size", 0); i++) {
				indices.add(quiz_progress.getInt("indices" + String.valueOf(i), 0));
			}
		}
		if (rAnswers) {
			for (int i = 0; i < 4; i++) {
				ch_index.add(quiz_progress.getInt("ch_index" + String.valueOf(i), 0));
			}
		}
		qcount = quiz_progress.getInt("qcount", 0);
		ccount = quiz_progress.getInt("ccount", 0);
		arrIndex = quiz_progress.getInt("arrIndex", 0);
		_setBGColors();
		_setQuestion(qindex);
		if (isSelected) {
			ch_num = quiz_progress.getInt("ch_num", 0);
			if (isConfirmed) {
				_markSelected(ch_num);
				if (rAnswers) {
					_markTrueOrFalse(ch_num, quiz.get((int) qindex).choice.get(ch_index.get((int) ch_num)).isTrue);
				} else {
					_markTrueOrFalse(ch_num, quiz.get((int) qindex).choice.get((int) ch_num).isTrue);
				}
				if (qcount == qquantity) {
					nt.setText("Result");
				} else {
					nt.setText("Next");
				}
			} else {
				_markSelected(ch_num);
			}
		} else {
			nt.setText("Confirm");
		}
	}

	public void _resetQuizDialog() {

		final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
		LayoutInflater inflater = getLayoutInflater();

		View convertView = (View) inflater.inflate(R.layout.reset_quiz, null);
		dialog.setView(convertView);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		TextView b1 = (TextView) convertView.findViewById(R.id.b1);
		TextView b2 = (TextView) convertView.findViewById(R.id.b2);
		TextView t1 = (TextView) convertView.findViewById(R.id.t1);
		TextView t2 = (TextView) convertView.findViewById(R.id.t2);
		TextView ht = (TextView) convertView.findViewById(R.id.ht);
		final CheckBox ch1 = (CheckBox) convertView.findViewById(R.id.ch1);
		final CheckBox ch2 = (CheckBox) convertView.findViewById(R.id.ch2);
		final CheckBox ch3 = (CheckBox) convertView.findViewById(R.id.ch3);
		final EditText et = (EditText) convertView.findViewById(R.id.et);
		LinearLayout bg = (LinearLayout) convertView.findViewById(R.id.bg);
		t1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		t2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_light.ttf"), 0);
		ht.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		et.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		ch1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		ch2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		ch3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		b1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		b2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		_rippleRoundStroke(bg, "#FFFFFF", "#000000", 15, 0, "#000000");
		_rippleRoundStroke(b1, "#F5F5F5", "#E0E0E0", 15, 0, "#000000");
		_rippleRoundStroke(b2, "#006E85", "#40FFFFFF", 15, 0, "#000000");
		_setEditTextRange(et, 1, 192);
		ch1.setChecked(all);
		ch2.setChecked(rand);
		ch3.setChecked(rAnswers);
		if (!all && qquantity != 0) {
			et.setText(String.valueOf((long) (qquantity)));
		}
		if (all) {
			et.setEnabled(false);
		}
		et.requestFocus();
		ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2) {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					et.setEnabled(false);
				} else {
					et.setEnabled(true);
					et.requestFocus();
				}
			}
		});
		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (ch1.isChecked() || !et.getText().toString().equals("")) {
					_setBGColors();
					_resetValues();
					all = ch1.isChecked();
					rand = ch2.isChecked();
					rAnswers = ch3.isChecked();
					if (all) {
						qquantity = quiz.size();
					} else {
						qquantity = Double.parseDouble(et.getText().toString());
					}
					_startQuiz(qquantity, ch2.isChecked());
					KeyboardUtils.hideKeyboard(MainActivity.this);
					SketchwareUtil.showMessage(getApplicationContext(), "Quiz Reseted");
					dialog.dismiss();
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Please, input questions quantity");
				}
			}
		});
		dialog.show();
	}

	public void _saveUserPrefs() {
		quiz_progress.edit().putInt("qquantity", (int) qquantity).commit();
		quiz_progress.edit().putBoolean("all", all).commit();
		quiz_progress.edit().putBoolean("rand", rand).commit();
		quiz_progress.edit().putBoolean("rAnswers", rAnswers).commit();
	}

	public void _restoreUserPrefs() {
		qquantity = quiz_progress.getInt("qquantity", 0);
		all = quiz_progress.getBoolean("all", false);
		rand_ch.setChecked(all);
		rand = quiz_progress.getBoolean("rand", false);
		rand_ch.setChecked(rand);
		rAnswers = quiz_progress.getBoolean("rAnswers", false);
		rand_ch.setChecked(rAnswers);
		if (!all && qquantity != 0) {
			qq.setText(String.valueOf((long) (qquantity)));
		}
	}

	public void _progressDialog() {
		final AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this).create();
		View inflate = getLayoutInflater().inflate(R.layout.progress_dialog, null);
		dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog1.setView(inflate);
		TextView t1 = (TextView) inflate.findViewById(R.id.t1);

		TextView t2 = (TextView) inflate.findViewById(R.id.t2);

		TextView b1 = (TextView) inflate.findViewById(R.id.b1);

		TextView b2 = (TextView) inflate.findViewById(R.id.b2);

		LinearLayout bg = (LinearLayout) inflate.findViewById(R.id.bg);
		t1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		t2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_light.ttf"), 0);
		b1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		b2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/en_medium.ttf"), 0);
		_rippleRoundStroke(bg, "#FFFFFF", "#000000", 15, 0, "#000000");
		_rippleRoundStroke(b1, "#F5F5F5", "#E0E0E0", 15, 0, "#000000");
		_rippleRoundStroke(b2, "#006E85", "#40FFFFFF", 15, 0, "#000000");
		onReDia = true;
		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog1.dismiss();
				onReDia = false;
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog1.dismiss();
				_setUI(1);
				_restoreProgress();
				onReDia = false;
			}
		});
		dialog1.setCancelable(false);
		dialog1.show();
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
				_result.add((double) _arr.keyAt(_iIdx));
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
