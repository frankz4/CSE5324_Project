package app.phms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import app.phms.R.color;

public class NewArticles extends Activity {

	int userHashValue = 0;
	int use = -1;
	int position = -1;
	Cursor c;
	
	TextView pagetitle;
	TextView title;
	EditText website;
	TextView details;
	
	Button btnArticles;
	Button btnClear;
	
	PHMSDatabase database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_articles);
		
		// Show the Up button in the action bar.
		//setupActionBar();
		
		pagetitle = (TextView) findViewById(R.id.artTitle);
		title = (TextView) findViewById(R.id.articleTitle);
		website = (EditText) findViewById(R.id.articleSite);
		details = (TextView) findViewById(R.id.articleDesc);
		
		
		btnArticles = (Button) findViewById(R.id.artcileBtn);
		btnClear = (Button) findViewById(R.id.artClear);
		
		database = new PHMSDatabase(this);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onResume(){
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			userHashValue = extras.getInt("USER_HASH");
			use = extras.getInt("USE");
			position = extras.getInt("ART_POSITION");
			
			//If we are viewing a current doctor, fill it in			
			if(    ( use == MainActivity.VIEW ) 
				&& ( position != -1 ) ) {				
				c = database.getArticles(userHashValue);
				c.moveToPosition(position);
				
				this.pagetitle.setText("Update Article Entry");
				this.title.setText(c.getString(Artciles.ART_NAME));
				this.website.setText(c.getString(Artciles.ART_SITE));
				this.details.setText(c.getString(Artciles.ART_DETAILS));
				
				
				this.btnArticles.setText("Update");
				
				this.btnClear.setEnabled(false);
				
				this.website.setFocusable(false);
				this.website.setClickable(true);
				this.website.setTextColor(Color.BLUE);
			}
			else{
				this.pagetitle.setText("New Article Entry");
				this.title.setText("");
				this.website.setText("");
				this.website.setTextColor(Color.BLACK);
				this.details.setText("");
				
				this.btnArticles.setText("Add New");
				this.btnClear.setEnabled(true);
			}
		}
		super.onResume();
	}
	
	@Override
	protected void onPause(){		
		Bundle bundle = new Bundle();
		bundle.putInt("USER_HASH", userHashValue);
		bundle.putInt("USE", use);
		bundle.putInt("ART_POSITION", position);
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
		getMenuInflater().inflate(R.menu.new_articles, menu);
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
	
	public void addNew(View view){
		
		String stTitle = title.getText().toString();
		String stSite = website.getText().toString();
		String stDesc = details.getText().toString();
		
		Context context = getApplicationContext();
		CharSequence text = "Please fill in all required fields!";
		int duration = Toast.LENGTH_LONG;
		if( stTitle.isEmpty() || 
			stDesc.isEmpty() )
		{
			text = "Please fill in all required fields!";
		}
		else
		{			
			if( this.use == MainActivity.NEW ){
				database.addNewArticles(userHashValue, stTitle, stSite, stDesc);
				text = "Article Added!";
			}
			else if ( this.use == MainActivity.VIEW ){
				database.updateArticles(userHashValue, stTitle, stSite, stDesc);
				text = "Article Updated!";
			}
			
			//Go back to main articles page
			Intent intent = new Intent(this, Artciles.class);
			intent.putExtra("USER_HASH", userHashValue);
			startActivity(intent);
		}
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public void clearFields( View view)
	{
		this.title.setText("");
		this.website.setText("");
		this.details.setText("");
	}
	
	public void launchWebsite( View view ){
		String url = website.getText().toString();
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			   url = "http://" + url;
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}
}
