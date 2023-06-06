package com.lilarcor.planner;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class Tab1Activity extends Activity
{
	private EditText[] ETextV = null;
	private Button[] ButtonV = null;
	private TextView[] TextV = null;
	private ImageButton[] IButtonV = null;
	private	HeritageResult[] hResult = null;
	
	private ProgressDialog pDialog = null;
	
	private webDataProcessor wdp = null;
	
	private int resultIndex = 0;
	private	int Count = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabpage1);
		
		FileAdministrator.writeLog("Tab1Activity Load!");
		
		setViews();
	}
	
	private void setViews() {
		ETextV = new EditText[1];
		ButtonV = new Button[5];
		TextV = new TextView[1];
		IButtonV = new ImageButton[3];
	
		myOnClickListener clickListener = new myOnClickListener(this, this);
		myOnKeyListener keyListener = new myOnKeyListener(this, this);
		
		ETextV[0] = (EditText)findViewById(R.id.tab1edittext00);
		
		ButtonV[0] = (Button)findViewById(R.id.tab1button00);
		ButtonV[1] = (Button)findViewById(R.id.tab1button01);
		ButtonV[2] = (Button)findViewById(R.id.tab1button02);
		ButtonV[3] = (Button)findViewById(R.id.tab1button03);
		ButtonV[4] = (Button)findViewById(R.id.tab1button04);
		
		TextV[0] = (TextView)findViewById(R.id.tab1text00);
		
		IButtonV[0] = (ImageButton)findViewById(R.id.tab1imagebutton00);
		IButtonV[1] = (ImageButton)findViewById(R.id.tab1imagebutton01);
		IButtonV[2] = (ImageButton)findViewById(R.id.tab1imagebutton02);
		
		ETextV[0].setOnKeyListener(keyListener);
		
		ButtonV[0].setOnClickListener(clickListener);
		ButtonV[1].setOnClickListener(clickListener);
		ButtonV[2].setOnClickListener(clickListener);
		ButtonV[3].setOnClickListener(clickListener);
		ButtonV[4].setOnClickListener(clickListener);
		
		IButtonV[0].setOnClickListener(clickListener);
		IButtonV[1].setOnClickListener(clickListener);
		IButtonV[2].setOnClickListener(clickListener);
		
		hResult = new HeritageResult[200];
	}
	
	private void updateList() {
		int forTargetInteger = 0;
		TextV[0].setText((resultIndex + 1) + " / " + ((Count / 5) + 1));
		if(Count - (resultIndex * 5) < 5)
			forTargetInteger = Count - (resultIndex * 5);
		else 
			forTargetInteger = 5;
			
		for(int i = 0; i < 5; i++) {
			ButtonV[i].setText(hResult[i + (resultIndex * 5)].getHeritageName());
			if(i >= forTargetInteger)
				ButtonV[i].setVisibility(View.INVISIBLE);
			else 
				ButtonV[i].setVisibility(View.VISIBLE);
		}
		return;
	}
	
