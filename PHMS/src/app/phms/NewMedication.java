package app.phms;

import java.sql.Date;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NewMedication extends Activity {

	int userHashValue = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_medication);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			userHashValue = extras.getInt("USER_HASH");
		
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
		final TextView tvMedName = (TextView) findViewById(R.id.medName);
		final TextView tvMedSpecial = (TextView) findViewById(R.id.medSpecial);
		final TextView tvRefillsLeft = (TextView) findViewById(R.id.refillsLeft);
		final TextView tvRefillMonth = (TextView) findViewById(R.id.refillMonth);
		final TextView tvRefillDay = (TextView) findViewById(R.id.refillDay);
		final TextView tvRefillYear = (TextView) findViewById(R.id.refillYear);
		
		String medName = tvMedName.getText().toString();
		String special = tvMedSpecial.getText().toString();
		String refills = tvRefillsLeft.getText().toString();
		String month = tvRefillMonth.getText().toString();
		String day = tvRefillDay.getText().toString();
		String year = tvRefillYear.getText().toString();
		
		if( medName.equals(null) ||
			refills.equals(null) ||
			month.equals(null) ||
			day.equals(null) ||
			year.equals(null) )
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
			PHMSDatabase database = new PHMSDatabase(null);
			database.addNewMed(userHashValue, medName, special, Date.valueOf(year+'-'+month+'-'+day), Integer.parseInt(refills));
			
			//Go back to Medications Listings
			Intent intent = new Intent(this, Medications.class);
			startActivity(intent);
		}
	}

}

