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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Diet extends Activity {
	
	int userHashValue = 0;
	
	final static int DIET_HASH = 0;
	final static int DIET_TITLE = 1;
	final static int DIET_MEAL = 2;
	final static int DIET_CALS = 3;
	final static int DIET_DATE = 4;
	final static int DIET_TIME = 5;
	
	Cursor c;

	int diet_position = -1;
	int use = -1;

	ListView dietListView;
	
	PHMSDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diet);
		// Show the Up button in the action bar.
		//setupActionBar();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onResume(){
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userHashValue = extras.getInt("USER_HASH");
		}
		
		//search User database for given hash
		database = new PHMSDatabase(this);
		c = database.getDiet(userHashValue);
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		
		if (c.getCount() > 0) {
			c.moveToFirst();

			while (!c.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				item.put("date", c.getString(DIET_DATE));
				String details = "";
				if (!c.getString(DIET_TIME).isEmpty())
					details += "Time: " + c.getString(DIET_TIME) + " | ";
				if (!c.getString(DIET_CALS).isEmpty())
					details += "Calories: " + c.getString(DIET_CALS);
				item.put("extra", details);
				list.add(item);
				c.moveToNext();
			}

			String[] from = new String[] { "date", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;

			dietListView = (ListView) findViewById(R.id.dietListView);
			dietListView.setClickable(true);

			dietListView.setAdapter(new SimpleAdapter(this, list, nativeLayout, from, to));
			dietListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					use = MainActivity.VIEW;
					diet_position = position;

					c.moveToPosition(diet_position);
					
					AlertDialog.Builder builder1 = new AlertDialog.Builder(Diet.this);
		            builder1.setMessage("Choose Your Action.");
		            builder1.setCancelable(true);
		            builder1.setPositiveButton("View", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                	
		                	launchActivity(MainActivity.VIEW, null);

							dialog.cancel();
		                }
		            });
		            builder1.setNegativeButton("Delete",
		                    new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                	launchActivity(MainActivity.DELETE, c.getString(Diet.DIET_TITLE));
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
				}
			});
		} 
		else {
			Context context = getApplicationContext();
			CharSequence text = "No diet entries found.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
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
		getMenuInflater().inflate(R.menu.diet, menu);
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

	public void gotoNewMeal(View view){
		Intent intent = new Intent(this, NewDiet.class);
    	intent.putExtra("USER_HASH", userHashValue);
		intent.putExtra("USE", MainActivity.NEW);
		intent.putExtra("DIET_POSITION", diet_position);
    	startActivity(intent);
	}
	
	private void launchActivity(int how, String name){
		
		if( how == MainActivity.VIEW ){
			Intent intent = new Intent(this, NewDiet.class);
			intent.putExtra("USER_HASH", userHashValue);
			intent.putExtra("USE", MainActivity.VIEW);
			intent.putExtra("MED_POSITION", diet_position);
			startActivity(intent);
		}
		else if( how == MainActivity.DELETE){
			database.deleteDiet(userHashValue, name);
			Context context = getApplicationContext();
			CharSequence text = "Meal Entry Removed.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			reload();
		}
	}
	
	private void reload(){
		Intent intent = new Intent(this, Diet.class);
		intent.putExtra("USER_HASH", userHashValue);
		overridePendingTransition(0,0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0,0);
		startActivity(intent);
	}
}
