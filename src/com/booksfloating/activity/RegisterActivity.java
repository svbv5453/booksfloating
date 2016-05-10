package com.booksfloating.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SharePreferenceUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;
import com.xd.dialog.DialogFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import com.booksfloating.globalvar.Constants;

public class RegisterActivity extends Activity implements OnClickListener,OnEditorActionListener{
	private EditText et_stuid,et_schoolname,et_phonenum,et_emailadderss,et_username,et_password,et_confirmpsd;
	private AutoCompleteTextView actv_email;
	private Button btn_register_now;
	private Button btn_back = null;
	private View mProgressView;
	private View mLoginFormView;
	private int schoolNum;
	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		context = RegisterActivity.this;
		initView();
	}

	private void initView()
	{
		actv_email = (AutoCompleteTextView) findViewById(R.id.actv_email);
		populateAutoComplete();
		et_password = (EditText)findViewById(R.id.et_password);
		mProgressView = findViewById(R.id.login_progress);
		et_confirmpsd = (EditText)findViewById(R.id.et_confirmpsd);
		et_phonenum = (EditText)findViewById(R.id.et_phonenum);
		et_schoolname = (EditText)findViewById(R.id.et_schoolname);
		et_stuid = (EditText)findViewById(R.id.et_stuid);
		et_username = (EditText)findViewById(R.id.et_username);
		btn_register_now = (Button)findViewById(R.id.btn_register_now);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		btn_register_now.setOnClickListener(this);
	}
	
	private void populateAutoComplete() {
		if (VERSION.SDK_INT >= 14) {
			// Use ContactsContract.Profile (API 14+)
			//getLoaderManager().initLoader(0, null, this);
		} else if (VERSION.SDK_INT >= 8) {
			// Use AccountManager (API 8+)
			new SetupEmailAutoCompleteTask().execute(null, null);
		}
	}
	
	/**
	 * Use an AsyncTask to fetch the user's email addresses on a background
	 * thread, and update the email text field with results on the main UI
	 * thread.
	 */
	class SetupEmailAutoCompleteTask extends
			AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... voids) {
			ArrayList<String> emailAddressCollection = new ArrayList<String>();

			// Get all emails from the user's contacts and copy them to a list.
			ContentResolver cr = getContentResolver();
			Cursor emailCur = cr.query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					null, null, null);
			while (emailCur.moveToNext()) {
				String email = emailCur
						.getString(emailCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				emailAddressCollection.add(email);
			}
			emailCur.close();

			return emailAddressCollection;
		}
	}
	
	private boolean checkInput()
	{
		// Reset errors.
		et_phonenum.setError(null);
		et_confirmpsd.setError(null);
		actv_email.setError(null);
		et_password.setError(null);
		et_schoolname.setError(null);

		// Store values at the time of the login attempt.
		String phoneNum = et_phonenum.getText().toString();
		String confirPsd = et_confirmpsd.getText().toString();
		String email = actv_email.getText().toString();
		String password = et_password.getText().toString();
		String schoolName = et_schoolname.getText().toString();
		
		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(schoolName)) {
			et_schoolname.setError("学校名称不能为空！");
			focusView = et_schoolname;
			cancel = true;
		}else if((schoolNum = convertSchoolAddress(schoolName)) == 0){
			et_schoolname.setError("请按照提示正确填写学校名称和校区");
			focusView = et_schoolname;
			cancel = true;
		}
		
		if(TextUtils.isEmpty(phoneNum)){
			et_phonenum.setError("手机号码不能为空！");
			focusView = et_phonenum;
			cancel = true;
		}else if(!isPhoneNumValid(phoneNum)){
			et_phonenum.setError("手机号码不合法！");
			focusView = et_phonenum;
			cancel = true;
		}
		
		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			et_password.setError("输入密码太短！");
			focusView = et_password;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			actv_email.setError("邮箱用于找回密码，不能为空");
			focusView = actv_email;
			cancel = true;
		} else if (!isEmailValid(email)) {
			actv_email.setError("邮箱格式不合法！");
			focusView = actv_email;
			cancel = true;
		}
		
		if(!password.equals(confirPsd)){
			et_confirmpsd.setError("两次输入密码不一致！");
			focusView = et_confirmpsd;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
			return false;
		}
		return true;
	}
	
	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}
	
	private boolean isPhoneNumValid(String phoneNum){
		Pattern p = Pattern.compile("^(13|15|18)\\d{9}$");
		Matcher m = p.matcher(phoneNum);
		return m.matches();
	}
	
	private int convertSchoolAddress(String schoolName){
		int schoolKey = 0;
		if(schoolName.contains("西安电子科技大学"))
		{
			if (schoolName.contains("老校区")||schoolName.contains("北校区")||schoolName.contains("雁塔校区")) {
				schoolKey = Constants.schoolNameMap.get("西安电子科技大学雁塔校区");
			}else {
				schoolKey = Constants.schoolNameMap.get("西安电子科技大学长安校区");
			}
		}
		else if (schoolName.contains("西北工业大学")) {
			if (schoolName.contains("老校区")||schoolName.contains("北校区")||schoolName.contains("友谊校区")) {
				schoolKey = Constants.schoolNameMap.get("西北工业大学友谊校区");
			}
			else{
				schoolKey = Constants.schoolNameMap.get("西北工业大学长安校区");
			}
		}
		else if (schoolName.contains("西安交通大学")) {
			if (schoolName.contains("老校区")||schoolName.contains("兴庆校区")) {
				schoolKey = Constants.schoolNameMap.get("西安交通大学兴庆校区");
			}
			else if(schoolName.contains("雁塔校区")){
				schoolKey = Constants.schoolNameMap.get("西安交通大学雁塔校区");
			}
			else{
				schoolKey = Constants.schoolNameMap.get("西安交通大学曲江校区");
			}
		}
		else if (schoolName.contains("陕西师范大学")) {
			if (schoolName.contains("老校区")||schoolName.contains("北校区")||schoolName.contains("雁塔校区")) {
				schoolKey = Constants.schoolNameMap.get("陕西师范大学雁塔校区");
			}
			else{
				schoolKey = Constants.schoolNameMap.get("陕西师范大学长安校区");
			}
		}
		else if (schoolName.contains("长安大学")) {
			if (schoolName.contains("雁塔校区")) {
				schoolKey = Constants.schoolNameMap.get("长安大学雁塔校区");
			}
			else{
				schoolKey = Constants.schoolNameMap.get("长安大学渭水校区");
			}
		}
		else if (schoolName.contains("西安工业大学")) {
			if (schoolName.contains("金花校区")) {
				schoolKey = Constants.schoolNameMap.get("西安工业大学金花校区");
			}
			else if(schoolName.contains("未央校区")){
				schoolKey = Constants.schoolNameMap.get("西安工业大学未央校区");
			}
			else{
				schoolKey = Constants.schoolNameMap.get("西安工业大学洪庆校区");
			}
		}
		else if(schoolName.contains("西安工程大学"))
		{
			schoolKey = Constants.schoolNameMap.get("西安工程大学金花校区");
		}
		else if(schoolName.contains("西安科技大学"))
		{
			schoolKey = Constants.schoolNameMap.get("西安科技大学雁塔校区");
		}
		
		return schoolKey;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_register_now:
			if(checkInput())
			{
				registerSubmit();
			}
			break;
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		
		return false;
	}
	
	private String jsonStr;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:	
				int result = 0;
				//解析json数据，判断是否注册成功
				result = parseReturnJson(jsonStr);
				if (result == 1) {
					SharePreferenceUtil sp = new SharePreferenceUtil(RegisterActivity.this, Constants.SAVE_USER);
					sp.setAccount(et_username.getText().toString());
					sp.setPassword(et_password.getText().toString());
					
					Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
					System.out.println("用户注册成功");
				}else if(result == -1){
					Toast.makeText(RegisterActivity.this, "用户名已被使用", Toast.LENGTH_SHORT).show();
					et_username.setText("");
					System.out.println("用户名已被使用");
				}else if (result == -2) {
					Toast.makeText(RegisterActivity.this, "手机号已被使用", Toast.LENGTH_SHORT).show();
					et_phonenum.setText("");
					System.out.println("手机号已被使用");
				}
				try {
					Thread.sleep(50);
					if (result == 1) {
						finish();
					}					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case -1:
				//服务器返回错误
				Toast.makeText(RegisterActivity.this, "服务器错误，登录失败！", Toast.LENGTH_SHORT).show();
				break;
			
			default:
				break;
			}			
		}
	};
	
	private void registerSubmit() {
		showLoadingDialog();
		final PostParameter[] parameters = new PostParameter[6];
		new Thread(new Runnable() {
			//http://10.170.51.229:8080/booksfloating/register?
			//student_number=1403121111&university=003&phone=13712345678&
			//email=147258@qq.com&username=yonghuming&password=123456
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(schoolNum == 0)
				{
					schoolNum = convertSchoolAddress(et_schoolname.getText().toString());
				}
				
				parameters[0] = new PostParameter("student_number", et_stuid.getText().toString());
				parameters[1] = new PostParameter("university", Integer.toString(schoolNum));
				parameters[2] = new PostParameter("phone", et_phonenum.getText().toString());
				parameters[3] = new PostParameter("email", actv_email.getText().toString());
				parameters[4] = new PostParameter("username", et_username.getText().toString());
				parameters[5] = new PostParameter("password", et_password.getText().toString());
				
				jsonStr = HttpUtil.httpRequest(HttpUtil.USER_REGISTER, parameters, HttpUtil.POST);
				if(jsonStr != null)
				{
					dismissLoadingDialog();
					handler.sendEmptyMessage(0);
				}
				else {
					dismissLoadingDialog();
					handler.sendEmptyMessage(-1);
				}
				
			}
		}).start();
	}
	
	private int parseReturnJson(String jsonString)
	{
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			String status = jsonObj.optString("status");
			String token = jsonObj.optString("token");
			if(status.equals("1"))
			{
				return 1;				
			}
			else if (status.equals("-1")) {
				return -1;				
			}
			else if (status.equals("-2")) {
				return -2;				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	private Dialog dialog = null;
	private void showLoadingDialog(){
		if(dialog == null)
		{
			dialog = DialogFactory.creatLoadingDialog(this, "正在注册，请稍后...");
			dialog.show();
		}
		else {
			dialog.dismiss();
		}
	}
	
	private void dismissLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
