package app.phms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	final static int NEW = 0;
	final static int VIEW = 1;
	final static int DELETE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/** Called when the user clicks the New User button */
    public void gotoNewUser(View view) {
    	Intent intent = new Intent(this, Reg.class);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the login */ 
    public void validateUser(View view) {
    	//Get entered UserName and Password
    	final EditText getUserName = (EditText) findViewById(R.id.maineditText1);
    	final EditText getPassword = (EditText) findViewById(R.id.maineditText2);
    	
    	//for toast
    	Context context = getApplicationContext();
		CharSequence text = "Username or Password Error! Try again!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
    	
    	//If either User Name or Password are empty, send out Toast
    	if( getUserName.getText().toString().equals(null) || getPassword.getText().toString().equals(null) )
    		toast.show();

    	else
    	{
    		String startHash = getUserName.getText().toString() + getPassword.getText().toString();
    		int hash = startHash.hashCode();
    		
    		//search User database for given hash
    		PHMSDatabase database = new PHMSDatabase(this);
    		
    		Cursor cursor = database.getUser(hash);
    		int count = cursor.getCount();
    		
    		cursor.moveToFirst();
    		
    		// if found, send first name to the Home Screen
    		if( count > 0 )
    		{
    			Intent intent = new Intent(this, HomeScreen.class);
    			intent.putExtra("USER_FIRST_NAME", 
    							cursor.getString(1));
    			intent.putExtra("USER_HASH", hash);
    			startActivity(intent);
    		}
    		
    		// if not found, send toast to notify user
    		else
    			toast.show();
    		
    	}
    }
	
}
