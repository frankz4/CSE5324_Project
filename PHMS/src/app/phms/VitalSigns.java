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
import android.widget.Toast;

public class VitalSigns extends Activity {
	
	int userHashValue = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vital_signs);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userHashValue = extras.getInt("USER_HASH");
		}
		
		//search User database for given hash
		PHMSDatabase database = new PHMSDatabase(null);
		
		Cursor cursor = database.getVitals(userHashValue);
		int columnIndex = cursor.getColumnIndex(PHMSDatabase.KEY_HASH);
		
		// if found, update the List View
		if( columnIndex > -1 )
		{
			
		}
		//else send out toast and go back to home screen
		else
		{
			//for toast
	    	Context context = getApplicationContext();
			CharSequence text = "Error in database";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			
			Intent intent = new Intent(this, HomeScreen.class);
			intent.putExtra("USER_HASH", userHashValue);
			startActivity(intent);
		}
		
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
    	startActivity(intent);
    }
    
    public void gotoViewVitalSigns (View view){
    	Intent intent = new Intent (this, ViewVitalSigns.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
    }

}
