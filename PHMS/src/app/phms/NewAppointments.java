package app.phms;

import java.sql.Date;
import java.sql.Time;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewAppointments extends Activity {

	int userHashValue = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_appointments);
		
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
		getMenuInflater().inflate(R.menu.new_appointments, menu);
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
	
	public void addNewAppointment (View view){
		TextView tvTime = (TextView) findViewById(R.id.aptTimePicker);
		TextView tvDate = (TextView) findViewById(R.id.aptDate);
		TextView tvLocation = (TextView) findViewById(R.id.aptLocation);
		ExpandableListView elvDoctor = (ExpandableListView) findViewById(R.id.aptExpDoctorsList);
		
		String stTime = tvTime.getText().toString();
		String stDate = tvDate.getText().toString();
		String stLocation = tvLocation.getText().toString();
		String stDoctor = elvDoctor.toString();
		
		if( stTime.equals(null) || stDate.equals(null))
		{
			Context context = getApplicationContext();
			CharSequence text = "Please fill in all required fields!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			//Store information in Database
			PHMSDatabase database = new PHMSDatabase(null);
			database.addNewApt(userHashValue, stDoctor, Date.valueOf(stDate), Time.valueOf(stTime), stLocation );
		}
		
		//Go back to appointments
		Intent intent = new Intent(this, Appointments.class);
		startActivity(intent);
	}

}
