package app.phms;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

public class EmergConct extends Activity {

	int userHashValue = 0;

	final static int CONT_HASH = 0;
	final static int CONT_NAME = 1;
	final static int CONT_PHONE = 3;
	final static int CONT_ADDR1 = 5;
	final static int CONT_ADDR2 = 6;
	final static int CONT_CITY = 7;
	final static int CONT_STATE = 8;
	final static int CONT_ZIP = 9;

	Cursor c;

	int cont_position = -1;
	int use = -1;

	ListView contactListView;
	
	PHMSDatabase database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emerg_conct);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userHashValue = extras.getInt("USER_HASH");
		}

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		database = new PHMSDatabase(this);
		c = database.getConct(userHashValue);

		if (c.getCount() > 0) {
			c.moveToFirst();

			while (!c.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				item.put("contact", c.getString(CONT_NAME));
				if (!c.getString(CONT_PHONE).isEmpty())
					item.put("extra", "Phone: "+ c.getString(CONT_PHONE));
				list.add(item);
				c.moveToNext();
			}

			String[] from = new String[] { "contact", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;

			contactListView = (ListView) findViewById(R.id.contactListView);
			contactListView.setClickable(true);

			contactListView.setAdapter(new SimpleAdapter(this, list, nativeLayout, from, to));
			contactListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					use = MainActivity.VIEW;
					cont_position = position;
					c.moveToPosition(cont_position);
							
					AlertDialog.Builder builder1 = new AlertDialog.Builder(EmergConct.this);
					builder1.setMessage("Choose Your Action.");
					builder1.setCancelable(true);
					builder1.setPositiveButton("View", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
		                	launchActivity(MainActivity.VIEW, null);
							dialog.cancel();
		                }
		            });
		            builder1.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                	launchActivity(MainActivity.DELETE, c.getString(EmergConct.CONT_NAME));
		                    dialog.cancel();
		                }
		            });
		            builder1.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();
		                }
		            });
	
		            AlertDialog alert11 = builder1.create();
		            alert11.show();
				}
			});
		} 
		else {
			Context context = getApplicationContext();
			CharSequence text = "No contact entries found.";
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
		getMenuInflater().inflate(R.menu.emerg_conct, menu);
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

	public void gotoNewContact(View view){
		Intent intent = new Intent(this, NewContact.class);
		intent.putExtra("USER_HASH", userHashValue);
		intent.putExtra("USE", MainActivity.NEW);
		intent.putExtra("CONT_POSITION", cont_position);
    	startActivity(intent);
	}
	
	private void launchActivity(int how, String name){
		
		if( how == MainActivity.VIEW ){
			Intent intent = new Intent(this, NewContact.class);
			intent.putExtra("USER_HASH", userHashValue);
			intent.putExtra("USE", MainActivity.VIEW);
			intent.putExtra("CONT_POSITION", cont_position);
			startActivity(intent);
		}
		else if( how == MainActivity.DELETE){
			database.deleteConct(userHashValue, name);
			Context context = getApplicationContext();
			CharSequence text = "Doctor Entry Removed.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			reload();
		}
	}
	
	private void reload(){
		Intent intent = new Intent(this, EmergConct.class);
		intent.putExtra("USER_HASH", userHashValue);
		overridePendingTransition(0,0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0,0);
		startActivity(intent);
	}
}
