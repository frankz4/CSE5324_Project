package app.phms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewVitals extends Activity {

	int userHashValue = 0;
	int use = -1;
	int position = -1;
	Cursor c;

	TextView title;
	TextView tvMonth;
	TextView tvDay;
	TextView tvYear;
	TextView tvWeight;
	TextView tvBP;
	TextView tvTemp;
	TextView tvGlucose;
	TextView tvCholesterol;
	
	Button btnVital;
	Button btnClear;
	
	PHMSDatabase database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_vitals);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		title = (TextView) findViewById(R.id.vitalTitle);
		tvMonth = (TextView) findViewById(R.id.vitalMonth);
		tvDay = (TextView) findViewById(R.id.vitalDay);
		tvYear = (TextView) findViewById(R.id.vitalYear);
		tvWeight = (TextView) findViewById(R.id.newVitalsWeightText);
		tvBP = (TextView) findViewById(R.id.newVitalBPText);
		tvTemp = (TextView) findViewById(R.id.newVitalTempText);
		tvGlucose = (TextView) findViewById(R.id.newVitalGlucText);
		tvCholesterol = (TextView) findViewById(R.id.newVitalCholText);
		
		btnVital = (Button) findViewById(R.id.btnNewVital);
		btnClear = (Button) findViewById(R.id.btnVitalClear);
		
		database = new PHMSDatabase(this);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onResume(){
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			userHashValue = extras.getInt("USER_HASH");
			use = extras.getInt("USE");
			position = extras.getInt("VITAL_POSITION");
			
			//If we are viewing a current doctor, fill it in			
			if(    ( use == MainActivity.VIEW ) 
				&& ( position != -1 ) ) {				
				c = database.getVitals(userHashValue);
				c.moveToPosition(position);
				
				this.title.setText("Update Vital Signs Entry");
				
				this.tvBP.setText(c.getString(VitalSigns.VITAL_BP));
				this.tvCholesterol.setText(c.getString(VitalSigns.VITAL_CHOL));
				this.tvGlucose.setText(c.getString(VitalSigns.VITAL_GLU));
				this.tvTemp.setText(c.getString(VitalSigns.VITAL_TEMP));
				this.tvWeight.setText(c.getString(VitalSigns.VITAL_WEIGHT));
				String date = c.getString(VitalSigns.VITAL_DATE);
				this.tvMonth.setText(date.substring(0, 2));
				this.tvDay.setText(date.substring(3, 5));
				this.tvYear.setText(date.substring(6, 10));
				
				this.btnVital.setText("Update");
				
				this.btnClear.setVisibility(View.INVISIBLE);
			}
			else{
				this.title.setText("New Vital Signs Entry");
				
				this.tvBP.setText("");
				this.tvCholesterol.setText("");
				this.tvDay.setText("");
				this.tvGlucose.setText("");
				this.tvMonth.setText("");
				this.tvTemp.setText("");
				this.tvWeight.setText("");
				this.tvYear.setText("");
				
				this.btnVital.setText("Add New");
				this.btnClear.setVisibility(View.VISIBLE);
			}
		}
		super.onResume();
	}
	
	@Override
	protected void onPause(){		
		Bundle bundle = new Bundle();
		bundle.putInt("USER_HASH", this.userHashValue);
		onSaveInstanceState(bundle);
		
		super.onPause();
	}

	@Override
	protected void onRestart(){
		super.onRestart();
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
		String month = this.tvMonth.getText().toString();
		String day = this.tvDay.getText().toString();
		String year = this.tvYear.getText().toString();
		String date =  month + '/' + day + '/' + year;					  
		String weight = this.tvWeight.getText().toString();
		String bp = this.tvBP.getText().toString();
		String temp = this.tvTemp.getText().toString();
		String glucose = this.tvGlucose.getText().toString();
		String cholesterol = this.tvCholesterol.getText().toString();
		
		Context context = getApplicationContext();
		CharSequence text = "Please fill in all required fields!";
		int duration = Toast.LENGTH_LONG;
		
		if( month.isEmpty() || day.isEmpty() || year.isEmpty() )
			text = "Error in date!";
		else if( (Integer.parseInt(month) > 12) || (Integer.parseInt(month) < 1) ||
				(Integer.parseInt(day) > 31) || (Integer.parseInt(day) < 1) ||
				(Integer.parseInt(year) < 2000) || (Integer.parseInt(year) > 9999) )
			text = "Error in date!";
		else if ( weight.isEmpty() && 
				  bp.isEmpty() && 
				  temp.isEmpty() && 
				  glucose.isEmpty() && 
				  cholesterol.isEmpty() ){
			text = "Verify at least one field is filled!";
		}
		else{
			try{
				if(use == MainActivity.NEW){
					//Add to database
					database.addNewVitals(userHashValue, date, weight, bp, temp, glucose, cholesterol);		
					text = "Vital Sign Entry Saved!";
				}
				else if (use == MainActivity.VIEW){
					//for updating an entry
					database.updateVitals(userHashValue, date, weight, bp, temp, glucose, cholesterol);
					text = "Vital Sign Entry Updated!";
				}
				
				//Go back to Vital Signs activity once complete
				Intent intent = new Intent(this, VitalSigns.class);
				intent.putExtra("USER_HASH", this.userHashValue);
		    	startActivity(intent);
			}
			catch(android.database.sqlite.SQLiteException ex){
				text = "Error Occured in Database!";
			}
		}
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public void clearFields( View view ){
		if( this.use == MainActivity.NEW ){
			this.tvBP.setText("");
			this.tvCholesterol.setText("");
			this.tvGlucose.setText("");
			this.tvTemp.setText("");
			this.tvWeight.setText("");
			this.tvDay.setText("");
			this.tvMonth.setText("");
			this.tvYear.setText("");
		}
		else if ( this.use == MainActivity.VIEW ){
			//put code to delete entry
		}
	}
}
