package com.lilarcor.planner;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.widget.TabHost.*;
import android.graphics.*;
import android.net.*;
import java.util.*;
import java.lang.*;

// return px / this.getContext().getResources().getDisplayMetrics().density;

public class MainActivity extends TabActivity
{

//============================| Global Variables |=========================

	private TabHost tabs;
	private ImageView action;
	private AlertDialog.Builder aDialog = null;
	
	static public int CityKey = -1;
	static public String CityName[] = {
		"청주",
		"청원",
		"진천",
		"증평",
		"음성",
		"충주",
		"괴산",
		"제천",
		"단양",
		"옥천",
		"영동",
		"보은"
	};
	
	final static public String VERSION = "0.23";
	
//============================| Events |===================================
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState); // 슈퍼클래스의 생성자를 호출합니다.
	    setContentView(R.layout.main); // main.xml 의 내용을 불러옵니다.
		setLogAvailable(); // 로그를 사용 가능하게 합니다.
		setRandomCityKey(); // 시티 키를 랜덤하게 셋팅합니다.
//		setEachKeys(); // 도시 세 개를 직접 셋팅합니다.
		
		checkNetwork(false); // 네트워크 연결을 확인하고, 재연결을 시도하며 연결되지 않았을 시 그대로 갑니다. 인자는 aDialog 생성 여부
    }
	
//-------------------------------------------------------------------------

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
//============================| Methods |==================================
	private void setLogAvailable() {
		FileAdministrator fAdmin = new FileAdministrator(this); // 로그를 사용할 수 있게 생성자를 호출합니다.
		return;
	}
	
	public void setRandomCityKey() {
		Calendar c = Calendar.getInstance(); // 랜덤 시드를 설정합니다.
		MyRandom.mysrand(c.get(Calendar.MONTH) + c.get(Calendar.YEAR) + c.get(Calendar.DAY_OF_MONTH));
		
		CityKey = MyRandom.myRandom();
		return; 
	}
	
	private void setTabs() {
		tabs = getTabHost();
	
		action = (ImageView)findViewById(R.id.action);
	
		handler.sendEmptyMessageDelayed(0, 4000);

		ImageView tabwidget0 = new ImageView(this);
        tabwidget0.setImageResource(R.drawable.widget0);
        ImageView tabwidget1 = new ImageView(this);
        tabwidget1.setImageResource(R.drawable.widget1);
        ImageView tabwidget2 = new ImageView(this);
        tabwidget2.setImageResource(R.drawable.widget2);		
		
        tabs.addTab(tabs.newTabSpec("tab0")
            .setIndicator(tabwidget0)
            .setContent(new Intent(this, Tab0Activity.class)));
       
        tabs.addTab(tabs.newTabSpec("tab1")
            .setIndicator(tabwidget1)
            .setContent(new Intent(this, Tab1Activity.class)));
			
        tabs.addTab(tabs.newTabSpec("tab2")
            .setIndicator(tabwidget2)
            .setContent(new Intent(this, Tab2Activity.class)));
/*			
        for(int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
            tabs.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
*/
		// 130 은 어떤 X
		// Density = 1.5
		// 어떠한 Density 의 연산 결과로 130이 나온다.
		// 1. 자기 폰 가로를 구한다.
		// 2. 폰 / 2 한다. 이는 X 다.
		// 3. 720 / X 한다. 3이 나온다. 이는 Y 다.
		// 4. 390 / Y 한 값을 픽셀로 때려준다.
		// 5. 즉, 390 / (720 / (Width / 2)) 가 값이다.
		// 6. 이를 정리하면 (13 * Width) / 48
//		float Density = this.getResources().getDisplayMetrics().density; // 1.5
//		float DP = (float)(130f / Density); // 
		Display dis = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		float Width = dis.getWidth();
		for (int tab = 0; tab < tabs.getTabWidget().getChildCount(); ++tab) {
			tabs.getTabWidget().getChildAt(tab).getLayoutParams().height = (int)((13 * Width) / 48);
		}
		
		tabs.setCurrentTab(0);
		
		return;
	}
	
	private void checkNetwork(boolean isCreated) {
		if(FileAdministrator.isOnline(this) == true) { setTabs(); return;}
		
		if(isCreated == false) {
			aDialog = new AlertDialog.Builder(this);	
			aDialog.setTitle("알림");
			aDialog.setMessage("네트워크 접속이 필요합니다. 데이터 네트워크 / WIFI 를 이용하여 주십시오."); 
			aDialog.setCancelable(false);
			aDialog.setPositiveButton("재시도", new myDialogInterface(this)); // 영문을 모르겠으나 위 아래 버튼 내용이 바뀌어야 함.		
			aDialog.setNegativeButton("취소", new myDialogInterface(this));	// 위 내용은 ICS 부터 바뀜. 무조건 Positive 가 우측에 위치함.
			aDialog.create();
		}
		aDialog.show();
		return;
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			action.setVisibility(View.GONE);
		}
	};
	
	class myDialogInterface implements DialogInterface.OnClickListener
	{
		Activity activity;
		public myDialogInterface(Activity activity) {
			this.activity = activity;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int whichButton) {   
			if(whichButton == DialogInterface.BUTTON_POSITIVE) {
				if(FileAdministrator.isOnline(activity) == false) {
					checkNetwork(true);
				} else {
					setTabs();
				}
			} else {
System.exit(0);
}
		}
	}
}


/*
Toast.makeText(this, "DEBUG:0", Toast.LENGTH_SHORT).show(); 
*/