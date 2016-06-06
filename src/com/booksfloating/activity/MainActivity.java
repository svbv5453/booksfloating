package com.booksfloating.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.booklsfloating.activity.searchbooks.SearchBooksFragment;
import com.booksfloating.adapter.FragmentAdapter;
import com.booksfloating.adapter.MainPagerAdapter;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.parse.JsonParser;
import com.booksfloating.util.SharePreferenceUtil;
import com.booksfloating.widget.MyRadioButton;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xd.booksfloating.R;

/**
 * 
 * @author wenyuanliu
 *
 */
public class MainActivity extends FragmentActivity implements OnClickListener{
	private ViewPager viewPager;
	private MyRadioButton mrb_search_books,mrb_info_notice,
			mrb_books_recommend,mrb_my_info;

	public static final int TAB_SEARCH_BOOKS = 0;
	public static final int TAB_INFO_NOTICE = 1;
	public static final int TAB_BOOKS_RECOMMEND = 2;
	public static final int TAB_MY_INFO = 3;
	private static Boolean isExit = false;
	private Button btn_voice;
	
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private String voiceSearch;
	
	
	public String getVoiceSearch() {
		return voiceSearch;
	}

	public void setVoiceSearch(String voiceSearch) {
		this.voiceSearch = voiceSearch;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5628aaa4");
		setContentView(R.layout.activity_main);
		initViewPager();
		SharePreferenceUtil sp = new SharePreferenceUtil(this, Constants.SAVE_USER);
		if(sp.getToken() != null && sp.getToken().length() > 0)
		{
			Constants.isLogin = true;
			System.out.println("用户已登录！token 为"+sp.getToken());
		}
	}
	
	private void initViewPager()
	{

		viewPager = (ViewPager)findViewById(R.id.vp_main_activity);
		mrb_search_books = (MyRadioButton)findViewById(R.id.mrb_search_books);
		mrb_info_notice = (MyRadioButton)findViewById(R.id.mrb_info_notice);
		mrb_books_recommend = (MyRadioButton)findViewById(R.id.mrb_books_recommend);
		mrb_my_info = (MyRadioButton)findViewById(R.id.mrb_my_info);
		
		mrb_search_books.setOnClickListener(this);
		mrb_info_notice.setOnClickListener(this);
		mrb_books_recommend.setOnClickListener(this);
		mrb_my_info.setOnClickListener(this);
		
		FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(0);
		ViewPagerListener();

	}

	private void ViewPagerListener()
	{
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case TAB_SEARCH_BOOKS:
					mrb_search_books.setChecked(true);
					System.out.println("选择1");
					break;
				case TAB_INFO_NOTICE:
					mrb_info_notice.setChecked(true);
					System.out.println("选择2");
					break;
				case TAB_BOOKS_RECOMMEND:
					mrb_books_recommend.setChecked(true);
					System.out.println("选择3");
					break;
				case TAB_MY_INFO:
					mrb_my_info.setChecked(true);
					System.out.println("选择4");
				default:
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mrb_search_books:
			viewPager.setCurrentItem(TAB_SEARCH_BOOKS);
			break;
		case R.id.mrb_info_notice:
			viewPager.setCurrentItem(TAB_INFO_NOTICE);
			break;
		case R.id.mrb_books_recommend:
			viewPager.setCurrentItem(TAB_BOOKS_RECOMMEND);
			break;
		case R.id.mrb_my_info:
			viewPager.setCurrentItem(TAB_MY_INFO);
			break;
			/*case R.id.btn_voice:
			// 1.创建RecognizerDialog对象
			RecognizerDialog mDialog = new RecognizerDialog(
					MainActivity.this, mInitListener);
			// 2.设置accent、language等参数
			mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
			// 3.设置回调接口
			mDialog.setListener(mRecognizerDialogListener);
			// 4.显示dialog，接收语音输入
			mDialog.show();
			break;*/
		default:
			break;
		}
	}

	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		@Override
		public void onResult(RecognizerResult arg0, boolean arg1) {
			String text = JsonParser.parseIatResult(arg0.getResultString());
			System.out.println("text" + text);
			String sn = null;
			// 读取json结果中的sn字段
			try {
				JSONObject resultJson = new JSONObject(arg0.getResultString());
				sn = resultJson.optString("sn");
				System.out.println("sn" + sn);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			mIatResults.put(sn, text);

			StringBuffer resultBuffer = new StringBuffer();
			for (String key : mIatResults.keySet()) {
				resultBuffer.append(mIatResults.get(key));
			}
			voiceSearch = resultBuffer.toString();
			if (voiceSearch != null) {
				setVoiceSearch(voiceSearch);
			}
		}

		@Override
		public void onError(SpeechError arg0) {

		}
	};
	
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
		}
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//getIntExtra的原型为getIntExtra(String name, int defaultValue),其中defaultValue为当name对应的值为null时设置的默认值

		int id = getIntent().getIntExtra("intent_fragmentId", -1);
		
		if (id == TAB_INFO_NOTICE) {
			viewPager.setCurrentItem(TAB_INFO_NOTICE, true);
		}
		getIntent().removeExtra("intent_fragmentId");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Timer tExtit = null;
			if(isExit == false){
				isExit = true;
				Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
				tExtit = new Timer();
				tExtit.schedule(new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						isExit = false;
					}
				}, 2000);
			}else{
				finish();
				System.exit(0);
			}
				
		}
		return false;
		
	}
	
	
}
