package net.locmap.locmap;

import net.locmap.locmap.models.Location;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowLocationActivity extends Activity {
	
	private Location location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_location);

		this.location = (Location) getIntent().getExtras().getParcelable("location");
		updateTextViews();
	}
	
	private void updateTextViews() {
		TextView title = (TextView) findViewById(R.id.txtShowLocationTitle);
		TextView description = (TextView) findViewById(R.id.txtShowLocationDescription);
		
		title.setText(location.getTitle());
		description.setText(location.getDescription());
	}
	
}
