package app.phms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NewAppointments extends Activity {

	int userHashValue = 0;
	int use = -1;
	int apt_position = -1;
	Cursor c;
	
	PHMSDatabase database;
	TimePicker tpTime;
	TextView tvMonth;
	TextView tvDay;
	TextView tvYear;
	TextView tvLocation;
	ListView lvDoctor;
	String chosenDoctor = "";
	String docLocation = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_appointments);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			userHashValue = extras.getInt("USER_HASH");
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		this.database = new PHMSDatabase(this);
		
		this.tpTime = (TimePicker) findViewById(R.id.aptTimePicker);
		this.tvMonth = (TextView) findViewById(R.id.aptMonth);
		this.tvDay = (TextView) findViewById(R.id.aptDay);
		this.tvYear = (TextView) findViewById(R.id.aptYear);
		this.tvLocation = (TextView) findViewById(R.id.aptLocation);
		this.lvDoctor = (ListView) findViewById(R.id.aptDoctorsList);
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		c = database.getDocs(userHashValue);

		if (c.getCount() > 0) {
			c.moveToFirst();

			while (!c.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				item.put("doctor", c.getString(Doctors.DOCTOR_NAME));
				if (!c.getString(Doctors.DOCTOR_SPEC).isEmpty())
					item.put("extra", "Specialty: " + c.getString(Doctors.DOCTOR_SPEC) + "  |  Phone: "+ c.getString(Doctors.DOCTOR_PHONE));
				else
					item.put("extra", "Phone: " + c.getString(Doctors.DOCTOR_PHONE));

				list.add(item);
				c.moveToNext();
			}

			String[] from = new String[] { "doctor", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;

			this.lvDoctor = (ListView) findViewById(R.id.doctorListView);
			this.lvDoctor.setClickable(true);

			this.lvDoctor.setAdapter(new SimpleAdapter(this, list, nativeLayout, from, to));
			this.lvDoctor.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					use = MainActivity.VIEW;
					apt_position = position;
					c.moveToPosition(apt_position);
					chosenDoctor = c.getString(Doctors.DOCTOR_NAME);
					docLocation = c.getString(Doctors.DOCTOR_ADDR1) 
							    + ", " + c.getString(Doctors.DOCTOR_CITY) 
							    + ", " + c.getString(Doctors.DOCTOR_STATE);
				}
			});
		} 
		else {
			Context context = getApplicationContext();
			CharSequence text = "No doctor entries found.";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
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
	
	public void addNewUpdate (View view){
		this.tpTime = (TimePicker) findViewById(R.id.aptTimePicker);
		this.tvMonth = (TextView) findViewById(R.id.aptMonth);
		this.tvDay = (TextView) findViewById(R.id.aptDay);
		this.tvYear = (TextView) findViewById(R.id.aptYear);
		this.tvLocation = (TextView) findViewById(R.id.aptLocation);
		this.lvDoctor = (ListView) findViewById(R.id.aptDoctorsList);
		
		String stTime = this.tpTime.toString();
		String stMonth = this.tvMonth.getText().toString();
		String stDay = this.tvDay.getText().toString();
		String stYear = this.tvYear.getText().toString();
		String stLocation = this.tvLocation.getText().toString();
		String stDoctor = this.lvDoctor.toString();
		
		Context context = getApplicationContext();
		CharSequence text = "Please fill in all required fields!";
		int duration = Toast.LENGTH_LONG;
		
		if( stMonth.isEmpty() || stDay.isEmpty() || stYear.isEmpty() )
			text = "Error in date!";
		else if( (Integer.parseInt(stMonth) > 12) || (Integer.parseInt(stMonth) < 1) ||
				(Integer.parseInt(stDay) > 31) || (Integer.parseInt(stDay) < 1) ||
				(Integer.parseInt(stYear) > 2012) || (Integer.parseInt(stYear) < 9999) )
			text = "Error in date!";
		else if( stTime.isEmpty() )
			text = "Please fill in all required fields!";
		else
		{
			try{
				String stDate = stMonth + "/" + stDay + "/" + stYear;
				if(use == MainActivity.NEW){					
					database.addNewApt(userHashValue, stDoctor, stDate, stTime, stLocation );
					text = "Appointment Entry Saved!";
				}
				else if (use == MainActivity.VIEW){
					//for updating an entry
					text = "Appointment Entry Updated!";
				}
				
				//Create Calendar Event
				Intent calIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
				
				//Add event details
				calIntent.putExtra(CalendarContract.Events.TITLE, "Doctors Appointment with " + stDoctor);
				calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Location " + stLocation);
				
				Calendar startTime = Calendar.getInstance();
				startTime.set(Integer.parseInt(stYear), Integer.parseInt(stMonth), Integer.parseInt(stDay));
				
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis());
				
				//Start Calendar Activity to set event
				startActivity(calIntent);
				
				//Go back to appointments
				Intent intent = new Intent(this, Appointments.class);
				intent.putExtra("USER_HASH", userHashValue);
				startActivity(intent);
			}
			catch (android.database.sqlite.SQLiteException ex) {
				text = "Database Error, Please Try Again...";
			}
		}
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}
