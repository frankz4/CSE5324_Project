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
	
	TextView tvName = (TextView) findViewById(R.id.docName);
	TextView tvPhone = (TextView)findViewById(R.id.docPhone);
	TextView tvSpecial = (TextView) findViewById(R.id.docSpecialty);
	TextView tvAddr = (TextView) findViewById(R.id.docAddr);
	TextView tvAddr2 = (TextView) findViewById(R.id.docAddr2);
	TextView tvCity = (TextView) findViewById(R.id.docCity);
	TextView tvState = (TextView) findViewById(R.id.docState);
	TextView tvZip = (TextView) findViewById(R.id.docZip);
	TextView tvFax = (TextView) findViewById(R.id.docFax);
	
	private static final int PICK_CONTACT = 1;
	
	PHMSDatabase database = new PHMSDatabase(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_doctors);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			userHashValue = extras.getInt("USER_HASH");
			use = extras.getInt("USE");
			
			if( use == MainActivity.VIEW ){
				position = extras.getInt("DOC_POSITION");
				
				c = database.getDocs(userHashValue);
				c.moveToPosition(position);
				
				this.tvName.setText(c.getString(Doctors.DOCTOR_NAME));
				this.tvSpecial.setText(c.getString(Doctors.DOCTOR_SPEC));
				this.tvPhone.setText(c.getString(Doctors.DOCTOR_PHONE));
				this.tvFax.setText(c.getString(Doctors.DOCTOR_FAX));
				this.tvAddr.setText(c.getString(Doctors.DOCTOR_ADDR1));
				this.tvAddr2.setText(c.getString(Doctors.DOCTOR_ADDR2));
				this.tvCity.setText(c.getString(Doctors.DOCTOR_CITY));
				this.tvState.setText(c.getString(Doctors.DOCTOR_STATE));
				this.tvZip.setText(c.getString(Doctors.DOCTOR_ZIP));
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

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data){
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
			case (PICK_CONTACT) :
				if (resultCode == Activity.RESULT_OK) {

					Uri contactData = data.getData();
					Cursor c =  managedQuery(contactData, null, null, null, null);
					if (c.moveToFirst()) {
						String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

						String hasPhone =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));

						if (hasPhone.equalsIgnoreCase("1")) {
							Cursor phones = getContentResolver().query( 
		                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
		                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, 
		                       null, null);
							phones.moveToFirst();
							//String cNumber = phones.getString(phones.getColumnIndex("data1"));
							selDocPhoneNumber = phones.getString(phones.getColumnIndex("data1"));
						}
						
						selDocName = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
/*
						Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
						cursor.moveToFirst();
						long id2 = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
						cursor.close();

						// get the data package containg the postal information for the contact
						cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, 
						    new String[]{ StructuredPostal.STREET,
						        StructuredPostal.CITY,
						        // add more coluns from StructuredPostal if you need them
						        StructuredPostal.POSTCODE},
						        ContactsContract.Data.CONTACT_ID + "=? AND " +
						            StructuredPostal.MIMETYPE + "=?",
						        new String[]{String.valueOf(id2), StructuredPostal.CONTENT_ITEM_TYPE},
						        null);

						selDocStreet = cursor.getString(cursor.getColumnIndexOrThrow(StructuredPostal.STREET));
						selDocZip = cursor.getString(cursor.getColumnIndexOrThrow(StructuredPostal.POSTCODE));
						selDocCity = cursor.getString(cursor.getColumnIndexOrThrow(StructuredPostal.CITY));
*/
					}
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
	
	public void findDoctor(View view){
		Intent intent = new Intent(Intent.ACTION_PICK,
									ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult( intent, PICK_CONTACT);
	}
	
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
		
		if( name.isEmpty() )
		{
			Context context = getApplicationContext();
			CharSequence text = "Please fill in all required fields!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			//Store information in Database
			database.addNewDoc(userHashValue, name, specialty, phone, fax, addr, addr2, city, state, zip);
			
			if(usedContacts)
			{
				//put in code to ask to add to contacts
			}
		}
		
		Intent intent = new Intent(this, Doctors.class);
		intent.putExtra("USER_HASH", userHashValue);
		startActivity(intent);
	}
	
	public void clearFields( View view ){
		TextView tvName = (TextView) findViewById(R.id.docName);
		TextView tvPhone = (TextView)findViewById(R.id.docPhone);
		TextView tvSpecial = (TextView) findViewById(R.id.docSpecialty);
		TextView tvAddr = (TextView) findViewById(R.id.docAddr);
		TextView tvAddr2 = (TextView) findViewById(R.id.docAddr2);
		TextView tvCity = (TextView) findViewById(R.id.docCity);
		TextView tvState = (TextView) findViewById(R.id.docState);
		TextView tvZip = (TextView) findViewById(R.id.docZip);
		TextView tvFax = (TextView) findViewById(R.id.docFax);
		
		tvName.setText("");
		tvPhone.setText("");
		tvSpecial.setText("");
		tvAddr.setText("");
		tvAddr2.setText("");
		tvCity.setText("");
		tvState.setText("");
		tvZip.setText("");
		tvFax.setText("");
	}
}
