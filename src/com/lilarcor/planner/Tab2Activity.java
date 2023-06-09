package com.lilarcor.planner; 

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class Tab2Activity extends Activity
{
	private Button[] ButtonV = null;
	private ImageButton[] IButtonV = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabpage2);
		
		setViews();
	}
	
	private void setViews() {
		ButtonV = new Button[13];
		IButtonV = new ImageButton[1];
		
		myOnClickListener onClickListener = new myOnClickListener();
		
		ButtonV[0] = (Button)findViewById(R.id.tab2button00);
		ButtonV[1] = (Button)findViewById(R.id.tab2button01);
		ButtonV[2] = (Button)findViewById(R.id.tab2button02);
		ButtonV[3] = (Button)findViewById(R.id.tab2button03);
		ButtonV[4] = (Button)findViewById(R.id.tab2button04);
		ButtonV[5] = (Button)findViewById(R.id.tab2button05);
		ButtonV[6] = (Button)findViewById(R.id.tab2button06);
		ButtonV[7] = (Button)findViewById(R.id.tab2button07);
		ButtonV[8] = (Button)findViewById(R.id.tab2button08);
		ButtonV[9] = (Button)findViewById(R.id.tab2button09);
		ButtonV[10] = (Button)findViewById(R.id.tab2button10);
		ButtonV[11] = (Button)findViewById(R.id.tab2button11);
		ButtonV[12] = (Button)findViewById(R.id.tab2button12);
		
		IButtonV[0] = (ImageButton)findViewById(R.id.tab2imagebutton00);
		
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
		ButtonV[12].setOnClickListener(onClickListener);
		
		IButtonV[0].setOnClickListener(onClickListener);
		
		return;
	}
	
	class myOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			String urlString = null;
			
			switch(v.getId()) {
				case R.id.tab2button00:
					urlString = "http://www.cjcity.net/";
					break;
				
				case R.id.tab2button01:
					urlString = "http://www.puru.net/";
					break;				
				
				case R.id.tab2button02:
					urlString = "http://www.jincheon.go.kr/";
					break;					
				
				case R.id.tab2button03:
					urlString = "http://www.jp.go.kr/";
					break;					
				
				case R.id.tab2button04:
					urlString = "http://www.es21.go.kr/";
					break;					
				
				case R.id.tab2button05:
					urlString = "http://www.cj100.net/";
					break;				
				
				case R.id.tab2button06:
					urlString = "http://www.goesan.go.kr/";
					break;					
				
				case R.id.tab2button07:
					urlString = "http://www.okjc.net/";
					break;						
				
				case R.id.tab2button08:
					urlString = "http://www.dy21.net/";
					break;				
				
				case R.id.tab2button09:
					urlString = "http://www.oc.go.kr/";
					break;					
				
				case R.id.tab2button10:
					urlString = "http://www.yd21.go.kr/";
					break;					
				
				case R.id.tab2button11:
					urlString = "http://www.boeun.go.kr/";
					break;		

				case R.id.tab2button12:
				case R.id.tab2imagebutton00:
					urlString = "http://www.chungbuknadri.net/";
					break;
					
				default:
					break;
			}
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
			startActivity(myIntent);
		}
	}
}
