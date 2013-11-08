package app.phms;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VitalSigns extends Activity {
	
	int userHashValue = 0;
	
	final static int VITAL_DATE = 1;
	final static int VITAL_WEIGHT = 2;
	final static int VITAL_BP = 3;
	final static int VITAL_TEMP = 4;
	final static int VITAL_GLU = 5;
	final static int VITAL_CHOL = 6;
	
	Cursor c;

	int vital_position = -1;
	int use = -1;

	ListView vitalsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vital_signs);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userHashValue = extras.getInt("USER_HASH");
		}
		
		//search User database for given hash
		PHMSDatabase database = new PHMSDatabase(this);
		c = database.getVitals(userHashValue);
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		
		if (c.getCount() > 0) {
			c.moveToFirst();

			while (!c.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				item.put("date", c.getString(VITAL_DATE));
				String details = "";
				if (!c.getString(VITAL_WEIGHT).isEmpty())
					details += "Weight: " + c.getString(VITAL_WEIGHT) + " | ";
				if (!c.getString(VITAL_BP).isEmpty())
					details += "Blood Pressure: " + c.getString(VITAL_BP) + " | ";
				if (!c.getString(VITAL_TEMP).isEmpty())
					details += "Temperature: " + c.getString(VITAL_TEMP);
				item.put("extra", details);
				list.add(item);
				c.moveToNext();
			}

			String[] from = new String[] { "date", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;

			vitalsListView = (ListView) findViewById(R.id.lvVitals);
			vitalsListView.setClickable(true);

			vitalsListView.setAdapter(new SimpleAdapter(this, list, nativeLayout, from, to));
			vitalsListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					use = MainActivity.VIEW;
					vital_position = position;

					openDialog();
					/*
					Intent intent = new Intent(view.getContext(),NewVitals.class);
					intent.putExtra("USER_HASH", userHashValue);
					intent.putExtra("USE", use);
					intent.putExtra("VITAL_POSITION", vital_position);
					startActivity(intent);
					*/
				}
			});
		} 
		else {
			Context context = getApplicationContext();
			CharSequence text = "No vital sign entries found.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	public void openDialog( ){
		AlertDialog.Builder builder = new AlertDialog.Builder(VitalSigns.this, 1);
		c.moveToPosition(vital_position);
		builder.setMessage(c.getString(VITAL_DATE));
		builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(VitalSigns.this,NewVitals.class);
	        	intent.putExtra("USER_HASH", userHashValue);
	        	intent.putExtra("USE", use);
	        	intent.putExtra("VITAL_POSITION", vital_position);
	        	startActivity(intent);
			}
		});
		builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
			}
		});
		builder.show();
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
		getMenuInflater().inflate(R.menu.vital_signs, menu);
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
	
	/** Called when the user clicks the Add New button */
    public void gotoNewVitals(View view) {
    	Intent intent = new Intent(this, NewVitals.class);
    	intent.putExtra("USER_HASH", userHashValue);

		if (use == -1)
			intent.putExtra("USE", MainActivity.NEW);
		else
			intent.putExtra("USE", use);
		intent.putExtra("VITAL_POSITION", vital_position);
    	startActivity(intent);
    }
}