//============================| Thread & Handler |=========================

	private class webDataProcessor extends AsyncTask<Void, Integer, String> {    
		String searchString = null;
		Activity activity = null;

		private webDataProcessor(Activity activity) {
			this.activity = activity;
		}
		
		@Override
		protected void onPreExecute() { // doInBackground 작업 전
			super.onPreExecute();
			Count = 0;
			
			for(int i = 0; i < 200; i++) {
				hResult[i] = new HeritageResult();
			}
			searchString = ETextV[0].getText().toString();
			
			pDialog = new ProgressDialog(activity);
			pDialog.setTitle("Loading");
			pDialog.setMessage("웹 페이지 소스 로딩 중");
			pDialog.setCancelable(false);
			pDialog.show();
		}

   	    @Override
		protected String doInBackground(Void... params) {
			String pasteString = "";
 			try {
				String urlString = new StringBuilder("http://chungbuknadri.net/www/search/search.do?searchWord=")
					.append(URLEncoder.encode(searchString, "utf-8"))
					.append("&searchType=sec_travel")
					.toString();
					FileAdministrator.writeLog("urlString: " + urlString);
				InputStream is = new URL(urlString).openStream();		
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				
				String line = null;
				while((line = br.readLine()) != null) { 
					if((line.indexOf(searchString) != -1) && (line.indexOf("<td class=\"subject\">") != -1)) {
						String tourKeyString = line.substring(line.indexOf("tourKey=") + 8, line.indexOf("target=") - 2);
						hResult[Count].setTourKey(Integer.parseInt(tourKeyString));
						hResult[Count].setHeritageName(line.substring(line.indexOf("target=\"_top\">") + 14, line.indexOf("</a>")));
						Count++;
//						pasteString += line.substring(line.indexOf("target=\"_top\">") + 14, line.indexOf("</a>"));
//						pasteString += '\n';
					}
				}
			} catch(Exception e) {
				FileAdministrator.writeExceptionLog(e, "in Tab1Act, doInBackground");
			} 
			return pasteString;
		}

		@Override
		protected void onProgressUpdate(Integer[] values) { // doInBackground 에서 publishProgress 로 사용한 값, Thread&Handler 에서 핸들러 역할을 한다.
		}

		@Override
		protected void onPostExecute(String result) { // doInBackground 의 리턴값, 즉 doInBackground 의 끝.
/**/FileAdministrator.writeLog("Tab1Activity webDataProcessor onPostExecute Call");
			updateList();

			pDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();	
		}  
	}
	
	class myOnClickListener implements OnClickListener
	{
		Activity activity = null;
		Context context = null;
		public myOnClickListener(Activity activity, Context context) {
			this.activity = activity;
			this.context = context;
		}
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.tab1button00:
				HeritageAdministrator.indexKey = hResult[(resultIndex * 5) + 0].getTourKey();
				startActivity(new Intent(activity, ShowHeritageActivity.class));
				break;
			case R.id.tab1button01:
				HeritageAdministrator.indexKey = hResult[(resultIndex * 5) + 1].getTourKey();
				startActivity(new Intent(activity, ShowHeritageActivity.class));
				break;
			case R.id.tab1button02:
				HeritageAdministrator.indexKey = hResult[(resultIndex * 5) + 2].getTourKey();
				startActivity(new Intent(activity, ShowHeritageActivity.class));
				break;
			case R.id.tab1button03:
				HeritageAdministrator.indexKey = hResult[(resultIndex * 5) + 3].getTourKey();
				startActivity(new Intent(activity, ShowHeritageActivity.class));
				break;
			case R.id.tab1button04:
				HeritageAdministrator.indexKey = hResult[(resultIndex * 5) + 4].getTourKey();
				startActivity(new Intent(activity, ShowHeritageActivity.class));
				break;
			
 			case R.id.tab1imagebutton00:
				if(resultIndex == 0)
					break;
				resultIndex--;
				updateList();
				break;
			case R.id.tab1imagebutton01:
				if(Count / 5 == resultIndex)
					break;
				resultIndex++;
				updateList();
				break;
				
			case R.id.tab1imagebutton02:
				if((ETextV[0].getText().toString().length()) < 1) {
					Toast.makeText(context, "반드시 한 글자 이상 입력하셔야 합니다.", 0).show();
				} else {
					if(FileAdministrator.isOnline(activity) == false) {
						Toast.makeText(context, "데이터 네트워크 / WIFI 연결이 필요합니다.", 0).show();
					} else {
						wdp = new webDataProcessor(activity);
						wdp.execute();	
					}
				}
				break;
			default:
				break;
			}
		}
	}
	
	class myOnKeyListener implements OnKeyListener
	{
		Activity activity = null;
		Context context = null;
		public myOnKeyListener(Activity activity, Context context) {
			this.activity = activity;
			this.context = context;
		}
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
 			if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
				EditText tmp = (EditText)v;
				if((tmp.getText().toString().length()) < 1) {
					Toast.makeText(context, "반드시 한 글자 이상 입력하셔야 합니다.", 0).show();
				} else {
					if(FileAdministrator.isOnline(activity) == false) {
						Toast.makeText(context, "데이터 네트워크 / WIFI 연결이 필요합니다.", 0).show();
					} else {
						wdp = new webDataProcessor(activity);
						wdp.execute();	
					}
					return true;
				}
			} 
			return false;
		}
	}
	
	class HeritageResult
	{
		private int tourKey = -1;
		private String heritageName = null;
		
		public HeritageResult() { 
		}
		
		public void setTourKey(int tourKey) {
			this.tourKey = tourKey;
		}
		
		public void setHeritageName(String heritageName) {
			this.heritageName = heritageName;
		}
		
		public int getTourKey() {
			return tourKey;
		}
		
		public String getHeritageName() {
			return heritageName;
		}
	}
		
}