package net.locmap.locmap;

import org.json.JSONException;
import org.json.JSONObject;

import net.locmap.locmap.utils.Network;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void btnRegister(View v) {
		
		EditText email = (EditText) findViewById(R.id.editRegisterEmail);
		EditText user = (EditText) findViewById(R.id.editRegisterUser);
		EditText password = (EditText) findViewById(R.id.editRegisterPassword);
		String[] params = {email.getText().toString() , user.getText().toString(), password.getText().toString()};
		
		new Register().execute(params);
	}
	
    /**
     * Async task for register.
     * Needs three parameters:
     * 
     * 1. parameter: Email
     * 2. parameter: Username
     * 3. parameter: Password
     */
	public class Register extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			if (params.length < 3)
				return "";
			
			String json = "";
			JSONObject jsonObj = new JSONObject();
			// TODO : Add valid for email and stuff ??
			try {
				jsonObj.accumulate("email", params[0]);
				jsonObj.accumulate("username", params[1]);
				jsonObj.accumulate("password", params[2]);
				json = jsonObj.toString();
			} catch (JSONException e) {
				Log.d("JSON convert", "String to JSON fail @ register");
			}
			
			return Network.Post(Network.registerUrl, json);
		}
		
		@Override
		protected void onPostExecute(String result) {
			TextView registerResult = (TextView) findViewById(R.id.txtRegisterResult);
			registerResult.setText(result);
		}
		
	}
}
