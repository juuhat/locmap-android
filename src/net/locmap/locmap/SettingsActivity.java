package net.locmap.locmap;

import net.locmap.locmap.utils.InputFilterMinMax;
import net.locmap.locmap.utils.UIFunctions;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SettingsActivity extends Activity {

	private EditText txtDistance;
	private SeekBar sbDistance;
	private final int maxDistance = 1000; // slider/edittext cannot be set to a greater value than this
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		// init distance textfield and slider
		txtDistance = (EditText) findViewById(R.id.txtSettingsDistance);
		sbDistance = (SeekBar) findViewById(R.id.sbSettingsDistance);
		sbDistance.setMax(maxDistance - 1);
		refreshDistance(UIFunctions.getDistance(this));
		
		sbDistance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				refreshDistance(progress +1);
			}
		});
		txtDistance.setFilters(new InputFilter[]{ new InputFilterMinMax(1, maxDistance)});
		txtDistance.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				refreshDistance(Integer.parseInt(txtDistance.getText().toString()));
				return true;
			}
		});
		
	}

	
	/**
	 * "bind" EditText and seekbar to same value
	 * @param _value Set both indicators to this value. If value < 0, does nothing. If value > maxDistance, set value to maxDistance
	 */
	private void refreshDistance(int _value) {
		int value = _value;
		if (value <= 0)
			return;
		if (value >  maxDistance)
			value = maxDistance;
		txtDistance.setText("" + value);
		sbDistance.setProgress(value - 1);
	
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
