package app.phms;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NewMedication extends Activity {

	int userHashValue = 0;
	int use = -1;
	int position = -1;
	Cursor c;
	
	TextView tvMedName;
	TextView tvMedSpecial;
	TextView tvRefillsLeft;
	TextView tvRefillMonth;
	TextView tvRefillDay;
	TextView tvRefillYear;
	
	Button btnMeds;
	Button btnClear;
	
	PHMSDatabase database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_medication);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			userHashValue = extras.getInt("USER_HASH");
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		this.tvMedName = (TextView) findViewById(R.id.medName);
		this.tvMedSpecial = (TextView) findViewById(R.id.medSpecial);
		this.tvRefillsLeft = (TextView) findViewById(R.id.refillsLeft);
		this.tvRefillMonth = (TextView) findViewById(R.id.refillMonth);
		this.tvRefillDay = (TextView) findViewById(R.id.refillDay);
		this.tvRefillYear = (TextView) findViewById(R.id.refillYear);
		
		btnMeds = (Button) findViewById(R.id.btnNewMed);
		btnClear = (Button) findViewById(R.id.btnMedClear);
		
		database = new PHMSDatabase(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		if (extras != null){
			userHashValue = extras.getInt("USER_HASH");
			use = extras.getInt("USE");
			position = extras.getInt("MED_POSITION");
			
			//If we are viewing a current doctor, fill it in			
			if( ( use == MainActivity.VIEW ) &&
				( position != -1 ) ) {				
				c = database.getDiet(userHashValue);
				c.moveToPosition(position);
				
				this.tvMedName.setText(c.getString(Medications.MED_NAME));
				this.tvMedSpecial.setText(c.getString(Medications.MED_SPEC));
				this.tvRefillsLeft.setText(c.getString(Medications.MED_REFILLS));
				
				String date = c.getString(Medications.MED_DATE);
				this.tvRefillMonth.setText(date.substring(0, 1));
				this.tvRefillDay.setText(date.substring(3, 4));
				this.tvRefillYear.setText(date.substring(6, 9));
				
				this.btnMeds.setText("Update");
				this.btnClear.setEnabled(false);
			}
			else{
				this.tvMedName.setText("");
				this.tvMedSpecial.setText("");
				this.tvRefillsLeft.setText("");
				this.tvRefillMonth.setText("");
				this.tvRefillDay.setText("");
				this.tvRefillYear.setText("");
				
				this.btnMeds.setText("Add New");
				this.btnClear.setEnabled(true);
			}
		}
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
		getMenuInflater().inflate(R.menu.new_medication, menu);
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
	
	public void addNewMedication(View view){
		/*
		this.tvMedName = (TextView) findViewById(R.id.medName);
		this.tvMedSpecial = (TextView) findViewById(R.id.medSpecial);
		this.tvRefillsLeft = (TextView) findViewById(R.id.refillsLeft);
		this.tvRefillMonth = (TextView) findViewById(R.id.refillMonth);
		this.tvRefillDay = (TextView) findViewById(R.id.refillDay);
		this.tvRefillYear = (TextView) findViewById(R.id.refillYear);
		*/
		
		String medName = this.tvMedName.getText().toString();
		String special = this.tvMedSpecial.getText().toString();
		String refills = this.tvRefillsLeft.getText().toString();
		String month = this.tvRefillMonth.getText().toString();
		String day = this.tvRefillDay.getText().toString();
		String year = this.tvRefillYear.getText().toString();
		
		if( medName.isEmpty() ||
			refills.isEmpty() ||
			month.isEmpty() ||
			day.isEmpty() ||
			year.isEmpty() )
		{
			Context context = getApplicationContext();
			CharSequence text = "Please fill in all required fields!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			
			//Create Calendar Event
			Intent calIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
			
			//Add event details
			calIntent.putExtra(CalendarContract.Events.TITLE, "Refill " + medName);
			calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Remember to refill your medication: " + medName);
			
			Calendar startTime = Calendar.getInstance();
			startTime.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
			
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis());
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
			
			//Start Calendar Activity to set event
			startActivity(calIntent);
			
			//Store information in Database
			database.addNewMed(userHashValue, medName, special, year+'/'+month+'/'+day, refills);
			
			//Go back to Medications Listings
			Intent intent = new Intent(this, Medications.class);
			intent.putExtra("USER_HASH", userHashValue);
			startActivity(intent);
		}
	}

	public void clearFields(View view){
		this.tvMedName.setText("");
		this.tvMedSpecial.setText("");
		this.tvRefillsLeft.setText("");
		this.tvRefillMonth.setText("");
		this.tvRefillDay.setText("");
		this.tvRefillYear.setText("");
	}
}