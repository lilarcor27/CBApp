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

public class MoreImageActivity extends Activity implements OnClickListener
{
	private ImageView[] ImageV = null;
	private TextView[] TextV = null;
	private ImageButton[] IButtonV = null;
	
	private ProgressDialog pDialog = null;
	private int imageIndex = 0;
	private int nowIndex = 0;
	private String[] imageURL = new String[10];
	private String[] imageName = new String[10];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moreimage);
		
		setViews();
		loadImage(); // 이미지를 전부 가져온다.
	}
	
	@Override
	public void onClick(View v) {
		String imageDir = null;
		switch(v.getId()) {
		case R.id.mimgimagebutton00:
			if(nowIndex == 0) return;
			nowIndex--;
			
			imageDir = new StringBuilder(FileAdministrator.privatePath)
				.append("/images/")
				.append(imageName[nowIndex])
				.toString();
				
			setImageBitmapEx(ImageV[0], imageDir);
			TextV[0].setText((nowIndex + 1) + " / " + imageIndex);
			break;
			
		case R.id.mimgimagebutton01:
			if(nowIndex == imageIndex - 1) return;
			nowIndex++;
			
			imageDir = new StringBuilder(FileAdministrator.privatePath)
				.append("/images/")
				.append(imageName[nowIndex])
				.toString();
				
			setImageBitmapEx(ImageV[0], imageDir);
			TextV[0].setText((nowIndex + 1) + " / " + imageIndex);
			break;
		default:	
		}
	}
	
