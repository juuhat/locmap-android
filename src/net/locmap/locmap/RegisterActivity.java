package net.locmap.locmap;

import org.json.JSONException;
import org.json.JSONObject;

import net.locmap.locmap.utils.Network;
import net.locmap.locmap.utils.Response;
import net.locmap.locmap.utils.UIFunctions;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

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
	
	/**
	 * @return RegisterActivity
	 */
	private Activity getActivity() {
		return this;
	}
	
	/**
	 * Gets user input values and send register request to API
	 * @param v
	 */
	public void btnRegister(View v) {
		
		EditText email = (EditText) findViewById(R.id.editRegisterEmail);
		EditText user = (EditText) findViewById(R.id.editRegisterUser);
		EditText password = (EditText) findViewById(R.id.editRegisterPassword);
		EditText passwordConf = (EditText) findViewById(R.id.editRegisterPasswordConfirmation);
		
		/**
		 ********  CHECK INPUT  ******** 
		 
		
		// check that email fills necessary criteria
		if ( !UIFunctions.isValidEmail(email.getText().toString()) ) {
			UIFunctions.showOKDialog(getString(R.string.check_email), this);
			return;
		}
		
		// check that username has only A-Z chars and numbers
		String username = user.getText().toString();
		if (!username.matches("^[A-Za-z0-9ƒ‰÷ˆ≈Â]+$")) {
			UIFunctions.showOKDialog(getString(R.string.username_characters), this);
			return;
		}
		
		// check that password at least 8 char long
		String pw = password.getText().toString(); 
		if (pw.length() < 8) {
			UIFunctions.showOKDialog(getString(R.string.password_length), this);
			return;
		}
		
		// check that password contains at least one letter and one number
		if (!(pw.matches(".*[A-Za-z].*") && pw.matches(".*[0-9].*")) ) {
			UIFunctions.showOKDialog(getString(R.string.password_alphanum), this);
			return;
		}
		
		// also check that passwords match
		if (!pw.equals(passwordConf.getText().toString())) {
			UIFunctions.showOKDialog(getString(R.string.password_match), this);
			return;
		}
		
		
		/**
		 ***********   INPUT CHECKED    ***********
	     */
		
		
		String[] params = {email.getText().toString() , user.getText().toString(), password.getText().toString()};
		
		// if connected to Internet..
		if (Network.isNetworkAvailable(this)){
			new Register().execute(params);
		}
		else {
			UIFunctions.showOKDialog(getString(R.string.check_internet), this);
		}
	}
	
    /**
     * Async task for register.
     * Needs three parameters:
     * 
     * 1. parameter: Email
     * 2. parameter: Username
     * 3. parameter: Password
     */
	public class Register extends AsyncTask<String, Void, Response> {

		/**
		 * Converts String data to JSON
		 * Calls Network.Post for registering
		 */
		@Override
		protected Response doInBackground(String... params) {
			if (params.length < 3)
				this.cancel(true);
			
			String json = "";
			JSONObject jsonObj = new JSONObject();
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
		
		/**
		 * Updates UI after response has arrived from API
		 */
		@Override
		protected void onPostExecute(Response res) {
			int statuscode = res.getStatusCode();
			String body = res.getBody();
			String msg = "";
			
			// check how request went and update UI accordingly
			if (statuscode == 200) {
				msg = getString(R.string.register_ok);
				String emailPrep = "";
				try {
					// get email from response so that it can be prefilled to login form
					JSONObject respBody = new JSONObject(body);
					emailPrep = respBody.get("email").toString();
				} catch (JSONException ex) {
					Log.d("JSON Convert", "JSON to String failed @ register extract email");
				}
				
				Intent login = new Intent();
				login.putExtra("email", emailPrep);
				setResult(Activity.RESULT_OK, login);
				getActivity().finish();
			}
			else if (statuscode >= 500 && statuscode < 600) {
				msg = getString(R.string.internal_problems);
			}
			// API error for trying to register with reserved email/username
			// is kind of cryptic, so we need to check the body for some keywords
			else if (body.contains("duplicate key")) {
				if (body.contains("locmap.users.$username")) {
					msg = getString(R.string.username_reserved);
				}
				else msg = getString(R.string.email_reserved);			
			} 
			else msg = body;
			
			UIFunctions.showToast(getActivity(),msg);
			
		}
		
	}
}
