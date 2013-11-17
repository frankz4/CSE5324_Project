package app.phms;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PHMSDatabase extends SQLiteOpenHelper{	
	
	// The index (key)column name for use in where clauses
	public static final String KEY_HASH = "HASH";
	
	// The name and column index of each column in the database
	
	/**********************
	 **  USERS Database  **
	 **********************/
	public static final String KEY_USERS_FIRST = "FIRST";
	public static final String KEY_USERS_LAST = "LAST";
	
	/***********************
	 **  VITALS Database  **
	 ***********************/
	public static final String KEY_VITAL_DATE = "DATE";
	public static final String KEY_VITAL_WEIGHT = "WEIGHT";
	public static final String KEY_VITAL_BP = "BP";
	public static final String KEY_VITAL_TEMP = "TEMP";
	public static final String KEY_VITAL_GLUCOSE = "GLUCOSE";
	public static final String KEY_VITAL_CHOLESTEROL = "CHOLESTEROL";
	
	/************************
	 **  DOCTORS Database  **
	 ************************/
	public static final String KEY_DOC_DOC = "DOCTOR";
	public static final String KEY_DOC_SPECIALTY = "SPECIALTY";
	public static final String KEY_DOC_PHONE = "PHONE";
	public static final String KEY_DOC_FAX = "FAX";
	public static final String KEY_DOC_ADDR1 = "ADDR1";
	public static final String KEY_DOC_ADDR2 = "ADDR2";
	public static final String KEY_DOC_CITY = "CITY";
	public static final String KEY_DOC_STATE = "STATE";
	public static final String KEY_DOC_ZIP = "ZIP";
	
	/****************************
	 **  MEDICATIONS Database  **
	 ****************************/
	public static final String KEY_MED_MED = "MED";
	public static final String KEY_MED_DOSE = "DOSE";
	public static final String KEY_MED_REFILL_DATE = "REFILL_DATE";
	public static final String KEY_MED_REFILLS_LEFT = "REFILLS";
	
	/*****************************
	 **  APPOINTMENTS Database  **
	 *****************************/
	public static final String KEY_APT_DOC = "DOCTOR";
	public static final String KEY_APT_DATE = "DATE";
	public static final String KEY_APT_TIME = "TIME";
	public static final String KEY_APT_LOC = "LOCATION";
	
	/*********************
	 **  DIET Database  **
	 *********************/
	public static final String KEY_DIET_TITLE = "TITLE";
	public static final String KEY_DIET_MEAL = "MEAL";
	public static final String KEY_DIET_DATE = "DATE";
	public static final String KEY_DIET_TIME = "TIME";
	public static final String KEY_DIET_CALS = "CALORIES";
	
	/*************************
	 **  EMG_CONT Database  **
	 *************************/
	public static final String KEY_CONT_NAME = "NAME";
	public static final String KEY_CONT_PHONE = "PHONE";
	public static final String KEY_CONT_ADDR1 = "ADDR1";
	public static final String KEY_CONT_ADDR2 = "ADDR2";
	public static final String KEY_CONT_CITY = "CITY";
	public static final String KEY_CONT_STATE = "STATE";
	public static final String KEY_CONT_ZIP = "ZIP";
	
	/******************************************************
	 ***		END EMERG CONT Database Access			***
	 ******************************************************/
	
	/*************************
	 **  ARTICLES Database  **
	 *************************/
	public static final String KEY_ART_TITLE = "TITLE";
	public static final String KEY_ART_DETAILS = "DETAILS";
	public static final String KEY_ART_SITE = "WEBSITE";
	
	/*************************
	 **  RECIPES Database   **
	 *************************/
	public static final String KEY_REC_TITLE = "TITLE";
	public static final String KEY_REC_DETAILS = "DETAILS";
	
	
	@SuppressLint("SdCardPath")
	//-----------------------------------------------------------
	//-----------------------------------------------------------
	//
	//					Database Open Helper Class
	//
	//-----------------------------------------------------------
	//-----------------------------------------------------------
	//private static class DatabaseOpenHelper extends SQLiteOpenHelper{
			
		private static final String DATABASE_NAME = "personalhealthManager1";
		private static final String DATABASE_TABLE_USERS = "USERS";
		private static final String DATABASE_TABLE_VITALS = "VITALS";
		private static final String DATABASE_TABLE_DOCS = "DOCTORS";
		private static final String DATABASE_TABLE_MEDS = "MEDICATIONS";
		private static final String DATABASE_TABLE_APTS = "APPOINTMENTS";
		private static final String DATABASE_TABLE_DIET = "DIET";
		private static final String DATABASE_TABLE_CONTS = "EMG_CONT";
		private static final String DATABASE_TABLE_ART = "ARTICLES";
		private static final String DATABASE_TABLE_REC = "RECIPES";

		private static final int DATABASE_VERSION = 1;
			
		private static final String DATABASE_CREATE_USERS = 
				"create table " + DATABASE_TABLE_USERS + 
				" (" + KEY_HASH + " integer primary key autoincrement, " +
				KEY_USERS_FIRST + " text not null, " +
				KEY_USERS_LAST + " text not null);";
		
		private static final String DATABASE_CREATE_VITALS =
				"create table " + DATABASE_TABLE_VITALS + 
				" (" + KEY_HASH + " integer primary key autoincrement, ";
		
		private static final String DATABASE_CREATE_DOCS = 
				"create table " + DATABASE_TABLE_DOCS + 
				" (" + KEY_HASH + " integer primary key autoincrement, ";
		
		private static final String DATABASE_CREATE_MEDS = 
				"create table " + DATABASE_TABLE_MEDS + 
				" (" + KEY_HASH + " integer primary key autoincrement, ";
		
		private static final String DATABASE_CREATE_APTS = 
				"create table " + DATABASE_TABLE_APTS + 
				" (" + KEY_HASH + " integer primary key autoincrement, ";
		
		private static final String DATABASE_CREATE_DIET = 
				"create table " + DATABASE_TABLE_DIET + 
				" (" + KEY_HASH + " integer primary key autoincrement, ";
		
		private static final String DATABASE_CREATE_CONTS = 
				"create table " + DATABASE_TABLE_CONTS + 
				" (" + KEY_HASH + " integer primary key autoincrement, ";
			
		public PHMSDatabase( Context context) {
			 super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
			
		// Called when no database exists in disk and the
		// helper class needs to create a new one.
		@Override
		public void onCreate( SQLiteDatabase db) {
			db.execSQL( DATABASE_CREATE_USERS );
			db.execSQL( DATABASE_CREATE_VITALS );
			db.execSQL( DATABASE_CREATE_DOCS );
			db.execSQL( DATABASE_CREATE_MEDS );
			db.execSQL( DATABASE_CREATE_APTS );
			db.execSQL( DATABASE_CREATE_DIET );
			db.execSQL( DATABASE_CREATE_CONTS );			
		}
			
		// Called when there is a database version mismatch meaning that
		// the version of the database on disk needs to be upgraded to
		// the current version.
		@Override
		public void onUpgrade( SQLiteDatabase db, int oldVersion, 
								int newVersion ){
			// log the version upgrade
			Log.w("TaskDBAdapter", "Upgrading from version " +
					oldVersion + " to " +
					newVersion + ", which will destroy all old data");
			
			//Upgrade the existing database to conform to the new
			// version. Multiple previous versions can be handled by
			// comparing oldVersion and newVersion values.
				
			// The simplest case is to drop the old table and create a new one
			db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_USERS );
			db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_VITALS );
			db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_DOCS );
			db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_MEDS );
			db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_APTS );
			db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_DIET );
			db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_CONTS );
				
			// Create a new one
			onCreate(db);
				
		}	
	
	// Called when no need to access the database
	public void closeDatabse() {
		this.close();
	}
	
	/******************************************************
	 ***				USERS Database Access			***
	 ******************************************************/
	
	//Access the User Database
	public Cursor getUser(int hash_value) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		String rawQuery = "SELECT * FROM USERS WHERE " + KEY_HASH + "=" + hash_value;
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		return cursor;
	}
	
	//Add a new user
	public void addNewUser( int hashValue, String firstName, String lastName) {
		
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
		
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_USERS_FIRST, firstName);
		newValues.put(KEY_USERS_LAST, lastName);
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_USERS, null, newValues);
	}
	
	
	/******************************************************
	 ***			END USERS Database Access			***
	 ******************************************************/
	
	
	/******************************************************
	 ***				VITALS Database Access			***
	 ******************************************************/
	//Access the VITALS Database
	public Cursor getVitals(int hash_value) {

		SQLiteDatabase db = this.getWritableDatabase();
		
		String rawQuery = "SELECT * FROM VITALS WHERE " + KEY_HASH + "=" + hash_value;
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		return cursor;
	}
	
	//Add a new user
	public void addNewVitals( int hashValue, String date, String weight,
			String bp, String temp, String glucose, String cholesterol) {
			
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_VITAL_DATE, date);
		newValues.put(KEY_VITAL_WEIGHT, weight);
		newValues.put(KEY_VITAL_BP, bp);
		newValues.put(KEY_VITAL_TEMP, temp);
		newValues.put(KEY_VITAL_GLUCOSE, glucose);
		newValues.put(KEY_VITAL_CHOLESTEROL, cholesterol);
			
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_VITALS, null, newValues);
	}
	
	public void updateVitals( int hashValue, String date, String weight,
			String bp, String temp, String glucose, String cholesterol) {
		
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
					
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_VITAL_DATE, date);
		newValues.put(KEY_VITAL_WEIGHT, weight);
		newValues.put(KEY_VITAL_BP, bp);
		newValues.put(KEY_VITAL_TEMP, temp);
		newValues.put(KEY_VITAL_GLUCOSE, glucose);
		newValues.put(KEY_VITAL_CHOLESTEROL, cholesterol);
		
		String whereClause = KEY_HASH + "=" + hashValue + 
				" AND " + KEY_VITAL_DATE + "='" + date + "'";
					
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(DATABASE_TABLE_VITALS, newValues, whereClause, null);
		
	}
	
	public void deleteVitals( int hashValue, String date ){
		
		String whereClause =  KEY_HASH + "=" + hashValue + 
				" AND " + KEY_VITAL_DATE + "='" + date + "'";
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PHMSDatabase.DATABASE_TABLE_VITALS, whereClause, null);
	}
	
	/******************************************************
	 ***			END VITALS Database Access			***
	 ******************************************************/
	
	
	/******************************************************
	 ***				DOCTORS Database Access			***
	 ******************************************************/
	//Access the Doctor Database
	public Cursor getDocs(int hash_value) {
	
		SQLiteDatabase db = this.getWritableDatabase();
		
		String rawQuery = "SELECT * FROM DOCTORS WHERE " + KEY_HASH + "=" + hash_value;
		Cursor cursor = db.rawQuery(rawQuery, null);
		return cursor;
	}
			
	//Add a new doctor
	public void addNewDoc( int hashValue, String name, String specialty, String phone, String fax, String addr1,
							String addr2, String city, String state, String zip) {
			
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_DOC_DOC, name);
		newValues.put(KEY_DOC_SPECIALTY, specialty);
		newValues.put(KEY_DOC_PHONE, phone);
		newValues.put(KEY_DOC_FAX, fax);
		newValues.put(KEY_DOC_ADDR1, addr1 );
		newValues.put(KEY_DOC_ADDR2, addr2 );
		newValues.put(KEY_DOC_CITY, city );
		newValues.put(KEY_DOC_STATE, state );
		newValues.put(KEY_DOC_ZIP, zip );
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_DOCS, null, newValues);
	}
	
	//Add a new doctor
	public void updateDoc( int hashValue, String name, String specialty, String phone, String fax, String addr1,
							String addr2, String city, String state, String zip) {
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_DOC_DOC, name);
		newValues.put(KEY_DOC_SPECIALTY, specialty);
		newValues.put(KEY_DOC_PHONE, phone);
		newValues.put(KEY_DOC_FAX, fax);
		newValues.put(KEY_DOC_ADDR1, addr1 );
		newValues.put(KEY_DOC_ADDR2, addr2 );
		newValues.put(KEY_DOC_CITY, city );
		newValues.put(KEY_DOC_STATE, state );
		newValues.put(KEY_DOC_ZIP, zip );
		
		String whereClause = KEY_HASH + "=" + hashValue + 
				" AND " + KEY_DOC_DOC + "='" + name + "'";
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(DATABASE_TABLE_DOCS, newValues, whereClause, null);
	}
	
	public void deleteDocs( int hashValue, String name ){
		
		String whereClause = KEY_HASH + "=" + hashValue + 
				" AND " + KEY_DOC_DOC + "='" + name + "'";
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PHMSDatabase.DATABASE_TABLE_DOCS, whereClause, null);
	}
	
	/******************************************************
	 ***			END DOCTORS Database Access			***
	 ******************************************************/
	
	
	/******************************************************
	 ***			MEDICATION Database Access			***
	 ******************************************************/
	//Access the Medication Database
	public Cursor getMeds(int hash_value) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		String rawQuery = "SELECT * FROM " + DATABASE_TABLE_MEDS + " WHERE " + KEY_HASH + "=" + hash_value;
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		return cursor;
	}

	//Add a new medication
	public void addNewMed( int hashValue, String medicine, String dosage, String refill_date, String num_refills) {
			
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_MED_MED, medicine);
		newValues.put(KEY_MED_DOSE, dosage);
		newValues.put(KEY_MED_REFILL_DATE, refill_date.toString());
		newValues.put(KEY_MED_REFILLS_LEFT, num_refills);
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_MEDS, null, newValues);
	}
	
	public void updateMed ( int hashValue, String medicine, String dosage, String refill_date, String num_refills) {
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
					
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_MED_MED, medicine);
		newValues.put(KEY_MED_DOSE, dosage);
		newValues.put(KEY_MED_REFILL_DATE, refill_date.toString());
		newValues.put(KEY_MED_REFILLS_LEFT, num_refills);
		
		String whereClause = KEY_HASH + "=" + hashValue + 
				" AND " + KEY_MED_MED + "='" + medicine + "'";
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(DATABASE_TABLE_MEDS, newValues, whereClause, null);
	}
	
	public void deleteMeds( int hashValue, String name ){
		
		String whereClause =  PHMSDatabase.KEY_HASH + "="+ hashValue + 
				" AND " + PHMSDatabase.KEY_MED_MED + "='" + name + "'";
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DATABASE_TABLE_MEDS, whereClause, null);
	}
	
	/******************************************************
	 ***		END MEDICATION Database Access			***
	 ******************************************************/
	
	/******************************************************
	 ***		APPOINTMENTS Database Access			***
	 ******************************************************/
	//Access the Appointments Database
	public Cursor getApts(int hash_value) {

		SQLiteDatabase db = this.getWritableDatabase();
		
		String rawQuery = "SELECT * FROM APPOINTMENTS WHERE " + KEY_HASH + "=" + hash_value;
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		return cursor;
	}
		
	//Add a new appointment
	public void addNewApt ( int hashValue, String doctor, String stDate, String stTime, String location) {
			
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_APT_DOC, doctor);
		newValues.put(KEY_APT_DATE, stDate);
		newValues.put(KEY_APT_TIME, stTime);
		newValues.put(KEY_APT_LOC, location);
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_APTS, null, newValues);
	}
	
	public void updateApt(int hashValue, String doctor, String date,
			String time, String location) {
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_APT_DOC, doctor);
		newValues.put(KEY_APT_DATE, date);
		newValues.put(KEY_APT_TIME, time);
		newValues.put(KEY_APT_LOC, location);
		
		String whereClause = KEY_HASH + "=" + hashValue + 
				" AND " + KEY_APT_DATE + "='" + date + 
				"' AND " + KEY_APT_TIME + "='" + time + "'";
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(DATABASE_TABLE_APTS, newValues, whereClause, null);
	}
	
	public void deleteApt( int hashValue, String date ){
		
		String whereClause =  PHMSDatabase.KEY_HASH + "="+ hashValue + 
				" AND " + PHMSDatabase.KEY_APT_DATE+ "='" + date + "'";
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PHMSDatabase.DATABASE_TABLE_APTS, whereClause, null);
	}
	
	/******************************************************
	 ***		END APPOINTMENT Database Access			***
	 ******************************************************/
	
	/******************************************************
	 ***			DIET Database Access				***
	 ******************************************************/
	//Access the User Database
	public Cursor getDiet(int hash_value) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String rawQuery = "SELECT * FROM DIET WHERE " + KEY_HASH + "=" + hash_value;
		Cursor cursor = db.rawQuery(rawQuery, null);
			
		return cursor;
	}
	
	//Add a new user
	public void addNewDiet( int hashValue, String date, String time, String meal, String calories, String title) {
			
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_DIET_CALS, calories );
		newValues.put(KEY_DIET_TITLE, title );
		newValues.put(KEY_HASH, hashValue );
		newValues.put(KEY_DIET_MEAL, meal );
		newValues.put(KEY_DIET_DATE, date );
		newValues.put(KEY_DIET_TIME, time );
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_DIET, null, newValues);
	}
	
	public void updateDiet( int hashValue, String date, String time, String meal, String calories, String title) {
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_DIET_CALS, calories );
		newValues.put(KEY_DIET_TITLE, title );
		newValues.put(KEY_HASH, hashValue );
		newValues.put(KEY_DIET_MEAL, meal );
		newValues.put(KEY_DIET_DATE, date );
		newValues.put(KEY_DIET_TIME, time );
		
		String whereClause = KEY_HASH + "=" + hashValue + 
				" AND " + KEY_DIET_TITLE + "='" + title + "'";
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(DATABASE_TABLE_DIET, newValues, whereClause, null);
		
	}
	
	public void deleteDiet( int hashValue, String title ){
		
		String whereClause =  KEY_HASH + "=" + hashValue + 
				" AND " + KEY_DIET_TITLE+ "='" + title + "'";
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PHMSDatabase.DATABASE_TABLE_DIET, whereClause, null);
	}	
	
	/******************************************************
	 ***			END DIET Database Access			***
	 ******************************************************/
	
	/*****************************************************
	 ***			EMERG CONCT Database Access			***
	 ******************************************************/
	//Access the User Database
	public Cursor getConct(int hash_value) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String rawQuery = "SELECT * FROM EMERG_CONT WHERE " + KEY_HASH + "=" + hash_value;
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		return cursor;
	}

	//Add a new user
	public void addNewConct ( int hashValue, String name, String phone, String addr1,
								String addr2, String city, String state, String zip) {
			
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_CONT_NAME, name);
		newValues.put(KEY_CONT_PHONE, phone );
		newValues.put(KEY_CONT_ADDR1, addr1 );
		newValues.put(KEY_CONT_ADDR2, addr2 );
		newValues.put(KEY_CONT_CITY, city );
		newValues.put(KEY_CONT_STATE, state );
		newValues.put(KEY_CONT_ZIP, zip );
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_CONTS, null, newValues);
	}
	
	//Add a new user
	public void updateConct ( int hashValue, String name, String phone, String addr1,
								String addr2, String city, String state, String zip) {
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_CONT_NAME, name);
		newValues.put(KEY_CONT_PHONE, phone );
		newValues.put(KEY_CONT_ADDR1, addr1 );
		newValues.put(KEY_CONT_ADDR2, addr2 );
		newValues.put(KEY_CONT_CITY, city );
		newValues.put(KEY_CONT_STATE, state );
		newValues.put(KEY_CONT_ZIP, zip );
		
		String whereClause = KEY_HASH + "=" + hashValue +
				" AND " + KEY_CONT_NAME + "='" + name +"'";
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(DATABASE_TABLE_CONTS, newValues, whereClause, null);
		
	}
	
	public void deleteConct ( int hashValue, String name ){
		
		String whereClause =  KEY_HASH + "=" + hashValue + " AND " 
								+ KEY_CONT_NAME + "='" + name;
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PHMSDatabase.DATABASE_TABLE_DOCS, whereClause, null);
	}

	/******************************************************
	 ***			Articles Database Access			***
	 ******************************************************/
	
	public Cursor getArticles(int hash_value) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String rawQuery = "SELECT * FROM ARTICLES WHERE " + KEY_HASH + "=" + hash_value;
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		return cursor;
	}
	
	public void addNewArticles(int hashValue, String title,
			String site, String desc) {
		
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_ART_TITLE, title);
		newValues.put(KEY_ART_SITE, site);
		newValues.put(KEY_ART_DETAILS, desc);
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_ART, null, newValues);
	}
	
	public void updateArticles(int hashValue, String title,
			String site, String desc) {
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_ART_TITLE, title);
		newValues.put(KEY_ART_SITE, site);
		newValues.put(KEY_ART_DETAILS, desc);
		
		String whereClause = KEY_HASH + "=" + hashValue +
				" AND " + KEY_ART_TITLE + "='" + title + "'";
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(DATABASE_TABLE_ART, newValues, whereClause, null);
	}
	
	public void deleteArticle( int hashValue, String name ){
		
		String whereClause =  KEY_HASH + "=" + hashValue + 
				" AND " + KEY_ART_TITLE + "='" + name + "'";
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PHMSDatabase.DATABASE_TABLE_ART, whereClause, null);
	}
	
	/******************************************************
	 ***			Recipes Database Access				***
	 ******************************************************/
	
	public Cursor getRecipes(int hash_value) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String rawQuery = "SELECT * FROM RECIPES WHERE " + KEY_HASH + "=" + hash_value;
		Cursor cursor = db.rawQuery(rawQuery, null);
		
		return cursor;
	}
	
	public void addNewRecipe (int hashValue, String title, String desc) {
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_REC_TITLE, title);
		newValues.put(KEY_REC_DETAILS, desc);
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_REC, null, newValues);
	}
	
	public void updateRecipe (int hashValue, String title, String desc) {
		// Create a new row of values to insert
		ContentValues newValues = new ContentValues();
			
		// Assign values for each row
		newValues.put(KEY_HASH, hashValue);
		newValues.put(KEY_REC_TITLE, title);
		newValues.put(KEY_REC_DETAILS, desc);
		
		String whereClause = KEY_HASH + "=" + hashValue +
				" AND " + KEY_REC_TITLE + "='" + title + "'";
		
		// Insert the row into your table
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(DATABASE_TABLE_REC, newValues, whereClause, null);
	}
	
	public void deleteRecipe ( int hashValue, String name ){
		
		String whereClause =  KEY_HASH + "=" + hashValue + 
				" AND " + KEY_REC_TITLE + "='" + name + "'";
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PHMSDatabase.DATABASE_TABLE_ART, whereClause, null);
	}
}