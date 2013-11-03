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

public class Diet extends Activity {
	
	int userHashValue = 0;
	
	final static int DIET_DATE = 4;
	final static int DIET_TIME = 5;
	final static int DIET_MEAL = 2;
	final static int DIET_CALS = 0;
	final static int DIET_TITLE = 1;
	
	Cursor c;

	int diet_position = -1;
	int use = -1;

	ListView dietListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diet);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userHashValue = extras.getInt("USER_HASH");
		}
		
		//search User database for given hash
		PHMSDatabase database = new PHMSDatabase(this);
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

					Intent intent = new Intent(view.getContext(),NewDiet.class);
					intent.putExtra("USER_HASH", userHashValue);
					intent.putExtra("USE", use);
					intent.putExtra("DIET_POSITION", diet_position);
					startActivity(intent);
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

		if (use == -1)
			intent.putExtra("USE", MainActivity.NEW);
		else
			intent.putExtra("USE", use);
		intent.putExtra("DIET_POSITION", diet_position);
    	startActivity(intent);
	}
}
