package app.phms;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SearchResults extends Activity {
	
	int userHashValue = 0;
	int type = -1;
	String keyword = "";
	
	PHMSDatabase database;
	
	final static int APTS = 0;
	final static int ARTS = 1;
	final static int DIET = 2;
	final static int DOC = 3;
	final static int CONT = 4;
	final static int MED = 5;
	final static int REC = 6;
	final static int VITAL = 7;
	
	static int [] position;
	int index;
	
	ListView searchListView;
	
	static Cursor cursor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		// Show the Up button in the action bar.
		setupActionBar();
		
		database = new PHMSDatabase(this);
		position = new int[100];
		index = 0;
		
		searchListView = (ListView) findViewById(R.id.searchResultsList);
		searchListView.setClickable(true);
	}
	
	@Override
	protected void onStart(){
		for( int x = 0; x < 100; x++ )
			position[x] = -1;
		super.onStart();
	}
	
	@Override
	protected void onResume(){
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			userHashValue = extras.getInt("USER_HASH");
			index = extras.getInt("TYPE");
			keyword = extras.getString("KEYWORD");
			if( keyword.isEmpty())
			{
				
			}
			//hopefully valid keyword
			else{
				returnResults(keyword);
			}
		}
		//problem
		else{
			
		}
	}
	
	@Override
	protected void onPause(){		
		Bundle bundle = new Bundle();
		bundle.putInt("USER_HASH", this.userHashValue);
		bundle.putInt("TYPE", index);
		bundle.putString("KEYWORD", keyword);
		onSaveInstanceState(bundle);
		super.onPause();
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_results, menu);
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

	private void returnResults( String search ){
		SimpleAdapter simple = null;
		switch(type){
		case APTS:
			cursor = database.searchApts(keyword);
			simple = showAppointments(cursor);
			break;
		case ARTS:
			cursor = database.searchArticles(keyword);
			simple = showArticles(cursor);
			break;
		case CONT:
			cursor = database.searchConct(keyword);
			simple = showContacts(cursor);
			break;
		case DIET:
			cursor = database.searchDiet(keyword);
			simple = showDiet(cursor);
			break;
		case DOC:
			cursor = database.searchDocs(keyword);
			simple = showDocs(cursor);
			break;
		case MED:
			cursor = database.searchMeds(keyword);
			simple = showMeds(cursor);
			break;
		case REC:
			cursor = database.searchRecipes(keyword);
			simple= showRecipes(cursor);
			break;
		case VITAL:
			cursor = database.searchVitals(keyword);
			simple = showVitals(cursor);
			break;
		default:
			//error
			break;
		}
		
		if( simple != null ){
			searchListView.setAdapter(simple);
			if( cursor != null ){
				searchListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
						SearchResults.cursor.moveToPosition(SearchResults.position[position]);
							
						AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchResults.this);
				        builder1.setMessage("Choose Your Action.");
				        builder1.setCancelable(true);
				        builder1.setPositiveButton("View", new DialogInterface.OnClickListener() {
				        	public void onClick(DialogInterface dialog, int id) {
				        		launchActivity(MainActivity.VIEW, type, -1);
								dialog.cancel();
				                }
				        });
				        builder1.setNegativeButton("Delete",
			                    new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int id) {
			                	launchActivity(MainActivity.DELETE, type, SearchResults.position[position]);
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
				});
			}
		}
		else {
			Context context = getApplicationContext();
			CharSequence text = "No entries found.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	/*
	 * Appointments List
	 */
	private SimpleAdapter showAppointments(final Cursor cursor2){
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		SimpleAdapter simple = null;
		
		if (cursor2.getCount() > 0) {
			cursor2.moveToFirst();

			while (!cursor2.isAfterLast()) {

				String string = "";
				HashMap<String, String> item = new HashMap<String, String>();
				if( cursor2.getInt(Appointments.APT_HASH) == userHashValue )
				{
					position[index]=cursor2.getPosition();
					item.put("date", cursor2.getString(Appointments.APT_DATE));
					if (!cursor2.getString(Appointments.APT_DOC).isEmpty())
						string = "Doctor: " + cursor2.getString(Appointments.APT_DOC) + "  |  ";
					if( !cursor2.getString(Appointments.APT_TIME).isEmpty())
						string += "Time: "+ cursor2.getString(Appointments.APT_TIME);
					
					item.put("extra", string);
	
					list.add(item);
					index++;
				}
				cursor2.moveToNext();
			}

			String[] from = new String[] { "date", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;
			
			simple = new SimpleAdapter(this, list, nativeLayout, from, to); 
		}
		return simple;
	}
	
	/*
	 * Articles list
	 */
	private SimpleAdapter showArticles(Cursor cursor2) {
		
		SimpleAdapter simple = null;
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		if (cursor2.getCount() > 0) {
			cursor2.moveToFirst();

			while (!cursor2.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				String details = "";
				
				if( cursor2.getInt(Artciles.ART_HASH) == userHashValue ){
					position[index]=cursor2.getPosition();
				
					//show medicine name
					item.put("art", cursor2.getString(Artciles.ART_NAME));
					
					//get website
					if( !cursor2.getString(Artciles.ART_SITE).isEmpty())
						details += "Website: " + cursor2.getString(Artciles.ART_SITE) + "  |  ";			
					
					//add to list
					item.put("extra", details);
					list.add(item);
					index++;
				}
				
				cursor2.moveToNext();
			}

			String[] from = new String[] { "art", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;
			
			simple = new SimpleAdapter(this, list, nativeLayout, from, to);
		}
		return simple;
	}
	
	/*
	 * Contacts List
	 */
	private SimpleAdapter showContacts(Cursor cursor2) {
		SimpleAdapter simple = null;
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		if (cursor2.getCount() > 0) {
			cursor2.moveToFirst();

			while (!cursor2.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				
				if( cursor2.getInt(EmergConct.CONT_HASH) == userHashValue ){
					position[index]=cursor2.getPosition();
					item.put("contact", cursor2.getString(EmergConct.CONT_NAME));
					if (!cursor2.getString(EmergConct.CONT_PHONE).isEmpty())
						item.put("extra", "Phone: "+ cursor2.getString(EmergConct.CONT_PHONE));
					list.add(item);
					index++;
				}
				cursor2.moveToNext();
			}

			String[] from = new String[] { "contact", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;
			
			simple = new SimpleAdapter(this, list, nativeLayout, from, to);
		}
		return simple;
	}
	
	/*
	 * Diet List
	 */
	private SimpleAdapter showDiet(Cursor cursor2) {
		SimpleAdapter simple = null;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		if (cursor2.getCount() > 0) {
			cursor2.moveToFirst();

			while (!cursor2.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				
				if( cursor2.getInt(Diet.DIET_HASH) == userHashValue ){
					position[index]=cursor2.getPosition();
					item.put("date", cursor2.getString(Diet.DIET_DATE));
					String details = "";
					if (!cursor2.getString(Diet.DIET_TIME).isEmpty())
						details += "Time: " + cursor2.getString(Diet.DIET_TIME) + " | ";
					if (!cursor2.getString(Diet.DIET_CALS).isEmpty())
						details += "Calories: " + cursor2.getString(Diet.DIET_CALS);
					item.put("extra", details);
					list.add(item);
					index++;
				}
				cursor2.moveToNext();
			}

			String[] from = new String[] { "date", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;
			simple = new SimpleAdapter(this, list, nativeLayout, from, to);
		} 
		return simple;
	}
	
	/*
	 * Doctors List
	 */
	private SimpleAdapter showDocs(Cursor cursor2) {
		
		SimpleAdapter simple = null;
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		if (cursor2.getCount() > 0) {
			cursor2.moveToFirst();

			while (!cursor2.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				
				if( cursor2.getInt(Doctors.DOCTOR_HASH) == userHashValue ){
					position[index]=cursor2.getPosition();
					item.put("doctor", cursor2.getString(Doctors.DOCTOR_NAME));
					if (!cursor2.getString(Doctors.DOCTOR_SPEC).isEmpty())
						item.put("extra", "Specialty: " + cursor2.getString(Doctors.DOCTOR_SPEC) + "  |  Phone: "+ cursor.getString(Doctors.DOCTOR_PHONE));
					else
						item.put("extra", "Phone: " + cursor2.getString(Doctors.DOCTOR_PHONE));
	
					list.add(item);
					index++;
				}
				cursor2.moveToNext();
			}

			String[] from = new String[] { "doctor", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;
			
			simple = new SimpleAdapter(this, list, nativeLayout, from, to);
		} 
		return simple;
	}
	
	/*
	 * Medication List
	 */
	private SimpleAdapter showMeds(Cursor cursor2) {
		SimpleAdapter simple = null;
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		if (cursor2.getCount() > 0) {
			cursor2.moveToFirst();

			while (!cursor2.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				String details = "";
				
				if( cursor2.getInt(Medications.MED_HASH) == userHashValue ){
					position[index]=cursor2.getPosition();
					//show medicine name
					item.put("med", cursor.getString(Medications.MED_NAME));
					
					//get refill date
					if( !cursor2.getString(Medications.MED_DATE).isEmpty())
						details += "Refill Date: " + cursor2.getString(Medications.MED_DATE) + "  |  ";
					//get # refills left
					if( !cursor2.getString(Medications.MED_REFILLS).isEmpty())
						details += "Refills: "+ cursor2.getString(Medications.MED_REFILLS);
					//get special instructions
					if (!cursor2.getString(Medications.MED_SPEC).isEmpty())
						details = "Special Instr: " + cursor2.getString(Medications.MED_SPEC) + "  |  ";
					
					//add to list
					item.put("extra", details);
					list.add(item);
					index++;
				}
				
				cursor2.moveToNext();
			}

			String[] from = new String[] { "med", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;
			
			simple = new SimpleAdapter(this, list, nativeLayout, from, to);
		} 
		return simple;
	}
	
	/*
	 * Recipes List
	 */
	private SimpleAdapter showRecipes(Cursor cursor2) {
		SimpleAdapter simple = null;
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		if (cursor2.getCount() > 0) {
			cursor2.moveToFirst();

			while (!cursor2.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				String details = "";
				
				if( cursor2.getInt(Recipes.REC_HASH) == userHashValue ){
					position[index]=cursor2.getPosition();
					//show medicine name
					item.put("rec", cursor.getString(Recipes.REC_NAME));
					
					//get details
					if( !cursor2.getString(Recipes.REC_DETAILS).isEmpty())
						details += "Details: " + cursor2.getString(Recipes.REC_DETAILS) + "  |  ";
					
					//add to list
					item.put("extra", details);
					list.add(item);
					index++;
				}
				
				cursor2.moveToNext();
			}

			String[] from = new String[] { "rec", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;
			
			simple = new SimpleAdapter(this, list, nativeLayout, from, to);
		} 
		return simple;
	}

	/*
	 * Vital Signs List
	 */
	private SimpleAdapter showVitals(Cursor cursor2) {
		SimpleAdapter simple = null;
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		if (cursor2.getCount() > 0) {
			cursor2.moveToFirst();

			while (!cursor2.isAfterLast()) {

				HashMap<String, String> item = new HashMap<String, String>();
				
				if( cursor2.getInt(VitalSigns.VITAL_HASH) == userHashValue ){
					position[index]=cursor2.getPosition();
					item.put("date", cursor2.getString(VitalSigns.VITAL_DATE));
					String details = "";
					if (!cursor2.getString(VitalSigns.VITAL_WEIGHT).isEmpty())
						details += "Weight: " + cursor2.getString(VitalSigns.VITAL_WEIGHT) + " | ";
					if (!cursor2.getString(VitalSigns.VITAL_BP).isEmpty())
						details += "Blood Pressure: " + cursor2.getString(VitalSigns.VITAL_BP) + " | ";
					if (!cursor2.getString(VitalSigns.VITAL_TEMP).isEmpty())
						details += "Temperature: " + cursor2.getString(VitalSigns.VITAL_TEMP);
					item.put("extra", details);
					list.add(item);
					index++;
				}
				cursor2.moveToNext();
			}

			String[] from = new String[] { "date", "extra" };

			int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

			int nativeLayout = android.R.layout.two_line_list_item;
			
			simple = new SimpleAdapter(this, list, nativeLayout, from, to);
		} 
		return simple;
	}
	
	private void launchActivity(int type, int page, int pos){		
		switch(type){
			case MainActivity.VIEW:
				switch( page ){
					case APTS:
						Intent intent = new Intent(this, NewAppointments.class);
						intent.putExtra("USER_HASH", userHashValue);
						intent.putExtra("USE", MainActivity.VIEW);
						intent.putExtra("APT_POSITION", pos);
						startActivity(intent);
						break;
					case ARTS:
						Intent intent1 = new Intent(this, NewArticles.class);
						intent1.putExtra("USER_HASH", userHashValue);
						intent1.putExtra("USE", MainActivity.VIEW);
						intent1.putExtra("ART_POSITION", pos);
						startActivity(intent1);
						break;
					case DIET:
						Intent intent2 = new Intent(this, NewDiet.class);
						intent2.putExtra("USER_HASH", userHashValue);
						intent2.putExtra("USE", MainActivity.VIEW);
						intent2.putExtra("MED_POSITION", pos);
						startActivity(intent2);
						break;
					case DOC:
						Intent intent3 = new Intent(this, NewDoctors.class);
						intent3.putExtra("USER_HASH", userHashValue);
						intent3.putExtra("USE", MainActivity.VIEW);
						intent3.putExtra("DOC_POSITION", pos);
						startActivity(intent3);
						break;
					case CONT:
						Intent intent4 = new Intent(this, NewContact.class);
						intent4.putExtra("USER_HASH", userHashValue);
						intent4.putExtra("USE", MainActivity.VIEW);
						intent4.putExtra("CONT_POSITION", pos);
						startActivity(intent4);
						break;
					case MED:
						Intent intent5 = new Intent(this, NewMedication.class);
						intent5.putExtra("USER_HASH", userHashValue);
						intent5.putExtra("USE", MainActivity.VIEW);
						intent5.putExtra("MED_POSITION", pos);
						startActivity(intent5);
						break;
					case REC:
						Intent intent6 = new Intent(this, NewRecipes.class);
						intent6.putExtra("USER_HASH", userHashValue);
						intent6.putExtra("USE", MainActivity.VIEW);
						intent6.putExtra("REC_POSITION", pos);
						startActivity(intent6);
						break;
					case VITAL:
						Intent intent7 = new Intent(this, NewVitals.class);
						intent7.putExtra("USER_HASH", userHashValue);
						intent7.putExtra("USE", MainActivity.VIEW);
						intent7.putExtra("VITAL_POSITION", pos);
						startActivity(intent7);
						break;
					default:
						break;
				}
				break;
				
			case MainActivity.DELETE:
				if( pos != -1 )
				{
					cursor.moveToPosition(position[pos]);
					switch( page ){
						case APTS:
							database.deleteApt(userHashValue, cursor.getString(Appointments.APT_DATE));
							break;
						case ARTS:
							database.deleteArticle(userHashValue, cursor.getString(Artciles.ART_NAME));
							break;
						case CONT:
							database.deleteConct(userHashValue, cursor.getString(EmergConct.CONT_NAME));
							break;
						case DOC:
							database.deleteDocs(userHashValue, cursor.getString(Doctors.DOCTOR_NAME));
							break;
						case DIET:
							database.deleteDiet(userHashValue, cursor.getString(Diet.DIET_TITLE));
							break;
						case MED:
							database.deleteMeds(userHashValue, cursor.getString(Medications.MED_NAME));
							break;
						case REC:
							database.deleteRecipe(userHashValue, cursor.getString(Recipes.REC_NAME));
							break;
						case VITAL:
							database.deleteVitals(userHashValue, cursor.getString(VitalSigns.VITAL_DATE));
							break;
						default:
							break;
					}
					reload();
				}
				break;
				
			default:
				break;
		}
	}
	
	private void reload(){
		Intent intent = new Intent(this, SearchResults.class);
		intent.putExtra("USER_HASH", userHashValue);
		intent.putExtra("TYPE", index);
		intent.putExtra("KEYWORD", keyword);
		overridePendingTransition(0,0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0,0);
		startActivity(intent);
	}
}