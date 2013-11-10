package app.phms;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewDiet extends Activity {
	
	int userHashValue = 0;
	int use = -1;
	int position = -1;
	Cursor c;
	
	TextView title;
	TextView tvMonth;
	TextView tvDay;
	TextView tvYear;
	TextView tvTime;
	TextView tvTitle;
	TextView tvMeal;
	TextView tvCals;
	
	Button btnDiet;
	Button btnClear;
	
	PHMSDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_diet);
		
		title = (TextView) findViewById(R.id.dietTitle);
		tvMonth = (TextView) findViewById(R.id.dietMonth);
		tvDay = (TextView) findViewById(R.id.dietDay);
		tvYear = (TextView) findViewById(R.id.dietYear);
		tvTime = (TextView) findViewById(R.id.dietTime);
		tvTitle = (TextView) findViewById(R.id.dietMealTitle);
		tvMeal = (TextView) findViewById(R.id.dietMeal);
		tvCals = (TextView) findViewById(R.id.dietCalories);
		
		btnDiet = (Button) findViewById(R.id.btnNewDiet);
		btnClear = (Button) findViewById(R.id.btnDietClear);
		
		database = new PHMSDatabase(this);
		
		Bundle extras = getIntent().getExtras();
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		if (extras != null){
			userHashValue = extras.getInt("USER_HASH");
			use = extras.getInt("USE");
			position = extras.getInt("DIET_POSITION");
			
			//If we are viewing a current doctor, fill it in			
			if(    ( use == MainActivity.VIEW ) 
				&& ( position != -1 ) ) {				
				c = database.getDiet(userHashValue);
				c.moveToPosition(position);
				
				this.title.setText("Update Meal Entry");
				String date = c.getString(Diet.DIET_DATE);
				this.tvMonth.setText(date.substring(0, 1));
				this.tvDay.setText(date.substring(3, 4));
				this.tvYear.setText(date.substring(6, 9));
				this.tvTitle.setText(c.getString(Diet.DIET_TITLE));
				this.tvMeal.setText(c.getString(Diet.DIET_MEAL));
				this.tvTime.setText(c.getString(Diet.DIET_TIME));
				
				
				this.btnDiet.setText("Update");
				
				this.btnClear.setVisibility(View.INVISIBLE);
			}
			else{
				this.title.setText("New Meal Entry");
				this.tvDay.setText("");
				this.tvMonth.setText("");
				this.tvYear.setText("");
				this.tvTime.setText("");
				this.tvMeal.setText("");
				this.tvTitle.setText("");
				
				this.btnDiet.setText("Add New");
				this.btnClear.setVisibility(View.VISIBLE);
			}
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
		String month = this.tvMonth.getText().toString();
		String day   = this.tvDay.getText().toString();
		String year  = this.tvYear.getText().toString();
		String title = this.tvTitle.getText().toString();
		String meal  = this.tvMeal.getText().toString();
		String time = this.tvTime.getText().toString();
		String cals  = this.tvCals.getText().toString();
		
		Context context = getApplicationContext();
		CharSequence text = "Please fill in all required fields!";
		int duration = Toast.LENGTH_LONG;
		
		if( month.isEmpty() ||
			day.isEmpty() ||
			year.isEmpty() ||
			time.isEmpty() ||
			title.isEmpty() )
		{
			text = "Please fill in all required fields!";
		}
		else
		{	
			String date = month + "/" + day + "/" + year;
			try{
				
				if(use == MainActivity.NEW){
					database.addNewDiet(userHashValue,date,time,meal,cals,title);
					text = "Meal Entry Saved!";
				}
				else if (use == MainActivity.VIEW){
					//for updating an entry
					database.updateDiet(userHashValue,date,time,meal,cals,title);
					text = "Meal Entry Updated!";
				}

				Intent intent = new Intent(this, Diet.class);
				intent.putExtra("USER_HASH", this.userHashValue);
				startActivity(intent);
			}
			catch (android.database.sqlite.SQLiteException ex) {
				text = "Database Error, Please Try Again...";
			}
		}
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public void clearFields( View view)
	{
		this.tvMonth.setText("");
		this.tvDay.setText("");
		this.tvYear.setText("");
		this.tvCals.setText("");
		this.tvMeal.setText("");
		this.tvTime.setText("");
		this.tvTitle.setText("");
	}

}
