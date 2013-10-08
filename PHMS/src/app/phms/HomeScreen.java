package app.phms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeScreen extends Activity {

	int userHashValue = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		//Get 1st name
		String firstName = "";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			firstName = extras.getString("USER_FIRST_NAME");
			userHashValue = extras.getInt("USER_HASH");
		}
		
		final TextView welcome = (TextView) findViewById(R.id.HStextView1);
		welcome.append(", " + firstName);
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
		getMenuInflater().inflate(R.menu.home_screen, menu);
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

	/** Called when the user clicks the Vital Signs button */
    public void gotoVitals(View view) {
    	Intent intent = new Intent(this, VitalSigns.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
    }
	
    /** Called when the user clicks the Diet button */
    public void gotoDiet(View view) {
    	Intent intent = new Intent(this, Diet.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the Medications button */
    public void gotoMeds(View view) {
    	Intent intent = new Intent(this, Medications.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the Appointments button */ 
    public void gotoApts(View view) {
    	Intent intent = new Intent(this, Appointments.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the Favorites button */ 
    public void gotoFavorites(View view) {
    	Intent intent = new Intent(this, Favorites.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the  button */
    public void gotoDocs(View view) {
    	Intent intent = new Intent(this, Doctors.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the Emergency Contact button */
    public void gotoEC(View view) {
    	Intent intent = new Intent(this, EmergConct.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the Search button */
    public void gotoSearch(View view) {
    	Intent intent = new Intent(this, Search.class);
    	intent.putExtra("USER_HASH", userHashValue);
    	startActivity(intent);
    }
}
