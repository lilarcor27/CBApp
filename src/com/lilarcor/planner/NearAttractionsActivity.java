package com.lilarcor.planner; 

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.text.*;
import android.net.*;
import android.text.style.*;
import android.graphics.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class NearAttractionsActivity extends Activity 
{

//============================| Global Variables |=========================

	private TextView[] TextV = null;
	private ImageView[] ImageV = null;
	private Button[] ButtonV = null;
	
	public static int AttractionType = -1;
	public static String[] AttractionsURL = null;
	
	private webDataProcessor wdp = null;

	
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.near);
		
		FileAdministrator.writeLog("NearAtt.. Act 호출 성공");
		
		setViews();
		
		wdp.execute();
	}
	
	private void setViews() {
		TextV = new TextView[6];
		ImageV = new ImageView[3];
		ButtonV = new Button[3];
		
		AttractionsURL = new String[3];		
		
		myOnClickListener onClickListener = new myOnClickListener(this);

		TextV[0] = (TextView)findViewById(R.id.neartext00);
		TextV[1] = (TextView)findViewById(R.id.neartext01);
		TextV[2] = (TextView)findViewById(R.id.neartext02);
		TextV[3] = (TextView)findViewById(R.id.neartext03);
		TextV[4] = (TextView)findViewById(R.id.neartext04);
		TextV[5] = (TextView)findViewById(R.id.neartext05);
		
		ImageV[0] = (ImageView)findViewById(R.id.nearimage00);
		ImageV[1] = (ImageView)findViewById(R.id.nearimage01);
		ImageV[2] = (ImageView)findViewById(R.id.nearimage02);
		
		ButtonV[0] = (Button)findViewById(R.id.nearbutton00);
		ButtonV[1] = (Button)findViewById(R.id.nearbutton01);
		ButtonV[2] = (Button)findViewById(R.id.nearbutton02);
		
		ButtonV[0].setOnClickListener(onClickListener);
		ButtonV[1].setOnClickListener(onClickListener);
		ButtonV[2].setOnClickListener(onClickListener);
		
		wdp = new webDataProcessor(this, this);
		
		return;
	}
	
	class myOnClickListener implements OnClickListener
	{
		Activity activity;
		public myOnClickListener(Activity activity) {
			this.activity = activity;
		}
		
		
		@Override
		public void onClick(View v) {
			Intent myIntent = null;
			switch(v.getId()) {
				case R.id.nearbutton00:
					myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NearAttractionsActivity.AttractionsURL[0]));
					startActivity(myIntent);
					break;
					
				case R.id.nearbutton01:	
					myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NearAttractionsActivity.AttractionsURL[1]));
					startActivity(myIntent);
					break;
					
				case R.id.nearbutton02:	
					myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NearAttractionsActivity.AttractionsURL[2]));
					startActivity(myIntent);
					break;
			}
		}
	}
				
