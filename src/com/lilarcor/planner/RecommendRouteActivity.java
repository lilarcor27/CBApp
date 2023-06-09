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

public class RecommendRouteActivity extends Activity implements OnClickListener
{

//============================| Global Variables |=========================

	private TextView[] TextV = null;
	private ImageView[] ImageV = null;
	private Button[] ButtonV = null;
	private myHandler handler = null;
	private HeritageAdministrator hA = null;
	
	public static int targetCity = -1;

	public static int[] EachKeys = new int[3];
	
//============================| Events |===================================

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommend);
		
		setViews();
		setEachKeys();
		
		hA.indexKey = EachKeys[0];
		hA.loadHeritageData();
		
		FileAdministrator.writeLog("isOnline: " + FileAdministrator.isOnline(this));	
	}	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {	
		case R.id.recommendbutton00:
			HeritageAdministrator.indexKey = EachKeys[0];
			startActivity(new Intent(this, ShowHeritageActivity.class));
			break;
			
		case R.id.recommendbutton01:
			HeritageAdministrator.indexKey = EachKeys[1];
			startActivity(new Intent(this, ShowHeritageActivity.class));
			break;
			
		case R.id.recommendbutton02:
			HeritageAdministrator.indexKey = EachKeys[2];
			startActivity(new Intent(this, ShowHeritageActivity.class));
			break;
			
		default:
		}
	}
	
//-------------------------------------------------------------------------
	
	private void setViews()	{
		TextV = new TextView[6];
		ImageV = new ImageView[3];
		ButtonV = new Button[3];
		
		TextV[0] = (TextView)findViewById(R.id.recommendtext00);
		TextV[1] = (TextView)findViewById(R.id.recommendtext01);
		TextV[2] = (TextView)findViewById(R.id.recommendtext02);
		TextV[3] = (TextView)findViewById(R.id.recommendtext00_1);
		TextV[4] = (TextView)findViewById(R.id.recommendtext01_1);
		TextV[5] = (TextView)findViewById(R.id.recommendtext02_1);
						
		
		ImageV[0] = (ImageView)findViewById(R.id.recommendimage00);
		ImageV[1] = (ImageView)findViewById(R.id.recommendimage01);
		ImageV[2] = (ImageView)findViewById(R.id.recommendimage02);
		
		ButtonV[0] = (Button)findViewById(R.id.recommendbutton00);
		ButtonV[1] = (Button)findViewById(R.id.recommendbutton01);
		ButtonV[2] = (Button)findViewById(R.id.recommendbutton02);
		
		ButtonV[0].setOnClickListener(this);
		ButtonV[1].setOnClickListener(this);
		ButtonV[2].setOnClickListener(this);
		
		handler = new myHandler();
		hA = new HeritageAdministrator(this, handler);

		//(hA.tourKeys[RecommendRouteActivity.targetCity][MainActivity.CityKey % hA.tourKeys[RecommendRouteActivity.targetCity].length])
	
		for(int i = 0; i < 3; i++) {
			FileAdministrator.writeLog("eachkeys" + EachKeys[i]);
		}
		
		return;
	}
	
	private void setEachKeys() {
		MyRandom.mysrand(MainActivity.CityKey);
		for(int i = 0; i < 3; i++) 
			RecommendRouteActivity.EachKeys[i] = (HeritageAdministrator.tourKeys[RecommendRouteActivity.targetCity][MyRandom.myRandom() % HeritageAdministrator.tourKeys[RecommendRouteActivity.targetCity].length]);

		for(int i = 0, Count = 4; Count != 0; i++, Count--) {
			if(RecommendRouteActivity.EachKeys[i % 3] == RecommendRouteActivity.EachKeys[(i + 1) % 3]) {
				RecommendRouteActivity.EachKeys[i % 3] = (HeritageAdministrator.tourKeys[RecommendRouteActivity.targetCity][MyRandom.myRandom() % HeritageAdministrator.tourKeys[RecommendRouteActivity.targetCity].length]);
				Count = 4;
			}
		}
	}
	
	private void setScroll() {
		final HorizontalScrollView hsv = (HorizontalScrollView)findViewById(R.id.recommendhscroll00);
		
		hsv.post(new Runnable() {
			@Override
			public void run() {
				hsv.smoothScrollBy(450, 0);
			} 
		});  
		return;
	}
		

	class myHandler extends Handler
	{
		int Count = 0;
		
		public void handleMessage(Message msg) {
			TextV[Count].setText(hA.getTextData(0));
			TextV[Count + 3].setText(hA.getTextData(1));
			ImageV[Count].setImageBitmap(hA.getBitmapData());
			
 	    	Count++;
	     	if(Count < 3) {
				hA.indexKey = EachKeys[Count];
				hA.loadHeritageData(); 
			} else {
				setScroll();
			}
		}
	}
}
		
		
