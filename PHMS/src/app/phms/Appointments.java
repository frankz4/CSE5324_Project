package app.phms;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Appointments extends Activity {

	int userHashValue = 0;
	
	Cursor c;

	int apt_position = -1;
	int use = -1;

	ListView aptsListView;
	
	final static int APT_HASH = 1;
	final static int APT_DOC = 2;
	final static int APT_DATE = 3;
	final static int APT_TIME = 4;
	final static int APT_LOC = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointments);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Get 1st name
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userHashValue = extras.getInt("USER_HASH");
		}
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		PHMSDatabase database = new PHMSDatabase(this);
		c = database.getApts(userHashValue);

		if (c.getCount() > 0) {
			c.moveToFirst();

			while (!c.isAfterLast()) {

				String string = "";
				HashMap<String, String> item = new HashMap<String, String>();
				item.put("date", c.getString(APT_DATE));
				if (!c.getString(APT_DOC).isEmpty())
					string = "Doctor: " + c.getString(APT_DOC) + "  |  ";
				if( !c.getString(APT_TIME).isEmpty())
					string += "Time: "+ c.getString(APT_TIME);
				
				item.put("extra", string);

				list.add(item);
				c.moveToNext();
			}

			String[] from = new String[] { "doctor", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;

			aptsListView = (ListView) findViewById(R.id.doctorListView);
			aptsListView.setClickable(true);

			aptsListView.setAdapter(new SimpleAdapter(this, list, nativeLayout, from, to));
			aptsListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					use = MainActivity.VIEW;
					apt_position = position;

					Intent intent = new Intent(view.getContext(),NewDoctors.class);
					intent.putExtra("USER_HASH", userHashValue);
					intent.putExtra("USE", use);
					intent.putExtra("apt_position", apt_position);
					startActivity(intent);
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
		getMenuInflater().inflate(R.menu.appointments, menu);
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
	
	public void gotoNewView (View view) {
		Intent intent = new Intent(this, NewAppointments.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
	}
}
