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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Medications<AlertDialogActivity> extends Activity {

	int userHashValue = 0;
	
	final static int MED_HASH = 0;
	final static int MED_NAME = 1;
	final static int MED_DATE = 3;
	final static int MED_SPEC = 2;
	final static int MED_REFILLS = 4;
	
	Cursor c;

	int med_position = -1;
	int use = -1;

	ListView medsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medications);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Get 1st name
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userHashValue = extras.getInt("USER_HASH");
		}
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		PHMSDatabase database = new PHMSDatabase(this);
		c = database.getMeds(userHashValue);

		if (c.getCount() > 0) {
			c.moveToFirst();

			while (!c.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				String details = "";
				
				//show medicine name
				item.put("med", c.getString(MED_NAME));
				
				//get refill date
				if( !c.getString(MED_DATE).isEmpty())
					details += "Refill Date: " + c.getString(MED_DATE) + "  |  ";
				//get # refills left
				if( !c.getString(MED_REFILLS).isEmpty())
					details += "Refills: "+ c.getString(MED_REFILLS);
				//get special instructions
				if (!c.getString(MED_SPEC).isEmpty())
					details = "Special Instr: " + c.getString(MED_SPEC) + "  |  ";
				
				//add to list
				item.put("extra", details);
				list.add(item);
				
				c.moveToNext();
			}

			String[] from = new String[] { "med", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;

			medsListView = (ListView) findViewById(R.id.medListView);
			medsListView.setClickable(true);

			medsListView.setAdapter(new SimpleAdapter(this, list, nativeLayout, from, to));
			medsListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					use = MainActivity.VIEW;
					med_position = position;
					
					AlertDialog.Builder builder1 = new AlertDialog.Builder(Medications.this);
		            builder1.setMessage("Choose Your Action.");
		            builder1.setCancelable(true);
		            builder1.setPositiveButton("View", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                	/*
		                	Button btn = (Button)findViewById(R.id.medBtn);
		                	View view1 = new View(btn.getId());
							gotoNewUpdate(view1);
							*/
							dialog.cancel();
		                }
		            });
		            builder1.setNegativeButton("Delete",
		                    new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();
		                }
		            });
		            builder1.setNeutralButton("Cancel",
		                    new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();
		                }
		            });

		            AlertDialog alert11 = builder1.create();
		            alert11.show();

					/*
					Intent intent = new Intent(view.getContext(),NewDoctors.class);
					intent.putExtra("USER_HASH", userHashValue);
					intent.putExtra("USE", use);
					intent.putExtra("MED_POSITION", med_position);
					startActivity(intent);
					*/
				}
			});
		} 
		else {
			Context context = getApplicationContext();
			CharSequence text = "No medication entries found.";
			int duration = Toast.LENGTH_LONG;
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
		getMenuInflater().inflate(R.menu.medications, menu);
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
	
	public void gotoNewUpdate(View view) {
		Intent intent = new Intent(this, NewMedication.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	if (use == -1)
			intent.putExtra("USE", MainActivity.NEW);
		else
			intent.putExtra("USE", use);
		intent.putExtra("MED_POSITION", med_position);
    	startActivity(intent);
	}
}
