package net.locmap.locmap;

import net.locmap.locmap.utils.UIFunctions;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	private TextView txtDistance;
	private SeekBar sbDistance;
	private final int maxDistance = 1000; // slider/edittext cannot be set to a greater value than this
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		// init distance textfield and slider
		txtDistance = (TextView) findViewById(R.id.txtSettingsDistance);
		sbDistance = (SeekBar) findViewById(R.id.sbSettingsDistance);
		sbDistance.setMax(maxDistance - 1);
		refreshDistance(UIFunctions.getDistance(this), true);
		
		// bind seekbar to textview
		sbDistance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				saveDistance(seekBar.getProgress());
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser)
					refreshDistance(progress +1);
			}
		});
		
	}

	
	/**
	 * Saves distance to sharedpref
	 * @param value
	 */
	private void saveDistance(int value) {
		UIFunctions.setDistance(this, value);
	}
	
	
	/**
	 * "bind" TextView and SeekBar to same value
	 * @param _value Set both indicators to this value. If value < 0, sets TV to 1. If value > maxDistance, set value to maxDistance
	 */
	private void refreshDistance(int _value) {
		int value = _value;
		if (value < 1) value = 1;
		else if (value > maxDistance) value = maxDistance;
		txtDistance.setText("" + value );
	}
	
	
	private void refreshDistance(int _value, boolean progress) {
		refreshDistance(_value);
		sbDistance.setProgress(_value - 1);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
}
