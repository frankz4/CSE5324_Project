package app.phms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity {

	int userHashValue = 0;
	
	final static int ALL_BTN = 8;
	
	RadioGroup radioGroup;
	View radioButton;
	TextView keyword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		radioGroup = (RadioGroup) findViewById(R.id.radioGroupSearch);
		
		keyword = (TextView) findViewById(R.id.searchKeyword);
		
		/*Temporarily disable the All Button */
		View all = radioGroup.getChildAt(ALL_BTN);
		all.setEnabled(false);
		all.setVisibility(View.INVISIBLE);
		
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
		
		if( userHashValue == 0 ){
			Context context = getApplicationContext();
			CharSequence text = "Error in activity!";
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
		getMenuInflater().inflate(R.menu.search, menu);
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
	
	public void search (View view){
		int clickedButton = radioGroup.getCheckedRadioButtonId();
		radioButton = radioGroup.findViewById(clickedButton);
		final int index = radioGroup.indexOfChild(radioButton);
		
		if( keyword.getText().toString().isEmpty() )
		{
			Context context = getApplicationContext();
			CharSequence text = "Please check your keyword";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else{
			if( index == 8 ){
				AlertDialog.Builder builder1 = new AlertDialog.Builder(Search.this);
		        builder1.setMessage("This action may take some time.");
		        builder1.setCancelable(true);
		        builder1.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int id) {
		        		launchActivity(index);
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
			launchActivity(index);
		}
	}
	
	public void launchActivity(int index){
		Intent intent = new Intent(this, SearchResults.class);
    	intent.putExtra("USER_HASH", userHashValue);
		intent.putExtra("TYPE", index);
		intent.putExtra("KEYWORD", keyword.getText().toString());
    	startActivity(intent);
	}
}