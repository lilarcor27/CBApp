/* 
 *	맛집 파싱 관련 자료
 *	http://www.chungbuknadri.net/tour/neer.do?tKey=165&maxPageItems=5&neerType=3 
 */
  
package com.lilarcor.planner;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class HeritageAdministrator
{

//============================| Global Variables |=========================

	private Activity activity = null;
	
	private ProgressDialog pDialog = null; // Recommend에서 넘어왔었으나 그냥 자체생성함.
	private Handler handler = null;
	
	private webDataProcessor wdp;
	static private String[] textData = new String[3];
	static private Bitmap bitmapData = null;
	
	static public int imageURLData = -1;
	static public int tourKeys[][] = {
		{1428, 1429, 1357, 186, 1156, 4, 1331, 1151},						// 청주
		{189, 419, 438, 423, 444, 436},									// 청원
		{40, 44, 95, 234},													// 진천
		{174, 176, 177},													// 증평 
		{114, 199, 116, 111, 198, 102},									// 음성
		
		{1102, 1220, 1609, 1219, 1048, 1038, 1120},						// 충주
		{1533, 1626, 1643, 1601},											// 괴산
		{1693, 1777, 1741},													// 제천
		{1276, 1360, 1320, 1285, 1286, 1334},								// 단양
		{1194, 499, 501},													// 옥천
		
		{263, 513, 254},													// 영동
		{392, 378, 401, 346, 1306, 400, 345}								// 보은
	};
	
	static public int indexKey = -1;
	
//============================| Constructor |==============================	
	
	public HeritageAdministrator(Activity activity, Handler handler) {
		this.activity = activity;
		this.handler = handler;
		
		wdp = new webDataProcessor();
		FileAdministrator.writeLog("HeritageAdministrator Constructor Called!" + HeritageAdministrator.indexKey);
	}
	
//============================| Methods |==================================

	private void setProgressDialog() { 
		pDialog = new ProgressDialog(activity);
		pDialog.setTitle("Loading");
		pDialog.setMessage("웹 페이지 소스 로딩 중");
		pDialog.setCancelable(false);
		pDialog.show();
	}
	
	static public String getTextData(int Index) {
		return textData[Index];
	}
	
	static public Bitmap getBitmapData() {
		return bitmapData;
	}
	
	public void loadHeritageData() {
		wdp = new webDataProcessor();
	
		FileAdministrator.writeLog("HeritageAdministrator loadHeritageData Method Called!");
		wdp.execute();
		return;
	}
	
//============================| Thread & Handler |=========================

	private class webDataProcessor extends AsyncTask<Void, Integer, String> {    
	
		private String textContent = null; // 텍스트뷰에 때려박기 위한 스트링
		private String textContent2 = null; // 겹침 현상 방지
		private String textContent3 = null; // 상동
		private Bitmap imageContent = null; // 이미지뷰에 때려박기 위한 비트맵
		
		@Override
		protected void onPreExecute() { // doInBackground 작업 전
			super.onPreExecute();
			setProgressDialog();
		}

   	    @Override
		protected String doInBackground(Void... params) {
			InputStream tis = null;
			BufferedReader br = null;
					
			int Flag = 0;
			int lineNum = 0;
			int progressForDebug = 0;
			
			String imageURL = "http://chungbuknadri.net";
			try {
				File firstProcessFile = new File(FileAdministrator.privatePath + '/' + HeritageAdministrator.indexKey);
				long fileSize = firstProcessFile.length();
				if(firstProcessFile.exists() == false || (firstProcessFile.exists() == true && fileSize == 0)) {
					FileAdministrator.writeLog("doInBackground try-catch 진입");
					tis = new URL("http://chungbuknadri.net/tour/view.do?menuKey=132&tourKey=" + HeritageAdministrator.indexKey).openStream();		
					br = new BufferedReader(new InputStreamReader(tis, "utf-8"));
					FileOutputStream fpos = new FileOutputStream(firstProcessFile);

					FileAdministrator.writeLog("http://chungbuknadri.net/tour/view.do?menuKey=132&tourKey=" + HeritageAdministrator.indexKey);

					String line = ""; 
					
					String summarySource = new String("<h5>개요</h5>");
					String trafficSource = new String("<h5>자가용 오시는길 안내</h5>");
					String imageDataSource = new String("firstFileKey");
					String imageSource = new String("/upload/www/tour/");
					
					String imageName = null;

					publishProgress(1);
					FileAdministrator.writeLog("doInBackground while 진입");
						
					while((line = br.readLine()) != null) { 
						lineNum++;
						if(Flag == 0 && line.indexOf(imageDataSource) != -1) { // 이미지 정보
							Flag++;
							FileAdministrator.writeLog("HeritageAdministrator imageDataSource Matched!");
							
							String buf = line.substring(68, line.lastIndexOf('"'));
							imageURLData = Integer.parseInt(buf);
							
							fpos.write((buf + '\n').getBytes());
							
							FileAdministrator.writeLog("imageURLData:" + imageURLData);
							publishProgress(2);
						}	
						if(Flag == 1 && line.indexOf(imageSource) != -1) { // 문화재 이미지
							Flag++;
							FileAdministrator.writeLog("HeritageAdministrator imageSource Matched!");
							
							imageURL += line.substring(16, line.indexOf(".png") + line.indexOf(".jpg") + line.indexOf(".JPG") + 6); // 이미지의 URL을 파싱하여 구했다. 
							imageName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
							fpos.write((imageName + '\n').getBytes()); // 다음 번 실행 시 안정화를 위해..
							
							FileAdministrator.writeLog("File Name = " + imageName);
							FileAdministrator.writeLog("imageURL = " + imageURL);
										
							File f = new File(FileAdministrator.privatePath + "/images/");
							f.mkdir();
							
							f = new File(FileAdministrator.privatePath + "/images/" + imageName);
							if(f.exists() == false) {
								FileAdministrator.writeLog("Image Not Found!");
								
								URL targetURL = new URL(imageURL);
								HttpURLConnection conn = (HttpURLConnection)targetURL.openConnection(); 
								conn.connect();
							
								byte[] buf = new byte[conn.getContentLength()];

								InputStream iis = conn.getInputStream();
								FileOutputStream fos = new FileOutputStream(f);
								
								int readbyte = 0;
								while((readbyte = iis.read(buf)) > 0) 
									fos.write(buf, 0, readbyte);
								
								iis.close();
								fos.close();
							}
							imageContent = BitmapFactory.decodeFile(f.getAbsolutePath());
							publishProgress(3);
							//targetImage.setImageBitmap(imageContent); 스레드에서 UI스레드 접근 X onProgressUpdate 로 넘어감
						
							/* 문화재 이름 파싱 시작 */
							textContent2 = line.substring(line.indexOf("alt=") + 5, line.indexOf("class=") - 2);
							FileAdministrator.writeLog("문화재 이름: " + textContent2);
							fpos.write((textContent2 + '\n').getBytes());
							publishProgress(30);
						} 
						if(Flag == 2 && line.indexOf(summarySource) != -1) { // 개요
							Flag++;
							FileAdministrator.writeLog("HeritageAdministrator summarySource Matched!");

							line = br.readLine(); // 다음 다음 번 행에 있는 내용을 가져옴
							line = br.readLine(); // 상동
							
							textContent = line.substring(6);
							textContent = deleteBadCharacter(textContent);
							fpos.write((textContent + '\n').getBytes()); // 안정화
							publishProgress(4);	
						}	
						if(Flag == 3 && line.indexOf(trafficSource) != -1) { // 교통정보
							Flag++;
							FileAdministrator.writeLog("HeritageAdministrator trafficSource Matched!");
							
							textContent3 = br.readLine(); // 다음번 행 가져오기
							
							textContent3 = deleteBadCharacter(textContent3);
							
							fpos.write((textContent3 + '\n').getBytes()); // 추후에 관리함. 일단 탭과 <br>태그 섞인 채로. 는 아니다. 그냥 여기서 처리한다.
							FileAdministrator.writeLog("textContent3: " + textContent3);
						
							publishProgress(5);
						}
					}
				} else {
					FileInputStream fis = new FileInputStream(firstProcessFile); 
					br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
					publishProgress(1);
					
					imageURLData = Integer.parseInt(br.readLine());
					publishProgress(2);
					
					imageContent = BitmapFactory.decodeFile(FileAdministrator.privatePath + "/images/" + br.readLine());
					publishProgress(3);
					
					textContent2 = br.readLine();
					FileAdministrator.writeLog("else 의 보타사.. 관련 : " + textContent2);
					publishProgress(30);
					
					textContent = br.readLine();
					publishProgress(4);
					
					textContent3 = br.readLine();
					publishProgress(5);
				}
			} catch(Exception e) {
				FileAdministrator.writeExceptionLog(e, new String("myData" + lineNum + " | " + progressForDebug));
			} finally {
				FileAdministrator.writeLog("Flag = " + Flag);
			}
				
			return "Not_Found!";
		}

		@Override
		protected void onProgressUpdate(Integer[] values) { // doInBackground 에서 publishProgress 로 사용한 값, Thread&Handler 에서 핸들러 역할을 한다.
			switch(values[0].intValue()) {
			case -1:
				FileAdministrator.writeLog("-1를 주고 뒤졌습니다.");
				break;
			case 1:
				pDialog.setMessage("웹 페이지에서 이미지 정보를 가져오는 중");
				break;
			case 2:
				pDialog.setMessage("웹 페이지에서 이미지를 가져오는 중");
				break;
			case 3:
				bitmapData = imageContent;
				pDialog.setMessage("웹 페이지에서 텍스트를 가져오는 중");
				break;
			case 30:
				textData[0] = (/* MainActivity.CityName[MainActivity.CityKey % 12] + " " + */textContent2);
				break;
			case 4:
				textData[1] = textContent;
				break;
			case 5:
				textData[2] = textContent3;
				pDialog.dismiss();
				break;
			default:
				FileAdministrator.writeLog("올바르지 않은 publishProgress 사용.");
				break;
			}
		}

		@Override
		protected void onPostExecute(String result) { // doInBackground 의 리턴값, 즉 doInBackground 의 끝.
		FileAdministrator.writeLog("onPostExecute..");
			handler.sendEmptyMessage(HeritageAdministrator.indexKey);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			
		}  
		
		private String deleteBadCharacter(String s) {
			s = s.replace("<br/>", " "); // 개행코드 제거
			s = s.replace("	", ""); // 탭 제거
			s = s.replace("<p>", ""); // 동일
			s = s.replace("</p>", "");
			s = s.replace("<BR>", " ");
			return s;
		}
	}
}
/*
//	Thread TViewThread

//	Thread wdp = new Thread() {
	class webDataProcessor extends Thread 
	{
		@Override
		public void run() {
			super.run();
			FileAdministrator.writeLog("HeritageAdministrator wdp Thread Called!");
            InputStream inputStream = new URL("http://www.naver.com").openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));             
			
				InputStream inputStream = new URL("http://chungbuknadri.net/tour/view.do?menuKey=132&tourKey=" + tourKeys[HeritageKey]).openStream();
				InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
				BufferedReader br = new BufferedReader(isr);	
				
				//<img src="/upload/www/tour/2/1139/thumb_1181319196092.jpg"
				String line = "";
				String imageSource = "<img src=\"/upload/www/tour/";
//				String trueImageSource = null;
				while((line = br.readLine()) != null) {
					if(line.charAt(0) != '<') continue; // 최적화
					if(line.indexOf(imageSource) != -1) {
						FileAdministrator.writeLog("HeritageAdministrator imageSource Matched!");
						for(int i = 0; i < line.length(); i++) {
							targetText.setText(line.substring(9));
							break;
						}
					}
				}				
	//		} catch(Exception e) {
	//			FileAdministrator.writeLog(e.toString());
	//		}
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
	
						<img src="/upload/www/tour/2/1139/thumb_1181319196092.jpg" width="336px" height="251px" alt="청룡사" class="majorFile" onerror="this.src='/home/skins/5/images/noimg.gif'" />
}

// AsyncTask클래스는 항상 Subclassing 해서 사용 해야 함.
   	    // 사용 자료형은
   	    // background 작업에 사용할 data의 자료형: String 형
   	    // background 작업 진행 표시를 위해 사용할 인자: Integer형
   	    // background 작업의 결과를 표현할 자료형: Long
   	    private class DoComplecatedJob extends AsyncTask<String, Integer, Long> {      
   	    
   	     
   	        // 이곳에 포함된 code는 AsyncTask가 execute 되자 마자 UI 스레드에서 실행됨.
   	        // 작업 시작을 UI에 표현하거나
   	        // background 작업을 위한 ProgressBar를 보여 주는 등의 코드를 작성.
057	        @Override
058	        protected void onPreExecute() {
059	            textResult.setText("Background 작업 시작 ");           
060	            super.onPreExecute();
061	        }
062	 
063	        // UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
064	        @Override
065	        protected Long doInBackground(String... strData) {
066	            long totalTimeSpent = 0;
067	             
068	            // 가변인자의 갯수 파악 (이 예제에서는 5개)
069	            int numberOfParams = strData.length;
070	             
071	            // 인자들을 이용한 어떤 작업을 처리를 함
072	            for(int i=0; i<numberOfParams; i++) {              
073	                 
074	                // 각 인자를 이용한 복잡한 Task 실행함.
075	                // 예제에서는 인자로 전달된 시간만큼 sleep
076	                SystemClock.sleep(new Integer(strData[i]));
077	                 
078	                // background 작업에 걸린시간을 누산해 리턴함
079	                totalTimeSpent += new Long(strData[i]);
080	                 
081	                // onProgressUpdate callback을 호출 해
082	                // background작업의 실행경과를 UI에 표현함
083	                publishProgress((int)(((i+1)/(float)numberOfParams)*100));
084	            }          
085	            return totalTimeSpent;
086	        }
087	         
088	        // onInBackground(...)에서 publishProgress(...)를 사용하면
089	        // 자동 호출되는 callback으로
090	        // 이곳에서 ProgressBar를 증가 시키고, text 정보를 update하는 등의
091	        // background 작업 진행 상황을 UI에 표현함.
092	        // (예제에서는 UI스레드의 ProgressBar를 update 함)
093	        @Override
094	        protected void onProgressUpdate(Integer... progress) {
095	            progressBar.setProgress(progress[0]);
096	        }
097	         
098	        // onInBackground(...)가 완료되면 자동으로 실행되는 callback
099	        // 이곳에서 onInBackground가 리턴한 정보를 UI위젯에 표시 하는 등의 작업을 수행함.
100	        // (예제에서는 작업에 걸린 총 시간을 UI위젯 중 TextView에 표시함)
101	        @Override
102	        protected void onPostExecute(Long result) {
103	            textResult.setText("Background 작업에 걸린 총 시간: "
104	                            + new Long(result).toString()
105	                            + "m초");   
106	        }
107	         
108	        // AsyncTask.cancel(boolean) 메소드가 true 인자로
109	        // 실행되면 호출되는 콜백.
110	        // background 작업이 취소될때 꼭 해야될 작업은  여기에 구현.
111	        @Override
112	        protected void onCancelled() {
113	            // TODO Auto-generated method stub
114	            super.onCancelled();
115	        }      
116	    }
//http://chungbuknadri.net/upload/www/tour/2/1139/thumb_1181319196092.jpg <img src="/upload/www/tour/2/1139/thumb_1181319196092.jpg"
*/