//============================| Thread & Handler |=========================

	private class webDataProcessor extends AsyncTask<Void, Integer, String> {    
		Activity activity = null;
		Context context = null;
		ProgressDialog pDialog = null;
		int index = 0;
		
		int ProgressForDebug = 0;
		
		Bitmap[] ImageBitmap = null;
		String[] ContactNum = null;
		String[] Info = null;
		String[] Name = null;
		
		private webDataProcessor(Activity activity, Context context) {
			this.activity = activity;
			this.context = context;
		}
		
		@Override
		protected void onPreExecute() { // doInBackground 작업 전
			super.onPreExecute();
			
			FileAdministrator.writeLog("NearAtt.. Act onPreExecute");
			
			pDialog = new ProgressDialog(activity);
			pDialog.setTitle("Loading");
			pDialog.setMessage("웹 페이지 소스 로딩 중");
			pDialog.setCancelable(false);
			pDialog.show();
		}

   	    @Override
		protected String doInBackground(Void... params) {					
			ImageBitmap = new Bitmap[3];
			ContactNum = new String[3];
			Info = new String[3];
			Name = new String[3];
			
			FileAdministrator.writeLog("NearAttractionsActivity Type" + (NearAttractionsActivity.AttractionType == 1 ? 1 : 2) + " 작업 시작");
			
			String line = "";
			
			try { // http://www.chungbuknadri.net/tour/neer.do?tKey=165&maxPageItems=5&neerType=3 
				String urlString = new StringBuilder("http://www.chungbuknadri.net/tour/neer.do?tKey=")
					.append(HeritageAdministrator.indexKey)
					.append("&maxPageItems=3&neerType=")
					.append(NearAttractionsActivity.AttractionType == 1 ? 3 : 2)
					.toString();
					FileAdministrator.writeLog("NearAttractionsActivity webDataProcessor urlString: " + urlString);
				InputStream is = new URL(urlString).openStream();		
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

				while((line = br.readLine()) != null) { 
					if(line.indexOf("<dt class=\"tit\"><a href=") != -1) {
						FileAdministrator.writeLog("NearAttractionsActivity Type" + (NearAttractionsActivity.AttractionType == 1 ? "1 맛집" : "2 숙박") + "스캔 중 일치");
						NearAttractionsActivity.AttractionsURL[index] = new StringBuilder("http://www.chungbuknadri.net")
							.append(line.substring(line.indexOf("<dt class=\"tit\"><a href=") + 25, line.indexOf("target=") - 2))
							.toString();
						
						index++;
						
						FileAdministrator.writeLog(NearAttractionsActivity.AttractionsURL[index - 1]);
					}
				}
				
				switch(index) { // 버튼 갯수 해줌
					case 0:
						publishProgress(1404);
						break;
					
					case 1:
						publishProgress(1000);
						break; 
						
					case 2:
						publishProgress(1001);
						break;
						
					case 3:
						publishProgress(1002);
						break;
					
					default:
						break;
				}
				
				for(int i = 0; i < index; i++) {
					String ImageURL = null;
					urlString = NearAttractionsActivity.AttractionsURL[i];
					
					boolean imaged = false;
					
					is = new URL(urlString).openStream();
					br = new BufferedReader(new InputStreamReader(is, "utf-8"));

					if(NearAttractionsActivity.AttractionType == 1) {
						while((line = br.readLine()) != null) {  
							if(line.indexOf("/upload/www/tour/5/") != -1 && !imaged) {
								imaged = true;
								ImageURL = new StringBuilder("http://www.chungbuknadri.net")
									.append(line.substring(line.indexOf("/upload/www/tour/5/"), line.indexOf("width=") - 2))
									.toString();
									
								Name[i] = line.substring(line.indexOf("alt=\"") + 5, line.indexOf("class=") - 2);
							}
							
							if(line.indexOf("<dt>대표전화</dt>") != -1) {
								line = br.readLine();
								ContactNum[i] = line.substring(line.indexOf("<dd>") + 4, line.indexOf("</dd>"));
							}
							
							if(line.indexOf("<h5>맛집소개</h5>") != -1) {
								line = br.readLine();
								Info[i] = line.substring(line.indexOf("<p>") + 3, line.indexOf("</p>"));
							}		
						}
					} else if(NearAttractionsActivity.AttractionType == 2) {
						while((line = br.readLine()) != null) {  
							if(line.indexOf("/upload/www/tour/4/") != -1 && !imaged) {
								imaged = true;
								ImageURL = new StringBuilder("http://www.chungbuknadri.net")
									.append(line.substring(line.indexOf("/upload/www/tour/4/"), line.indexOf("width=") - 2))
									.toString();
									
								Name[i] = line.substring(line.indexOf("alt=\"") + 5, line.indexOf("class=") - 2);
							}
							
							if(line.indexOf("<td>0") != -1) {
								ContactNum[i] = line.substring(line.indexOf("<td>") + 4, line.indexOf("</td"));
							}
							
							if(line.indexOf("<h5>멋집소개</h5>") != -1) {
								line = br.readLine();
								Info[i] = line.substring(line.indexOf("<p>") + 3, line.indexOf("</p>"));
							}		
						}
					}
					
					FileAdministrator.writeLog("NearAttractionsActivity 이미지작업 진행, index: " + index);	
					
					File f = new File(FileAdministrator.privatePath + "/" + "tempimage");
					
					URL targetURL = new URL(ImageURL);
					HttpURLConnection conn = (HttpURLConnection)targetURL.openConnection(); 
					conn.connect();

					byte[] buf = new byte[conn.getContentLength()];

					InputStream iis = conn.getInputStream();
					FileOutputStream fos = new FileOutputStream(f);
					
					FileAdministrator.writeLog("NearAttractionsActivity 이미지 쓰기 시작");
					
					int readbyte = 0;
					while((readbyte = iis.read(buf)) > 0) 
						fos.write(buf, 0, readbyte);
					
					iis.close();
					fos.close();
					
					ImageBitmap[i] = BitmapFactory.decodeFile(f.getAbsolutePath());
				}
			} catch(Exception e) {
				FileAdministrator.writeExceptionLog(e, "In NearAttractionsActivity doInBackground Type1");
			}
			return "asdasd";
		}

		@Override
		protected void onProgressUpdate(Integer[] values) { // doInBackground 에서 publishProgress 로 사용한 값, Thread&Handler 에서 핸들러 역할을 한다.
			switch(values[0]) {
				case 1404:
					Toast.makeText(context, "주변에 " + (NearAttractionsActivity.AttractionType == 1 ? "맛집이" : "숙박지가") +   " 없습니다.", Toast.LENGTH_LONG).show();
					finish();
					break;
										
				case 1000:
					ButtonV[0].setVisibility(View.VISIBLE);
					break;
					
				case 1001:
					ButtonV[0].setVisibility(View.VISIBLE);
					ButtonV[1].setVisibility(View.VISIBLE);
					break;
					
				case 1002:
					ButtonV[0].setVisibility(View.VISIBLE);
					ButtonV[1].setVisibility(View.VISIBLE);
					ButtonV[2].setVisibility(View.VISIBLE);
					break;

				default:
					break;
			}
		}

		@Override
		protected void onPostExecute(String result) { // doInBackground 의 리턴값, 즉 doInBackground 의 끝.
			for(int i = 0; i < index; i++) {
				String infoStr = new StringBuilder("\t전화번호: ")
					.append(ContactNum[i])
					.append("\n\n")
					.append("   ")
					.append(Info[i])
					.toString();
					
				ImageV[i].setImageBitmap(ImageBitmap[i]);
				TextV[(i * 2)].setText(Name[i]);
				TextV[(i * 2) + 1].setText(infoStr);
			}
			
			pDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();	
		}  
	}
}










				
/* 				String line = null;
				while((line = br.readLine()) != null) { 
					if(line.indexOf("</a> |<em>거리") != -1) {
						AttractionIntroduce += line.substring(line.indexOf("er;\">") + 5, line.indexOf("</a> |<em>거리"));
					}
					
					if(line.indexOf("/upload/www/tour/") != -1) {
						ImageURL = new StringBuilder("http://www.chungbuknadri.net")
							.append(line.substring(line.indexOf("/upload/www/tour/"), line.indexOf("width=") - 2))
							.toString();
						
						br = readLine();
						br = readLine();
						br = readLine();
						br = readLine();
						br = readLine();
						br = readLine();
						
						AttractionIntroduce += "\n\n */
	
