package net.locmap.locmap.utils;

import net.locmap.locmap.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

/***
 * Class that wraps small dialog functions and such
 * TODO: Function that re-logins if token is not valid anymore
 *       and if re-login is not possible (remember me not checked)
 *       clears token from SharedPreferences and shows dialog with appropriate message
 * @author Janne Heikkinen
 */
public class UIFunctions {
	
	
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
		editor.remove("email");
		editor.remove("password");
		editor.commit();
	}


	/**
	 * Gets password that user has selected this app to remember
	 * @param current Activity
	 * @return password, if defined. Empty string if not
	 */
	public static String getPassword(Activity current) {
		SharedPreferences sharedPref = current.getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getString("password", "");
	}
	
	
	/**
	 * Gets email that user has selected this app to remember
	 * @param current Activity
	 * @return email, if any. Empty string if not defined
	 */
	public static String getEmail(Activity current) {
		SharedPreferences sharedPref = current.getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getString("email", "");
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
		editor.putString("email", email);
		editor.putString("password", password);
		editor.commit();
	}
}
