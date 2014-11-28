package net.locmap.locmap;

import net.locmap.locmap.utils.Network;
import net.locmap.locmap.utils.Response;
import net.locmap.locmap.utils.UIFunctions;

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
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Functions for user to log in
 * 
 * @author Janne Heikkinen
 * @author Juuso Hatakka
 */
public class LogInActivity extends Activity {

	EditText email;
	EditText password;
	CheckBox remember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);

		email = (EditText) findViewById(R.id.editLogInEmail);
		password = (EditText) findViewById(R.id.editLogInPassword);
		remember = (CheckBox) findViewById(R.id.cbLogInRemember);
		
		fillTextFields();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}

	
	/**
	 * When returned from register-intent
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// if registered successfully, put email to textfield and clear login data from memory 
		if (data.hasExtra("email")) {
			email.setText(data.getStringExtra("email"));
			password.setText("");
			UIFunctions.clearLoginData(this);
		}
		// if not, check if account info saved to preferences
		else {
			fillTextFields();
		}
	}

	
	/**
	 * Checks if user has saved login info.
	 * Fills textfields accordingly
	 */
	private void fillTextFields() {
		String emailPre = "";
		String passwordPre = "";
		emailPre = UIFunctions.getEmail(this);
		passwordPre = UIFunctions.getPassword(this);

		if (emailPre.length() > 0 && passwordPre.length() > 0) {
			remember.setChecked(true);
		}

		email.setText(emailPre);
		password.setText(passwordPre);
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
	 * @return this activity
	 */
	private Activity getActivity() {
		return this;
	}

	/**
	 * Click event for register hyperlink. Open Register -activity
	 * 
	 * @param v
	 */
	public void onRegisterClick(View v) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * Listens to clicks on "Remember me" -checkbox. If box get's unchecked,
	 * clear login data from SharedPreferences
	 * 
	 * @param v
	 */
	public void onCbRememberClick(View v) {
		CheckBox cb = (CheckBox) v;
		if (!cb.isChecked()) {
			UIFunctions.clearLoginData(this);
			Log.d("LOGIN CLEARED", "Login data cleared at cbRememberClick");
		}
	}

	/**
	 * Click event for Log In -button. Save email and password only if checkbox
	 * checked and login successful
	 * 
	 * @param v
	 */
	public void btnLogIn(View v) {
		String emailText = email.getText().toString();
		String pwText = password.getText().toString();
		String[] params = { emailText, pwText };

		new LogIn().execute(params);
	}

	/**
	 * Sends LogIn request to API. Execute needs two parameters.
	 * 
	 * 1. parameter: Email 2. parameter: Password
	 */
	public class LogIn extends AsyncTask<String, Void, Response> {

		/**
		 * Converts string data to JSON and calls Network.Post Returns HTTP
		 * response content as string
		 */
		@Override
		protected Response doInBackground(String... params) {
			if (params.length < 2)
				this.cancel(true);

			String json = "";
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.accumulate("email", params[0]);
				jsonObj.accumulate("password", params[1]);
				json = jsonObj.toString();
			} catch (JSONException ex) {
				Log.d("JSON Convert", "String to JSON fail @ login");
			}

			return Network.Post(Network.loginUrl, json);

		}

		/**
		 * Save access token if login successful. Else show error message. Save
		 * email and password if user has checked Remember me -checkbox
		 */
		@Override
		protected void onPostExecute(Response res) {

			String token = res.getHeader("x-access-token");
			// login successful, save token and possibly login info
			if (token != null) {
				String username = "";
				try {
					JSONObject body = new JSONObject(res.getBody());
					username = body.getString("username");
				} catch (JSONException ex) {
					Log.d("JSON ERROR", ex.getMessage());
				}
				UIFunctions.saveToken(getActivity(), token, username);
				UIFunctions.showToast(getActivity(),
						getString(R.string.logged_in));
				CheckBox remember = (CheckBox) findViewById(R.id.cbLogInRemember);

				if (remember.isChecked()) {
					UIFunctions.saveLoginInfo(getActivity(), email.getText()
							.toString(), password.getText().toString());
				}
				getActivity().finish();
			} else {
				String msg = UIFunctions.getErrors(getActivity(), res,
						getString(R.string.login_fail));
				if (msg == null)
					msg = res.getBody();
				UIFunctions.showOKDialog(msg, getActivity());
			}

		}
	}

}
