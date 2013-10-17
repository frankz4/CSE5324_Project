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
import android.widget.TextView;
import android.widget.Toast;

public class NewDiet extends Activity {
	
	int userHashValue = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_diet);
		
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
		getMenuInflater().inflate(R.menu.new_diet, menu);
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
	
	public void addNewDiet (View view){
		final TextView tvMonth = (TextView) findViewById(R.id.dietMonth);
		final TextView tvDay   = (TextView) findViewById(R.id.dietDay);
		final TextView tvYear  = (TextView) findViewById(R.id.dietYear);
		final TextView tvMeal  = (TextView) findViewById(R.id.dietMeals);
		final TextView tvTime  = (TextView) findViewById(R.id.dietTime);
		final TextView tvCals  = (TextView) findViewById(R.id.dietCalories);
		
		String month = tvMonth.getText().toString();
		String day   = tvDay.getText().toString();
		String year  = tvYear.getText().toString();
		String meal  = tvMeal.getText().toString();
		String s_time    = tvTime.getText().toString();
		String cals  = tvCals.getText().toString();
		
		if( month.equals(null) ||
			day.equals(null) ||
			year.equals(null) ||
			meal.equals(null) ||
			s_time.equals(null) )
		{
			Context context = getApplicationContext();
			CharSequence text = "Please fill in all required fields!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{	
			Time time = Time.valueOf(s_time);

			try{
				//Store information in Database
				PHMSDatabase database = new PHMSDatabase(null);
				database.addNewDiet(userHashValue, 
									Date.valueOf(year+'-'+month+'-'+day), 
									time, 
									meal, 
									Integer.parseInt(cals));
			
				Intent intent = new Intent(this, Diet.class);
				startActivity(intent);
			}
			catch (android.database.sqlite.SQLiteException ex) {
				Context context = getApplicationContext();
				CharSequence text = "Database Error, Please Try Again...";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
	}

}
