package app.phms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class NewVitals extends Activity {

	int hashValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		hashValue = extras.getInt("USER_HASH");
		setContentView(R.layout.activity_new_vitals);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_vitals, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void addToDB(View view) {
		
		//Get input values
		final EditText etWeight = (EditText) findViewById(R.id.newVitalsWeightText);
		final EditText etBP = (EditText) findViewById(R.id.newVitalBPText);
		final EditText etTemp = (EditText) findViewById(R.id.newVitalTempText);
		final EditText etGlucose = (EditText) findViewById(R.id.newVitalGlucText);
		final EditText etCholesterol = (EditText) findViewById(R.id.newVitalCholText);
		
		int weight = Integer.parseInt(etWeight.getText().toString());
		int bp = Integer.parseInt(etBP.getText().toString());
		int temp = Integer.parseInt(etTemp.getText().toString());
		int glucose = Integer.parseInt(etGlucose.getText().toString());
		int cholesterol = Integer.parseInt(etCholesterol.getText().toString());
		
		//Add to database
		//search User database for given hash
		PHMSDatabase database = new PHMSDatabase(getApplicationContext());
		database.addNewVitals(hashValue, weight, bp, temp, glucose, cholesterol);
		
		
		//Go back to Vital Signs activity once complete
		Intent intent = new Intent(this, VitalSigns.class);
    	startActivity(intent);
	}
	
}
