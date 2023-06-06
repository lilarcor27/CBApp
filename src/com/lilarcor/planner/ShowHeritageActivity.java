package com.lilarcor.planner;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class ShowHeritageActivity extends Activity implements OnClickListener
{

//============================| Global Variables |=========================

	private TextView[] TextV = null;
	private ImageView[] ImageV = null;
	private ImageButton[] IButtonV = null;
	private ImageView[] summaryImageV = null;
	private Button[] summaryButtonV = null;
	private ImageButton[] summaryIButtonV = null;
	
	private HeritageAdministrator hA = null;
	
//============================| Events |===================================

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heritage);
		
		setViews();
		
		hA = new HeritageAdministrator(this, handler);
		hA.loadHeritageData();
	}	
	
//-------------------------------------------------------------------------
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { // return super.onKeyDown(keyCode, event);
		if(keyCode != KeyEvent.KEYCODE_BACK) return super.onKeyDown(keyCode, event);
		return super.onKeyDown(keyCode, event);
	}
			
//-------------------------------------------------------------------------

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.heritageimagebutton00:
			startActivity(new Intent(this, MoreImageActivity.class));
			break;
			
		case R.id.heritagesummarybutton00:
		case R.id.heritagesummaryimagebutton00:
		
			TextV[1].setVisibility((TextV[1].getVisibility() == View.GONE ? View.VISIBLE : View.GONE));
			summaryIButtonV[0].setImageResource((TextV[1].getVisibility() == View.GONE ? R.drawable.direction_down : R.drawable.direction_up));
			
			break;
			
		case R.id.heritagesummarybutton01:
		case R.id.heritagesummaryimagebutton01:
			
			TextV[2].setVisibility((TextV[2].getVisibility() == View.GONE ? View.VISIBLE : View.GONE));
			summaryIButtonV[1].setImageResource((TextV[2].getVisibility() == View.GONE ? R.drawable.direction_down : R.drawable.direction_up));
			
			break;
			
		case R.id.heritagesummarybutton02:
		case R.id.heritagesummaryimagebutton02:
			NearAttractionsActivity.AttractionType = 1;
			startActivity(new Intent(this, NearAttractionsActivity.class));
			break;
			
		case R.id.heritagesummarybutton03:
		case R.id.heritagesummaryimagebutton03:
			NearAttractionsActivity.AttractionType = 2;
			startActivity(new Intent(this, NearAttractionsActivity.class));
			break;
			
		default:
			break;
		}
	}


//============================| Methods |==================================

	private void setViews() {
		TextV = new TextView[3];
		ImageV = new ImageView[1];
		IButtonV = new ImageButton[1];
		summaryButtonV = new Button[4];
		summaryIButtonV = new ImageButton[4];
		
		TextV[0] = (TextView)findViewById(R.id.heritagetext00);
		TextV[1] = (TextView)findViewById(R.id.heritagetext01);
		TextV[2] = (TextView)findViewById(R.id.heritagetext02);
		
		ImageV[0] = (ImageView)findViewById(R.id.heritageimage00);
		
		IButtonV[0] = (ImageButton)findViewById(R.id.heritageimagebutton00);
		
		summaryButtonV[0] = (Button)findViewById(R.id.heritagesummarybutton00);
		summaryButtonV[1] = (Button)findViewById(R.id.heritagesummarybutton01);
		summaryButtonV[2] = (Button)findViewById(R.id.heritagesummarybutton02);
		summaryButtonV[3] = (Button)findViewById(R.id.heritagesummarybutton03);
		
		summaryIButtonV[0] = (ImageButton)findViewById(R.id.heritagesummaryimagebutton00);
		summaryIButtonV[1] = (ImageButton)findViewById(R.id.heritagesummaryimagebutton01);
		summaryIButtonV[2] = (ImageButton)findViewById(R.id.heritagesummaryimagebutton02);
		summaryIButtonV[3] = (ImageButton)findViewById(R.id.heritagesummaryimagebutton03);
		
		IButtonV[0].setOnClickListener(this);
		
		summaryButtonV[0].setOnClickListener(this);
		summaryButtonV[1].setOnClickListener(this);
		summaryButtonV[2].setOnClickListener(this);
		summaryButtonV[3].setOnClickListener(this);		
		
		summaryIButtonV[0].setOnClickListener(this);
		summaryIButtonV[1].setOnClickListener(this);
		summaryIButtonV[2].setOnClickListener(this);
		summaryIButtonV[3].setOnClickListener(this);		

		return;
	}
	
//-------------------------------------------------------------------------

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ImageV[0].setImageBitmap(HeritageAdministrator.getBitmapData());
			TextV[0].setText(HeritageAdministrator.getTextData(0));
			TextV[1].setText(HeritageAdministrator.getTextData(1));
			TextV[2].setText(HeritageAdministrator.getTextData(2));
		}
	};
}
//<!-- heritageimage00 heritagesummarytext00-02 heritagesummaryimage00-02 heritagetext00-02 -->
/*
[sw]
	1:	청주
	2:	청원
	3:	진천
	4:	증평
	5:	음성
	6:	충주
	7:	괴산
	8:	제천
	9:	단양
	10:	옥천
	11:	영동
	12:	보은
[/sw] 
*/
//		TextV[0].setText(new StringBuilder("today: ").append(City[MainActivity.CityKey]).toString());
//		TextV[2].setText("asd"); // new StringBuilder("fA.isVersionCorrcts() = ").append(fA.isVersionCorrects()).toString());
/*
	private int getRandomDate(int div) {
		int total = 0;
		
		Calendar c = Calendar.getInstance();
		
		total += (c.get(Calendar.MONTH) + c.get(Calendar.YEAR) + c.get(Calendar.DAY_OF_MONTH));
				
//		DEBUG: Toast.makeText(this, new StringBuilder("Value1: ").append(c.get(Calendar.MONTH)).append(" Value2: ").append(c.get(Calendar.YEAR)).append(" Value3: ").append(c.get(Calendar.DAY_OF_MONTH)).toString(), Toast.LENGTH_SHORT).show(); 
//		DEBUG: Toast.makeText(this, new StringBuilder("Value1: " + total).append(" Value2: ").append(total % div).toString(), Toast.LENGTH_SHORT).show(); 
		return total % div;
	}	

	android:adjustViewBounds="true"
	android:scaleType="fitXY"
	
*/
/*
Intent intent = new Intent();
intent.setAction(Intent.ACTION_WEB_SEARCH);
intent.putExtra(SearchManager.QUERY,"searchString")
startActivity(intent);
*/