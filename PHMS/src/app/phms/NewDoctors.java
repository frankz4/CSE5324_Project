package app.phms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewDoctors extends Activity {

	int userHashValue = 0;
	int use	= 0;
	int position = -1;
	Cursor c;
	
	boolean usedContacts = false;
	
	String selDocPhoneNumber = "";
	String selDocName = "";
	String selDocStreet = "";
	String selDocCity = "";
	String selDocZip = "";
	
	TextView title;
	TextView tvName;
	TextView tvPhone;
	TextView tvSpecial;
	TextView tvAddr;
	TextView tvAddr2;
	TextView tvCity;
	TextView tvState;
	TextView tvZip;
	TextView tvFax;
	Button btnNewDoc;
	Button btnClear;
	Button btnFind;
	
	private static final int PICK_CONTACT = 1;
	
	PHMSDatabase database = new PHMSDatabase(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_doctors);
		
		title = (TextView) findViewById(R.id.docTitle);
		tvName = (TextView) findViewById(R.id.docName);
		tvPhone = (TextView)findViewById(R.id.docPhone);
		tvSpecial = (TextView) findViewById(R.id.docSpecialty);
		tvAddr = (TextView) findViewById(R.id.docAddr);
		tvAddr2 = (TextView) findViewById(R.id.docAddr2);
		tvCity = (TextView) findViewById(R.id.docCity);
		tvState = (TextView) findViewById(R.id.docState);
		tvZip = (TextView) findViewById(R.id.docZip);
		tvFax = (TextView) findViewById(R.id.docFax);
		btnNewDoc = (Button) findViewById(R.id.btnNewDoc);
		btnClear = (Button) findViewById(R.id.btnDocClear);
		btnFind = (Button) findViewById(R.id.btnDocFind);
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null){
			userHashValue = extras.getInt("USER_HASH");
			use = extras.getInt("USE");
			position = extras.getInt("DOC_POSITION");
			
			//If we are viewing a current doctor, fill it in			
			if(    ( use == MainActivity.VIEW ) 
				&& ( position != -1 ) ) {				
				c = database.getDocs(userHashValue);
				c.moveToPosition(position);
				
				this.title.setText("Update Doctor Entry");
				this.tvName.setText(c.getString(Doctors.DOCTOR_NAME));
				this.tvSpecial.setText(c.getString(Doctors.DOCTOR_SPEC));
				this.tvPhone.setText(c.getString(Doctors.DOCTOR_PHONE));
				this.tvFax.setText(c.getString(Doctors.DOCTOR_FAX));
				this.tvAddr.setText(c.getString(Doctors.DOCTOR_ADDR1));
				this.tvAddr2.setText(c.getString(Doctors.DOCTOR_ADDR2));
				this.tvCity.setText(c.getString(Doctors.DOCTOR_CITY));
				this.tvState.setText(c.getString(Doctors.DOCTOR_STATE));
				this.tvZip.setText(c.getString(Doctors.DOCTOR_ZIP));
				
				btnNewDoc.setText("Update");
				
				//dont allow the clear or find button to be used
				btnClear.setVisibility(View.INVISIBLE);
				btnFind.setVisibility(View.INVISIBLE);
			}
			else{
				this.title.setText("New Doctor Entry");
				//clear out input fields
				this.tvName.setText("");
				this.tvSpecial.setText("");
				this.tvPhone.setText("");
				this.tvFax.setText("");
				this.tvAddr.setText("");
				this.tvAddr2.setText("");
				this.tvCity.setText("");
				this.tvState.setText("");
				this.tvZip.setText("");
				
				btnNewDoc.setText("Add New");
				
				//dont allow the clear or find button to be used
				btnClear.setVisibility(View.VISIBLE);
				btnFind.setVisibility(View.VISIBLE);
			}
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
		getMenuInflater().inflate(R.menu.new_doctors, menu);
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
							selDocPhoneNumber = phones.getString(phones.getColumnIndex("data1"));
						}
						
						selDocName = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
						
						Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
				        Cursor postal_cursor  = getContentResolver().query(postal_uri,null,  
				        							ContactsContract.Data.CONTACT_ID + "="+contactId, null,null);
				        while(postal_cursor.moveToNext())
				        {
				        	this.selDocStreet = postal_cursor.getString(postal_cursor.getColumnIndex(StructuredPostal.STREET));
				            this.selDocCity = postal_cursor.getString(postal_cursor.getColumnIndex(StructuredPostal.CITY));
				            this.selDocZip = postal_cursor.getString(postal_cursor.getColumnIndexOrThrow(StructuredPostal.POSTCODE));
				        } 
				        postal_cursor.close();
					}
					c.close();
				}
				break;
		}
		
		TextView tvName = (TextView) findViewById(R.id.docName);
		TextView tvPhone = (TextView) findViewById(R.id.docPhone);
		TextView tvAddr = (TextView) findViewById(R.id.docAddr);
		TextView tvCity = (TextView) findViewById(R.id.docCity);
		TextView tvZip = (TextView) findViewById(R.id.docZip);
		
		if( selDocName.isEmpty() )
			usedContacts = false;
		else{
			usedContacts = true;
			tvName.setText(selDocName);
			tvPhone.setText(selDocPhoneNumber);
			tvAddr.setText(selDocStreet);
			tvCity.setText(selDocCity);
			tvZip.setText(selDocZip);
		}
	}
	
	//Add or update current doctor
	public void addNewDoctor (View view){
		
		String name = tvName.getText().toString();
		String phone = tvPhone.getText().toString();
		String specialty = tvSpecial.getText().toString();
		String addr = tvAddr.getText().toString();
		String addr2 = tvAddr2.getText().toString();
		String city = tvCity.getText().toString();
		String state = tvState.getText().toString();
		String zip = tvZip.getText().toString();
		String fax = tvFax.getText().toString();

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
				database.addNewDoc(userHashValue, name, specialty, phone, fax, addr, addr2, city, state, zip);
				text = "Doctor Information Saved!";
			}
			else if (use == MainActivity.VIEW){
				database.updateDoc(userHashValue, name, specialty, phone, fax, addr, addr2, city, state, zip);
				text = "Doctor Information Updated!";
				
			}
			Intent intent = new Intent(this, Doctors.class);
			intent.putExtra("USER_HASH", userHashValue);
			startActivity(intent);
			
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	//Clear all the fields
	public void clearFields( View view ){
		if(use == MainActivity.NEW){
			this.tvName.setText("");
			this.tvPhone.setText("");
			this.tvSpecial.setText("");
			this.tvAddr.setText("");
			this.tvAddr2.setText("");
			this.tvCity.setText("");
			this.tvState.setText("");
			this.tvZip.setText("");
			this.tvFax.setText("");
		}
		else if (use == MainActivity.VIEW){
			//put code to delete entry from database
		}
	}
}