/*
BitmapFactory.Options options = new BitmapFactory.Options();
options.inJustDecodeBounds = true;
BitmapFactory.decodeFile(fileName, options);

return options.outHeight;

*/
	
	private void setProgressDialog() { 
		pDialog = new ProgressDialog(this);
		pDialog.setTitle("Loading");
		pDialog.setMessage("이미지 로딩 중..");
		pDialog.setCancelable(false);
		pDialog.show();
	}
	
	private void setViews() {
		ImageV = new ImageView[1];
		TextV = new TextView[1];
		IButtonV = new ImageButton[2];
		
		ImageV[0] = (ImageView)findViewById(R.id.mimgimage00);
		TextV[0] = (TextView)findViewById(R.id.mimgtext00);
		IButtonV[0] = (ImageButton)findViewById(R.id.mimgimagebutton00);
		IButtonV[1] = (ImageButton)findViewById(R.id.mimgimagebutton01);
	
		IButtonV[0].setOnClickListener(this);
		IButtonV[1].setOnClickListener(this);
		return;
	}
	
	private void loadImage() {
		webDataProcessor wdp = new webDataProcessor();
		wdp.execute();
		return;
	}
	
	private void setImageBitmapEx(ImageView targetImage, String imageDirection) {
		BitmapFactory.Options bmo = new BitmapFactory.Options();
		
		File imageFile = new File(imageDirection);
		if(imageFile.length() > 1000000)
			bmo.inSampleSize = 16;
		else if(imageFile.length() > 100000)
			bmo.inSampleSize = 4;
		
		targetImage.setImageBitmap(BitmapFactory.decodeFile(imageDirection, bmo));
	}
			
	private class webDataProcessor extends AsyncTask<Void, Integer, String> {    
		@Override
		protected void onPreExecute() { // doInBackground 작업 전
			super.onPreExecute();
			setProgressDialog();
		}

   	    @Override
		protected String doInBackground(Void... params) {		
			InputStream is = null;
			BufferedReader br = null; // http://chungbuknadri.net/tour/images.do?tKey=1139&tourFileKey=7281
			try { // http://chungbuknadri.net/upload/www/tour/2/1139/1181319196092.jpg
				FileAdministrator.writeLog("MIAct. doInB.. 진입");
				
				String line = null;
				
				File firstProcessFile = new File(FileAdministrator.privatePath + '/' + HeritageAdministrator.indexKey);
				FileInputStream fis = new FileInputStream(firstProcessFile); 
				BufferedReader fbr = new BufferedReader(new InputStreamReader(fis, "utf-8"));
				int index;
				for(index = 0; (line = fbr.readLine()) != null; index++);
				fis.close();

				String urlString = new StringBuilder("http://chungbuknadri.net/tour/images.do?tKey=")
					.append(HeritageAdministrator.indexKey)
					.append("&tourFileKey=")
					.append(HeritageAdministrator.imageURLData)
					.toString();

				FileAdministrator.writeLog("MIAct. 접근 URL: " + urlString);
				
				is = new URL(urlString).openStream();		
				br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				
				String imageSource = new String("thumb_img");
				
				if(index < 6) {
				FileAdministrator.writeLog("MIAct. index<6 진입");
					FileOutputStream fpos = new FileOutputStream(firstProcessFile, true);
					File f = null;
					FileAdministrator.writeLog("MIAct. while.. 진입");
					while((line = br.readLine()) != null) { 
						if(line.indexOf(imageSource) != -1) {
							imageURL[imageIndex] = new StringBuilder("http://chungbuknadri.net/")	
								.append(line.substring(82, (line.indexOf(".jpg") + line.indexOf(".JPG") + 1) + 4))
								.toString();
							imageName[imageIndex] = imageURL[imageIndex].substring(imageURL[imageIndex].lastIndexOf('/') + 1);
								
							fpos.write((imageURL[imageIndex].substring(imageURL[imageIndex].lastIndexOf('/') + 1) + '\n').getBytes());
							
							f = new File(FileAdministrator.privatePath + "/images/" + imageName[imageIndex]);
							
							FileAdministrator.writeLog("MIAct. Image Decode.. 진입" + '\n' + imageURL[imageIndex]);
							
							if(f.exists() == false) {
								URL targetURL = new URL(imageURL[imageIndex]);
								HttpURLConnection conn = (HttpURLConnection)targetURL.openConnection(); 
								conn.connect();
							
								byte[] buf = new byte[conn.getContentLength()];

								InputStream iis = conn.getInputStream();
								FileOutputStream fos = new FileOutputStream(f);
								
								FileAdministrator.writeLog("MIAct. Image write.. 진입");
								
								int readbyte = 0;
								while((readbyte = iis.read(buf)) > 0) 
									fos.write(buf, 0, readbyte);
								
								iis.close();
								fos.close();
							}
							
							imageIndex++;				
						}
						if(imageIndex == 10)
							break;
					}
				} else {
					FileAdministrator.writeLog("Pass doInBa...");
					
					firstProcessFile = new File(FileAdministrator.privatePath + '/' + HeritageAdministrator.indexKey);
					fis = new FileInputStream(firstProcessFile); 
					fbr = new BufferedReader(new InputStreamReader(fis, "utf-8"));
					
					fbr.readLine();
					fbr.readLine();
					fbr.readLine();		
					fbr.readLine();
					fbr.readLine();
					
					for(int i = 0; i < 10; i++) {
						if((line = fbr.readLine()) != null) {
							imageName[i] = line;
							imageIndex++;
						}
					}
					fis.close();
					
					FileAdministrator.writeLog(imageName[0]);
				}
			} catch(Exception e) {
				FileAdministrator.writeExceptionLog(e, "in MoreImageActivity");
			}
			return "asd";
		}

		@Override
		protected void onProgressUpdate(Integer[] values) { // doInBackground 에서 publishProgress 로 사용한 값, Thread&Handler 에서 핸들러 역할을 한다.
			switch(values[0].intValue()) {
			case -1:
				break;		
			default:
				break;
			}
		}

		@Override
		protected void onPostExecute(String result) { // doInBackground 의 리턴값, 즉 doInBackground 의 끝.
			FileAdministrator.writeLog("MIAct. onPos.. 진입");
			FileAdministrator.writeLog("현재 index: " + imageIndex);
			
			pDialog.dismiss();
			
			TextV[0].setText("1 / " + imageIndex); 
			
			String imageDir = new StringBuilder(FileAdministrator.privatePath)
				.append("/images/")
				.append(imageName[0])
				.toString();
				
			FileAdministrator.writeLog(imageDir);
		
			setImageBitmapEx(ImageV[0], imageDir);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();	
		}  
	}
}
	
	