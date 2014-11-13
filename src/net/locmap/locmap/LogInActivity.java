package net.locmap.locmap;

import net.locmap.locmap.utils.Network;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Functions for user to log in
 * @author Janne Heikkinen
 */
public class LogInActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
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
	
	/**
	 * Click event for register hyperlink. Open Register -activity
	 * @param v
	 */
	public void onRegisterClick(View v) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
	
	
	/**
	 * Click event for Log In -button.
	 * @param v
	 */
	public void btnLogIn(View v) {
		EditText username = (EditText) findViewById(R.id.editLogInUser);
		EditText password = (EditText) findViewById(R.id.editLogInPassword);
		
		String[] params = {username.getText().toString(), password.getText().toString()};
		
		new LogIn().execute(params);
	}
	
	
	/**
	 * Sends LogIn request to API.
	 * Execute needs two parameters.
	 * 
	 *  1. parameter: Username
	 *  2. parameter: Password
	 */
	public class LogIn extends AsyncTask<String, Void, String> {

		/**
		 * Converts string data to JSON and calls Network.Post
		 * Returns HTTP response content as string
		 */
		@Override
		protected String doInBackground(String... params) {
			if (params.length < 2) 
				return "";
			
			String json = "";
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.accumulate("email", params[0]);
				jsonObj.accumulate("password", params[1]);
				json = jsonObj.toString();
			} catch (JSONException ex) {
				Log.d("JSON Convert", "String to JSON fail @ login");
			}
			if (json != "")
				return Network.Post(Network.loginUrl, json);
			return "Error while logging in";
		}
		
		/**
		 * TODO: Parse access token, do something clever
		 */
		@Override
		protected void onPostExecute(String result) {
			TextView resultView = (TextView) findViewById(R.id.txtLogInResult);
			resultView.setText(result);
		}
	}
	
}
