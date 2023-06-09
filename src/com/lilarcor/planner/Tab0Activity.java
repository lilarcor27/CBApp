package com.lilarcor.planner; 

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.text.*;
import android.text.style.*;
import java.util.*;
import java.lang.*;

public class Tab0Activity extends Activity
{

//============================| Global Variables |=========================

	private ImageButton[] IButtonV = null;
	private Button[] ButtonV = null;
	
//============================| Events |===================================

	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabpage0);
		
		setViews();
//		setScroll();
	}
		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { // return super.onKeyDown(keyCode, event);
		if(keyCode != KeyEvent.KEYCODE_BACK) return super.onKeyDown(keyCode, event);
		System.exit(0);
		return true;
	}
	
//-------------------------------------------------------------------------	

	private void setViews()
	{
		IButtonV = new ImageButton[1];
		ButtonV = new Button[12];
		
		myOnClickListener onClickListener = new myOnClickListener(this, this);
		
		IButtonV[0] = (ImageButton)findViewById(R.id.tab0imagebutton00);
		
		ButtonV[0] = (Button)findViewById(R.id.tab0button00);
		ButtonV[1] = (Button)findViewById(R.id.tab0button01);
		ButtonV[2] = (Button)findViewById(R.id.tab0button02);
		ButtonV[3] = (Button)findViewById(R.id.tab0button03);
		ButtonV[4] = (Button)findViewById(R.id.tab0button04);
		ButtonV[5] = (Button)findViewById(R.id.tab0button05);
		ButtonV[6] = (Button)findViewById(R.id.tab0button06);
		ButtonV[7] = (Button)findViewById(R.id.tab0button07);
		ButtonV[8] = (Button)findViewById(R.id.tab0button08);
		ButtonV[9] = (Button)findViewById(R.id.tab0button09);
		ButtonV[10] = (Button)findViewById(R.id.tab0button10);
		ButtonV[11] = (Button)findViewById(R.id.tab0button11);
		
		IButtonV[0].setOnClickListener(onClickListener);
		
		ButtonV[0].setOnClickListener(onClickListener);
		ButtonV[1].setOnClickListener(onClickListener);
		ButtonV[2].setOnClickListener(onClickListener);
		ButtonV[3].setOnClickListener(onClickListener);
		ButtonV[4].setOnClickListener(onClickListener);
		ButtonV[5].setOnClickListener(onClickListener);
		ButtonV[6].setOnClickListener(onClickListener);
		ButtonV[7].setOnClickListener(onClickListener);
		ButtonV[8].setOnClickListener(onClickListener);
		ButtonV[9].setOnClickListener(onClickListener);
		ButtonV[10].setOnClickListener(onClickListener);
		ButtonV[11].setOnClickListener(onClickListener);
		
	}
	
//-------------------------------------------------------------------------	

	private void setScroll() {
		final HorizontalScrollView hsv = (HorizontalScrollView)findViewById(R.id.tab0hscroll00);
		
		hsv.postDelayed(new Runnable() {
			@Override
			public void run() {
				hsv.smoothScrollBy(375, 0);
			} 
		}, 100);  
		return;
	}
	
//-------------------------------------------------------------------------		
	
	class myOnClickListener implements OnClickListener
	{
		Context context;
		Activity activity;
		public myOnClickListener(Context context, Activity activity) {
			this.context = context;
			this.activity = activity;
		}
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
				case R.id.tab0button00:
					RecommendRouteActivity.targetCity = 0;
					IButtonV[0].setImageResource(R.drawable.terrain_0);
					break;
				case R.id.tab0button01:
					RecommendRouteActivity.targetCity = 1;
					IButtonV[0].setImageResource(R.drawable.terrain_1);
					break;
				case R.id.tab0button02:
					RecommendRouteActivity.targetCity = 2;
					IButtonV[0].setImageResource(R.drawable.terrain_2);
					break;
				case R.id.tab0button03:
					RecommendRouteActivity.targetCity = 3;
					IButtonV[0].setImageResource(R.drawable.terrain_3);
					break;
				case R.id.tab0button04:
					RecommendRouteActivity.targetCity = 4;
					IButtonV[0].setImageResource(R.drawable.terrain_4);
					break;
				case R.id.tab0button05:
					RecommendRouteActivity.targetCity = 5;
					IButtonV[0].setImageResource(R.drawable.terrain_5);
					break;
				case R.id.tab0button06:
					RecommendRouteActivity.targetCity = 6;
					IButtonV[0].setImageResource(R.drawable.terrain_6);
					break;
				case R.id.tab0button07:
					RecommendRouteActivity.targetCity = 7; 
					IButtonV[0].setImageResource(R.drawable.terrain_7);
					break;
				case R.id.tab0button08:
					RecommendRouteActivity.targetCity = 8;
					IButtonV[0].setImageResource(R.drawable.terrain_8);
					break;
				case R.id.tab0button09:
					RecommendRouteActivity.targetCity = 9;
					IButtonV[0].setImageResource(R.drawable.terrain_9);
					break;
				case R.id.tab0button10:
					RecommendRouteActivity.targetCity = 10;
					IButtonV[0].setImageResource(R.drawable.terrain_10);
					break;
				case R.id.tab0button11:
					RecommendRouteActivity.targetCity = 11;
					IButtonV[0].setImageResource(R.drawable.terrain_11);
					break;
				
				case R.id.tab0imagebutton00:
				if(RecommendRouteActivity.targetCity == -1) {
					Toast.makeText(context, "지역을 선택하십시오.", 0).show();
					return;
				}
				if(FileAdministrator.isOnline(activity) == false)
					Toast.makeText(context, "데이터 네트워크 / WIFI 연결이 필요합니다.", 0).show();
				else 
					startActivity(new Intent(getApplicationContext(), RecommendRouteActivity.class));
					
				default: break;
			}
			return;
		}
	}
}
/*	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			TextV[0].setText(hA.getTextData(0));
		}
	};
*/
/*
AlertDialog.Builder alert = new AlertDialog.Builder(MyActivity.this);
alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
*/
/*		
		final SpannableStringBuilder sps = new SpannableStringBuilder("아래 지도를 터치하세요.");
		sps.setSpan(new AbsoluteSizeSpan(50), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sps.setSpan(new ForegroundColorSpan(0xff40a940), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		TextV[0].append(sps);

		TextV[1].setText("위 이미지를 클릭하세요." + "CityKey = " + MainActivity.CityKey % 12 + '\n' + "CityName = " + MainActivity.CityName[MainActivity.CityKey % 12]);
*/

