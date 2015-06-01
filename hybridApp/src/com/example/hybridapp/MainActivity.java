package com.example.hybridapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener{

	private Document document;
	private String myHomeUrl = "http://ktk1015.dothome.co.kr/s2/";
	private String myHomeTestUrl = "http://ktk1015.dothome.co.kr/s2/test.php/";
	private URL url;
	private StringBuffer buffer;
	private Button saveBtn, getInfoBtn;
	private EditText memIdEdTxt, memAgeEdTxt;
	private final String inputMemPhp ="inputMemdata.php";
	private final String getMemPhp ="getMemdata.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("tk_test", "onCreate");
		
		url = null;
		
		memIdEdTxt = (EditText)findViewById(R.id.id_edtxt);
		memAgeEdTxt = (EditText)findViewById(R.id.age_edtxt);
		saveBtn = (Button)findViewById(R.id.save_btn);
		getInfoBtn = (Button)findViewById(R.id.getmeminfo_btn);
		
		saveBtn.setOnClickListener(this);
		getInfoBtn.setOnClickListener(this);
		
//		HttpConnection thread = new HttpConnection();
//		thread.execute();
		
	}
	
	// Http ��� �Լ�
	// Params : ���� URL ��Ʈ��, progress bar ī���� ����
	// Return : ���� msg
	private class HttpConnection extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... urlParams) {
			// TODO Auto-generated method stub
			String urlStr;
			urlStr = urlParams[0];
			String resultMsg=null;//err ������ �ʱ�ȭ???
			try {
				Log.d("tk_test", "HttpConnection AsyncTask.....");
				Log.d("tk_test", "urlStr = "+urlStr);
				Log.d("tk_test", "urlParams[1] = "+urlParams[1]);
				
				url = new URL(urlStr);
//				url = new URL("http://ktk1015.dothome.co.kr/s2/test.php" + "?"+"memid=hahahaha&age=11");
				HttpURLConnection httpUrlConnect = (HttpURLConnection)url.openConnection();
				
				//���۸�� ����
				httpUrlConnect.setRequestMethod("GET");	// �ƹ��͵� ���� �� ���� ��� default �� = GET
				
				//���� ���� Ȯ��
				if(httpUrlConnect.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					Log.d("tk_test", "Connection OK~~~!!");
				}
				else
				{
					Log.d("tk_test", "Fail to connect~~!! : "+httpUrlConnect.getResponseCode());
				}
				
				BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnect.getInputStream()));
				buffer =new StringBuffer();
							
				int c;
				while((c = in.read()) !=-1)
				{
					buffer.append((char)c);
				}
				
				resultMsg = buffer.toString();
				Log.d("tk_test", "from server = "+resultMsg);
				
			} catch (IOException e) {
				Log.d("tk_test", "IOException = "+e);
				e.printStackTrace();
			}
						
			return resultMsg;
		}//doInBackground
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//������� �޾Ƽ� ���÷���
			// ��� ����
			// �ܼ� �ؽ�Ʈ, ȸ������ ������
			// � �Լ����� ���ߴ����� ���� ���÷��� ����� �ٲ��
			// ����,����,����,��ȸ,������� ��ȸ
			
			super.onPostExecute(result);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.save_btn:			// ȸ�� �߰�
			SaveMemInfo();
			break;
		case R.id.getmeminfo_btn:	// ȸ������ �˻�
			GetMemInfo();
		default:
			break;
		}
	}
	
	private void SaveMemInfo()
	{
		String id,toServMsg, saveUrl;
		int age;
		id = memIdEdTxt.getText().toString();
		age = Integer.parseInt(memAgeEdTxt.getText().toString());
		Log.d("tk_test", "id = "+id);
		Log.d("tk_test", "age = "+age);
		
		toServMsg = null;
		saveUrl = null;
		toServMsg = "memid="+ id + "&" + "age=" +age;
		Log.d("tk_test", "toServMsg = "+toServMsg);
		saveUrl = myHomeUrl + inputMemPhp + "?" + toServMsg;
		
		HttpConnection thread = new HttpConnection();
		thread.execute(saveUrl,"SaveMemInfo");
	}
	
	private void GetMemInfo()
	{
		String id,toServMsg, getInfoUrl;
		id = memIdEdTxt.getText().toString();
		
		toServMsg = null;
		getInfoUrl = null;
		toServMsg = "memid="+ id;
		Log.d("tk_test", "toServMsg = "+toServMsg);
		getInfoUrl = myHomeUrl + getMemPhp + "?" + toServMsg;
		
		HttpConnection thread = new HttpConnection();
		thread.execute(getInfoUrl,"GetMemInfo");
	}
	
}
