package app.phms;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

public class NewRecipes extends Activity {

	int userHashValue = 0;
	int use = -1;
	int position = -1;
	Cursor c;
	
	TextView pagetitle;
	TextView title;
	TextView details;
	
	Button btnRecipes;
	Button btnClear;
	
	PHMSDatabase database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_recipes);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		pagetitle = (TextView) findViewById(R.id.recipeMainTitle);
		title = (TextView) findViewById(R.id.recipeTitle);
		details = (TextView) findViewById(R.id.recipeDesc);
		
		
		btnRecipes = (Button) findViewById(R.id.recipeBtn);
		btnClear = (Button) findViewById(R.id.recipeClear);
		
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
			position = extras.getInt("REC_POSITION");
			
			//If we are viewing a current doctor, fill it in			
			if(    ( use == MainActivity.VIEW ) 
				&& ( position != -1 ) ) {				
				c = database.getRecipes(userHashValue);
				c.moveToPosition(position);
				
				this.pagetitle.setText("Update Recipe Entry");
				this.title.setText(c.getString(Recipes.REC_NAME));
				this.details.setText(c.getString(Recipes.REC_DETAILS));
				this.btnRecipes.setText("Update");
				this.btnClear.setVisibility(View.INVISIBLE);
			}
			else{
				this.pagetitle.setText("New Recipe Entry");
				this.title.setText("");
				this.details.setText("");
				this.btnRecipes.setText("Add New");
				this.btnClear.setVisibility(View.VISIBLE);
			}
		}
		
		super.onResume();
	}
	
	@Override
	protected void onPause(){		
		Bundle bundle = new Bundle();
		bundle.putInt("USER_HASH", this.userHashValue);
		bundle.putInt("USE", use );
		bundle.putInt("REC_POSITION", position);
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
		getMenuInflater().inflate(R.menu.new_recipes, menu);
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
			if(use == MainActivity.NEW){
				database.addNewRecipe(userHashValue, stTitle, stDesc);
				text = "Recipe Entry Saved!";
			}
			else if (use == MainActivity.VIEW){
				//for updating an entry
				database.updateRecipe(userHashValue, stTitle, stDesc);
				text = "Recipe Entry Updated!";
			}
			
			Intent intent = new Intent(this, Recipes.class);
			intent.putExtra("USER_HASH", userHashValue);
			startActivity(intent);
		}
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public void clearFields(View view){
		this.title.setText("");
		this.details.setText("");
	}
}
