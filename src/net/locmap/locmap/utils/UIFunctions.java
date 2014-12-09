package net.locmap.locmap.utils;

import org.json.JSONException;
import org.json.JSONObject;

import net.locmap.locmap.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

/***
 * Class that wraps small dialog functions and such
 * TODO: Function that re-logins if token is not valid anymore
 *       and if re-login is not possible (remember me not checked)
 *       clears token from SharedPreferences and shows dialog with appropriate message
 * @author Janne Heikkinen
 */
public class UIFunctions {
	private static final String prefKey = "user"; // SharedPreferences key
	private static final String tokenKey = "token";
	private static final String emailKey = "email";
	private static final String pwKey = "password";
	private static final String usernameKey = "username";
	private static final String idKey = "id";
	private static final String distanceKey = "distance";
	private static int defaultDistance = 300;
	
	
	/**
	 * Sets distance from within locations are fetched 
	 * @param current Activity
	 * @param distance Distance in kilometers
	 */
	public static void setDistance(Activity current, int distance) {
		SharedPreferences sharedPref = current.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(distanceKey, distance);
		editor.commit();
	}
	
	/**
	 * Gets maximum distance where to look for locations
	 * @param current Activity
	 * @return Distance in kilometers. -1 if preference not set
	 */
	public static int getDistance(Activity current) {
		SharedPreferences sharedPref = current.getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getInt(distanceKey, -1);
	}
	/**
	 * Puts modal dialog to display. Only button available is OK which closes dialog
	 * @param msg Message to show in dialog
	 * @param act Current activity
	 */
	public static void showOKDialog(String msg, Activity act) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do nothing
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}

	
	/**
	 * Just checks that there's no whitespace and that @-char and .-char is somewhere
	 * More info why I don't wan't to make this too strict:
	 * http://haacked.com/archive/2007/08/21/i-knew-how-to-validate-an-email-address-until-i.aspx/
	 * @param email email to "validate"
	 * @return true if valid email
	 */
	public static boolean isValidEmail(String email) {
		return email.contains("@") && email.contains(".") && !email.contains(" ");
	}


	/**
	 * Check response for errors. Checks if response has
	 * - Internal server errors ( 500-599)
	 * - Problems with connecting to API (401-499)
	 * - Bad request 400
	 * @param res Response to check
	 * @param error Returns this if response has statuscode 400
	 * @return Error message from string resources, or null if statuscode successful
	 */
	public static String getErrors(Activity current, Response res, String error) {
		String msg = null;
		int statuscode = res.getStatusCode();
		if (statuscode > 400 && statuscode < 600) {
			msg = current.getString(R.string.internal_problems);
		} else if (statuscode == 400) {
			msg = error;
		}
		return msg;
	}


	/**
	 * Clears login info from SharedPreferences
	 * @param current current activity
	 */
	public static void clearLoginData(Activity current) {
		SharedPreferences sharedPref = current.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(emailKey);
		editor.remove(pwKey);
		editor.commit();
	}


	/**
	 * Gets password that user has selected this app to remember
	 * @param current Activity
	 * @return password, if defined. Empty string if not
	 */
	public static String getPassword(Activity current) {
		SharedPreferences sharedPref = current.getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getString(pwKey, "");
	}
	
	
	/**
	 * Gets email that user has selected this app to remember
	 * @param current Activity
	 * @return email, if any. Empty string if not defined
	 */
	public static String getEmail(Activity current) {
		SharedPreferences sharedPref = current.getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getString(emailKey, "");
	}


	/**
	 * Saves email and password to SharedPreferences
	 * @param current Activity
	 * @param email Valid email
	 * @param password User password
	 */
	public static void saveLoginInfo(Activity current, String email, String password) {
		SharedPreferences sharedPref = current.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(emailKey, email);
		editor.putString(pwKey, password);
		editor.commit();
	}

	
	/**
	 * Saves token in SharedPreferences
	 * @param current Activity
	 * @param token Acces-token
	 * @param user Username who owns this token
	 * @param id ObjectId for user
	 */
	public static void saveToken(Activity current, String token, String username, String id) {
		SharedPreferences sharedPref = current.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(tokenKey, token);
		editor.putString(usernameKey, username);
		editor.putString(idKey, id);
		editor.commit();
	}

	/**
	 * Gets access-token
	 * @param current Activity
	 * @return Access-token if available, otherwise null
	 */
	public static String getToken(Activity current) {
		SharedPreferences sharedPref = current.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		return sharedPref.getString(tokenKey, null);
	}
	
	
	/**
	 * Gets logged in user's ObjectId
	 * @param current Activity
	 * @return id if available, otherwise null
	 */
	public static String getId(Activity current) {
		SharedPreferences sharedPref = current.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		return sharedPref.getString(idKey, null);
	}
	
	
	/**
	 * Shows short toast
	 * @param activity current
	 * @param msg to toast
	 */
	public static void showToast(Activity activity, String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}


	/**
	 * Clears auth-token and username from sharedpref
	 * @param current Activity
	 */
	public static void clearToken(Activity current) {
		SharedPreferences sharedPref = current.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
		sharedPref.edit().clear().commit();
	}

	/**
	 * Inits max distance in preferences if it is not set.
	 * @param current Activity
	 */
	public static void initDistance(Activity current) {
		if (getDistance(current) <= 0)
			setDistance(current, defaultDistance);
	}
	
	
	/**
	 * Parse message contained in API response
	 * @param json
	 */
	public static String parseJsonMessage(String json) {
		try {
			JSONObject jsonObj = new JSONObject(json);
			return jsonObj.getString("message");
		} catch (JSONException e) {
			return "Invalid response from API";
		}
	}
	
}
