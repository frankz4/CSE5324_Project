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
import android.widget.EditText;
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
	Cursor doc_c;
	
	TextView title;
	PHMSDatabase database;
	TimePicker tpTime;
	EditText tvMonth;
	EditText tvDay;
	EditText tvYear;
	EditText tvLocation;
	ListView lvDoctor;
	String chosenDoctor = "";
	String docLocation = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_appointments);
		
		// Show the Up button in the action bar.
		//setupActionBar();
		
		database = new PHMSDatabase(this);
		
		title = (TextView) findViewById(R.id.aptTitle);
		tpTime = (TimePicker) findViewById(R.id.aptTimePicker);
		tvMonth = (EditText) findViewById(R.id.aptMonth);
		tvDay = (EditText) findViewById(R.id.aptDay);
		tvYear = (EditText) findViewById(R.id.aptYear);
		tvLocation = (EditText) findViewById(R.id.aptLocation);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onResume(){
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			userHashValue = extras.getInt("USER_HASH");
			use = extras.getInt("USE");
		}
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		doc_c = database.getDocs(userHashValue);

		apt_position = extras.getInt("APT_POSITION"); 
		
		int hour, minute;

		//If we are viewing a current doctor, fill it in			
		if( ( use == MainActivity.VIEW ) &&
			( apt_position != -1 ) ) {				
			c = database.getApts(userHashValue);
			c.moveToPosition(apt_position);
			
			String date = c.getString(Appointments.APT_DATE);
			hour = Integer.parseInt(c.getString(Appointments.APT_TIME).substring(0, 1));
			minute = Integer.parseInt(c.getString(Appointments.APT_TIME).substring(3, 4));
			
			this.title.setText("Update Appointment Entry");
			this.tpTime.setCurrentHour(hour);
			this.tpTime.setCurrentMinute(minute);
			this.tvMonth.setText(date.substring(0, 1));
			this.tvDay.setText(date.substring(3, 4));
			this.tvYear.setText(date.substring(6, 9));
			this.tvLocation.setText(c.getString(Appointments.APT_LOC));
		}
		else{
			Calendar cal = Calendar.getInstance();
			hour = cal.get(Calendar.HOUR);
			minute = cal.get(Calendar.MINUTE);
			
			title.setText("New Appointment Entry");
			tpTime.setCurrentHour(hour);
			tpTime.setCurrentMinute(minute);
			tvMonth.setText("");
			tvDay.setText("");
			tvYear.setText("");
			tvLocation.setText("");
			
			if (doc_c.getCount() > 0) {
				doc_c.moveToFirst();
	
				while (!doc_c.isAfterLast()) {
	
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("doctor", doc_c.getString(Doctors.DOCTOR_NAME));
					String temp = "";
					if (!doc_c.getString(Doctors.DOCTOR_SPEC).isEmpty()){
						temp = "Specialty: " + doc_c.getString(Doctors.DOCTOR_SPEC) + "  |  ";
					}
						
					if(!doc_c.getString(Doctors.DOCTOR_PHONE).isEmpty()){
						temp += "Phone: "+ doc_c.getString(Doctors.DOCTOR_PHONE);
						
					}
					item.put("extra", temp);
	
					list.add(item);
					doc_c.moveToNext();
				}
	
				String[] from = new String[] { "doctor", "extra" };
	
				int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
	
				int nativeLayout = android.R.layout.two_line_list_item;
	
				lvDoctor = (ListView) findViewById(R.id.aptDoctorsList);
				lvDoctor.setClickable(true);
				
				lvDoctor.setAdapter(new SimpleAdapter(this, list, nativeLayout, from, to));
				lvDoctor.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						apt_position = position;
						doc_c.moveToPosition(apt_position);
						chosenDoctor = doc_c.getString(Doctors.DOCTOR_NAME);
						docLocation = doc_c.getString(Doctors.DOCTOR_ADDR1) 
								    + ", " + doc_c.getString(Doctors.DOCTOR_CITY) 
								    + ", " + doc_c.getString(Doctors.DOCTOR_STATE);
						tvLocation.setText(docLocation);
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
		super.onResume();
	}
	
	@Override
	protected void onPause(){		
		Bundle bundle = new Bundle();
		bundle.putInt("USER_HASH", userHashValue);
		bundle.putInt("USE", use);
		onSaveInstanceState(bundle);
		super.onPause();
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
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
		
		String hours = tpTime.getCurrentHour().toString();
		String mins = tpTime.getCurrentMinute().toString();
		String stTime = hours+":"+mins;
		
		String stMonth = this.tvMonth.getText().toString();
		String stDay = this.tvDay.getText().toString();
		String stYear = this.tvYear.getText().toString();
		String stLocation = this.tvLocation.getText().toString();
		
		Context context = getApplicationContext();
		CharSequence text = "Please fill in all required fields!";
		int duration = Toast.LENGTH_LONG;
		
		if( stMonth.isEmpty() || stDay.isEmpty() || stYear.isEmpty() )
			text = "Error in date!";
		else if( (Integer.parseInt(stMonth) > 12) || (Integer.parseInt(stMonth) < 1) ||
				(Integer.parseInt(stDay) > 31) || (Integer.parseInt(stDay) < 1) ||
				(Integer.parseInt(stYear) < 2012) || (Integer.parseInt(stYear) > 9999) )
			text = "Error in date!";
		else if( stTime.isEmpty() )
			text = "Please fill in all required fields!";
		else
		{
			try{
				String stDate = stMonth + "/" + stDay + "/" + stYear;
				if(use == MainActivity.NEW){					
					database.addNewApt(userHashValue, chosenDoctor, stDate, stTime, stLocation );
					text = "Appointment Entry Saved!";
				}
				else if (use == MainActivity.VIEW){
					//for updating an entry
					database.updateApt(userHashValue, chosenDoctor, stDate, stTime, stLocation );
					text = "Appointment Entry Updated!";
				}
				
				//Create Calendar Event
				Intent calIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
				
				//Get user data for appointment
				Cursor temp = database.getUser(userHashValue);
				temp.moveToFirst();
				
				//Add event details
				calIntent.putExtra(CalendarContract.Events.TITLE, "Doctors Appointment with " + chosenDoctor);
				calIntent.putExtra(CalendarContract.Events.ORGANIZER, temp.getString(1));
				calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, stLocation);
				
				//Add reminder
				calIntent.putExtra(CalendarContract.Reminders.MINUTES, 60);
				calIntent.putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_SMS);
				calIntent.putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
				
				//Add attendees
				
				//first get all emergency contacts
				Cursor c = database.getConct(userHashValue);
				
				if( c.getCount() > 0){
					c.moveToFirst();
					
					while( !c.isAfterLast() ){
						calIntent.putExtra(CalendarContract.Attendees.ATTENDEE_NAME, c.getString(EmergConct.CONT_NAME));
						
						int indexofcomma = c.getString(EmergConct.CONT_EMAIL).indexOf(',');
						String email = c.getString(EmergConct.CONT_EMAIL).substring(0, indexofcomma);
						if( !email.isEmpty() )
							calIntent.putExtra(CalendarContract.Attendees.ATTENDEE_EMAIL, email );
						
						calIntent.putExtra(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_OPTIONAL);
						
						c.moveToNext();
					}
				}
				
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
	
	public void clearFields(View view){
		this.chosenDoctor = "";
		this.docLocation = "";
		this.tvDay.setText("");
		this.tvMonth.setText("");
		this.tvYear.setText("");
		this.tvLocation.setText("");
	}

}
