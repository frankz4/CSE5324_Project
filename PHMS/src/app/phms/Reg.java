package app.phms;

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

public class Reg extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
		// Show the Up button in the action bar.
		//setupActionBar();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override
	protected void onPause(){
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
		getMenuInflater().inflate(R.menu.reg, menu);
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

	public void addToDB(View view){
		
		int input_error = 0;
		CharSequence text = "";
		
		final TextView tvFirstName = (TextView) findViewById(R.id.regEditText1);
		final TextView tvLastName = (TextView) findViewById(R.id.regEditText2);
		final TextView tvUserName = (TextView) findViewById(R.id.regEditText5);
		final TextView tvPassword = (TextView) findViewById(R.id.regEditText6);
		
		String firstName = tvFirstName.getText().toString();
		String lastName = tvLastName.getText().toString();
		String userName = tvUserName.getText().toString(); 
		String password = tvPassword.getText().toString();
		
		if( firstName.isEmpty() || 
			lastName.isEmpty() || 
			userName.isEmpty() || 
			password.isEmpty() )
		{
			text = "First, Last, User Name, or Password Empty!";
			input_error = 1;
		}
		if (userName.length() < 6 || userName.length() > 16 || password.length() < 6 || password.length() > 16)
		{
			text = "Username and password must be between 6 and 16 characters";
			input_error = 1;
		}
		
		if (input_error == 1)
		{
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			String hashString = userName + password;
			int hashValue = hashString.hashCode();
		
			PHMSDatabase database = new PHMSDatabase(this);
			
			database.addNewUser(hashValue, firstName, lastName);
			
			Intent intent = new Intent(this, HomeScreen.class);
			intent.putExtra("USER_HASH", hashValue );
			intent.putExtra("USER_FIRST_NAME", firstName);
	    	startActivity(intent);
		}
	}
	
}
