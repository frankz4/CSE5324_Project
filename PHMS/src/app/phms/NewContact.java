package app.phms;

import android.net.Uri;
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
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

public class NewContact extends Activity {
	
	int userHashValue = 0;
	int use	= 0;
	int position = -1;
	Cursor c;
	
	boolean usedContacts = false;
	
	String selPhoneNumber = "";
	String selName = "";
	String selStreet = "";
	String selCity = "";
	String selZip = "";
	
	TextView title;
	TextView tvName;
	TextView tvPhone;
	TextView tvAddr;
	TextView tvAddr2;
	TextView tvCity;
	TextView tvState;
	TextView tvZip;
	
	Button btnNew;
	Button btnClear;
	Button btnFind;
	
	private static final int PICK_CONTACT = 1;
	
	PHMSDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_contact);
		// Show the Up button in the action bar.
		setupActionBar();
		
		database = new PHMSDatabase(this);
		
		title = (TextView) findViewById(R.id.contTitle);
		
		tvName = (TextView) findViewById(R.id.contName);
		tvPhone = (TextView)findViewById(R.id.contPhone);
		tvAddr = (TextView) findViewById(R.id.contAddr);
		tvAddr2 = (TextView) findViewById(R.id.contAddr2);
		tvCity = (TextView) findViewById(R.id.contCity);
		tvState = (TextView) findViewById(R.id.contState);
		tvZip = (TextView) findViewById(R.id.contZip);
		btnNew = (Button) findViewById(R.id.btnContNew);
		btnClear = (Button) findViewById(R.id.btnContClear);
		btnFind = (Button) findViewById(R.id.btnContFind);
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
			position = extras.getInt("CONT_POSITION");
			
			//If we are viewing a current doctor, fill it in			
			if(    ( use == MainActivity.VIEW ) 
				&& ( position != -1 ) ) {				
				c = database.getDocs(userHashValue);
				c.moveToPosition(position);
				
				this.tvName.setText(c.getString(EmergConct.CONT_NAME));
				this.tvPhone.setText(c.getString(EmergConct.CONT_PHONE));
				this.tvAddr.setText(c.getString(EmergConct.CONT_ADDR1));
				this.tvAddr2.setText(c.getString(EmergConct.CONT_ADDR2));
				this.tvCity.setText(c.getString(EmergConct.CONT_CITY));
				this.tvState.setText(c.getString(EmergConct.CONT_STATE));
				this.tvZip.setText(c.getString(EmergConct.CONT_ZIP));
				
				btnNew.setText("Update");
				
				//dont allow the clear or find button to be used
				btnClear.setVisibility(View.INVISIBLE);
				btnFind.setVisibility(View.INVISIBLE);
				
				title.setText("Update Contact Entry");
			}
			else{
				//clear out input fields
				this.tvName.setText("");
				this.tvPhone.setText("");
				this.tvAddr.setText("");
				this.tvAddr2.setText("");
				this.tvCity.setText("");
				this.tvState.setText("");
				this.tvZip.setText("");
				
				btnNew.setText("Add New");
				btnClear.setVisibility(View.VISIBLE);
				btnFind.setVisibility(View.VISIBLE);
				
				title.setText("New Contact Entry");
			}
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
		getMenuInflater().inflate(R.menu.new_contact, menu);
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
	
	//Open Native Contact Activity
	public void findDoctor(View view){
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult( intent, PICK_CONTACT);
	}
		
	//Get information back from the Native Contacts activity
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data){
		super.onActivityResult(reqCode, resultCode, data);
		switch (reqCode) {
			case (PICK_CONTACT) :
				if (resultCode == Activity.RESULT_OK) {
					Uri contactData = data.getData();
					Cursor c =  managedQuery(contactData, null, null, null, null);
					if (c.moveToFirst()) {
						String contactId =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

						String hasPhone =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));

						if (hasPhone.equalsIgnoreCase("1")) {
							Cursor phones = getContentResolver().query( 
		                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
		                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, 
		                       null, null);
							phones.moveToFirst();
							//String cNumber = phones.getString(phones.getColumnIndex("data1"));
							selPhoneNumber = phones.getString(phones.getColumnIndex("data1"));
						}
							
						selName = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
						
						Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
				        Cursor postal_cursor  = getContentResolver().query(postal_uri,null,  
				        							ContactsContract.Data.CONTACT_ID + "="+contactId, null,null);
				        while(postal_cursor.moveToNext())
				        {
				        	this.selStreet = postal_cursor.getString(postal_cursor.getColumnIndex(StructuredPostal.STREET));
				            this.selCity = postal_cursor.getString(postal_cursor.getColumnIndex(StructuredPostal.CITY));
				            this.selZip = postal_cursor.getString(postal_cursor.getColumnIndexOrThrow(StructuredPostal.POSTCODE));
				        } 
				        postal_cursor.close();
					}
					c.close();
				}
			break;
		}
			
		TextView tvName = (TextView) findViewById(R.id.contName);
		TextView tvPhone = (TextView) findViewById(R.id.contPhone);
		TextView tvAddr = (TextView) findViewById(R.id.contAddr);
		TextView tvCity = (TextView) findViewById(R.id.contCity);
		TextView tvZip = (TextView) findViewById(R.id.contZip);
			
		if( selName.isEmpty() )
			usedContacts = false;
		else{
			usedContacts = true;
			tvName.setText(selName);
			tvPhone.setText(selPhoneNumber);
			tvAddr.setText(selStreet);
			tvCity.setText(selCity);
			tvZip.setText(selZip);
		}
	}
	
	public void addToDB(View view){
		String name = tvName.getText().toString();
		String phone = tvPhone.getText().toString();
		String addr = tvAddr.getText().toString();
		String addr2 = tvAddr2.getText().toString();
		String city = tvCity.getText().toString();
		String state = tvState.getText().toString();
		String zip = tvZip.getText().toString();

		Context context = getApplicationContext();
		CharSequence text = "Please fill in all required fields!";
		int duration = Toast.LENGTH_LONG;
		
		if( name.isEmpty() )
		{
			text = "Please fill in all required fields!";
		}
		else
		{
			if(use == MainActivity.NEW){
				//Store information in Database
				database.addNewConct(userHashValue, name, phone, addr, addr2, city, state, zip);
				text = "Doctor Information Saved!";
			}
			else if (use == MainActivity.VIEW){
				database.updateConct(userHashValue, name, phone, addr, addr2, city, state, zip);
				text = "Doctor Information Updated!";
				
			}
			Intent intent = new Intent(this, Doctors.class);
			intent.putExtra("USER_HASH", userHashValue);
			startActivity(intent);
			
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	public void clearFields(View view){
		//clear out input fields
		this.tvName.setText("");
		this.tvPhone.setText("");
		this.tvAddr.setText("");
		this.tvAddr2.setText("");
		this.tvCity.setText("");
		this.tvState.setText("");
		this.tvZip.setText("");
	}

}
