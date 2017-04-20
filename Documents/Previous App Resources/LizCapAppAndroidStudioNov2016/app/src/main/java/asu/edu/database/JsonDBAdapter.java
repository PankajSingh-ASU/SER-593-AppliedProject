package asu.edu.database;

import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class JsonDBAdapter {
	private static final String TAG = "GradeCalcDBAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	public static final String MANT_NUM = "MANT";
	public static final String UNKI_NUM = "UNKI";
	public static final String THYS_NUM = "THYS";
	public static final String SOLI_NUM = "SOLI";
	public static final String SCOR_NUM = "SCOR";
	public static final String PSEU_NUM = "PSEU";
	public static final String ORTH_NUM = "ORTH";
	public static final String LEPI_NUM = "LEPI";
	public static final String HYMB_NUM = "HYMB";
	public static final String HYMA_NUM = "HYMA";
	public static final String HETE_NUM = "HETE";
	public static final String DIPT_NUM = "DIPT";
	public static final String DIEL_NUM = "DIEL";
	public static final String DERM_NUM = "DERM";
	public static final String CRUS_NUM = "CRUS";
	public static final String COLE_NUM = "COLE";
	public static final String CHIL_NUM = "CHIL";
	public static final String BLAT_NUM = "BLAT";
	public static final String AUCH_NUM = "AUCH";
	public static final String ARAN_NUM = "ARAN";
	public static final String CHANGED_NUM = "Changed";
	public static final String CHECKDATES_TEXT = "CheckDates_CheckdateID";
	public static final String TRAP_TEXT = "Trap";
	public static final String PREDATOR_TEXT = "Predator";
	public static final String NOTES_TEXT = "Notes";
	public static final String ANTHROPODID_NUM = "ArthropodID";
	public static final String ANTHROPODCAPTUEID_TEXT = "AnthropodCaptureID";
	public static final String CLASS_TEXT = "Class";
	public static final String ORDERNAME_TEXT = "OrderName";
	public static final String ORDERCODE_TEXT = "OrderCode";
	public static final String DESCRIPTION_TEXT = "Description";
	public static final String CHECKDATEID_TEXT = "CheckdateID";
	public static final String HANDLER_TEXT = "Handler";
	public static final String RECORDER_TEXT = "Recorder";
	public static final String DATE_TEXT = "Date";
	public static final String TIME_TEXT = "Time";
	public static final String CLOSED_TEXT = "Closed";
	public static final String NOCAPTURE_TEXT = "NoCapture";
	public static final String COMMENTS_TEXT = "Comments";
	public static final String LOCATIONID_TEXT = "Location_LocationID";
	public static final String HERPID_NUM = "HerpID";
	public static final String TAXA_TEXT = "Taxa";
	public static final String GENUS_TEXT = "Genus";
	public static final String SPECIES_TEXT = "Species";
	public static final String SPPCODE_TEXT = "Spp_Code";
	public static final String LOCATIONID_NUM = "LocationID";
	public static final String SITENAME_TEXT = "SiteName";
	public static final String ARRAYNAME_TEXT = "ArrayName";
	public static final String LATITIUDE_REAL = "Latitude";
	public static final String LONGITUDE_REAL = "Longitude";
	public static final String HERPCAPTUREID_TEXT = "HerpCaptureID";
	public static final String RECAPTURE_TEXT = "Recapture";
	public static final String SVL_NUM = "SVL";
	public static final String VTL_NUM = "VTL";
	public static final String MASS_REAL = "Mass";
	public static final String HDBODY_NUM = "HDBody";
	public static final String SEX_TEXT = "Sex";
	public static final String OTL_NUM = "OTL";
	public static final String DEAD_TEXT = "Dead";
	public static final String HATCHLING_TEXT = "Hatchling";
	public static final String IDNEEDED_TEXT = "IdNeeded";
	public static final String CHECKDATESID_TEXT = "CheckDates_CheckdateID";
	public static final String HERPSID_TEXT = "Herps_HerpID";
	public static final String IDENTIFIERS_TEXT = "Identifiers";

	/**
	 * Database creation sql statement
	 */
	private static final String CREATE_ARTHROPODCAPTURE_TABLE = "CREATE TABLE ArthropodCapture"
			+ " (MANT INTEGER ,"
			+ " UNKI INTEGER ,"
			+ " THYS INTEGER ,"
			+ " SOLI INTEGER ,"
			+ " SCOR INTEGER ,"
			+ " PSEU INTEGER ,"
			+ " ORTH INTEGER ,"
			+ " LEPI INTEGER ,"
			+ " HYMB INTEGER ,"
			+ " HYMA INTEGER ,"
			+ " HETE INTEGER ,"
			+ " DIPT INTEGER ,"
			+ " DIEL INTEGER ,"
			+ " DERM INTEGER ,"
			+ " CRUS INTEGER ,"
			+ " COLE INTEGER ,"
			+ " CHIL INTEGER ,"
			+ " BLAT INTEGER ,"
			+ " AUCH INTEGER ,"
			+ " ARAN INTEGER ,"
			+ " CheckDates_CheckdateID TEXT,"
			+ " Trap TEXT,"
			+ " Predator TEXT,"
			+ " Notes TEXT,"
			+ " AnthropodCaptureID TEXT,"
			+ " Changed INTEGER," + " PRIMARY KEY('AnthropodCaptureID')" + ");";
	private static final String CREATE_ARTHROPOD_TABLE = "CREATE TABLE Arthropods"
			+ " (ArthropodID TEXT,"
			+ " Class TEXT,"
			+ " OrderName TEXT,"
			+ " OrderCode TEXT,"
			+ " Description TEXT,"
			+ " Changed INTEGER,"
			+ " PRIMARY KEY('ArthropodID'));";
	private static final String CREATE_CHECKDATES_TABLE = "CREATE TABLE CheckDates"
			+ " (CheckdateID TEXT,"
			+ " Handler TEXT,"
			+ " Recorder TEXT,"
			+ " Date TEXT,"
			+ " Time TEXT,"
			+ " Closed TEXT,"
			+ " NoCapture TEXT,"
			+ " Comments TEXT,"
			+ " Location_LocationID TEXT,"
			+ " Changed INTEGER,"
			+ " PRIMARY KEY('CheckdateID'));";
	private static final String CREATE_HERPS_TABLE = "CREATE TABLE Herps"
			+ " (HerpID TEXT," 
			+ " Taxa TEXT," 
			+ " Genus TEXT,"
			+ " Species TEXT," 
			+ " Spp_Code TEXT," 
			+ " Changed INTEGER,"
			+ " PRIMARY KEY('HerpID'));";
	private static final String CREATE_LOCATIONS_TABLE = "CREATE TABLE Locations"
			+ " (LocationID TEXT,"
			+ " SiteName TEXT,"
			+ " ArrayName TEXT,"
			+ " Latitude REAL,"
			+ " Longitude REAL,"
			+ " Changed INTEGER,"
			+ " PRIMARY KEY('LocationID'));";
	private static final String CREATE_HERPCAPTURE_TABLE = "CREATE TABLE HerpCapture"
			+ " (HerpCaptureID TEXT,"
			+ " Trap TEXT,"
			+ " Recapture TEXT,"
			+ " SVL INTEGER,"
			+ " VTL INTEGER,"
			+ " Mass REAL,"
			+ " HDBody INTEGER,"
			+ " Sex TEXT,"
			+ " OTL INTEGER,"
			+ " Dead TEXT,"
			+ " Hatchling TEXT,"
			+ " IdNeeded TEXT,"
			+ " Notes TEXT,"
			+ " CheckDates_CheckdateID TEXT,"
			+ " Herps_HerpID TEXT,"
			+ " Identifiers TEXT,"
			+ " Changed INTEGER," + " PRIMARY KEY ('HerpCaptureID'));";

	private static final String DATABASE_NAME = "jsonlizapp";
	public static final String ARTHROPODCAPTURE_TABLE = "ArthropodCapture";
	private static final String ARTHROPOD_TABLE = "Arthropods";
	public static final String CHECKDATES_TABLE = "CheckDates";
	private static final String HERPS_TABLE = "Herps";
	private static final String LOCATIONS_TABLE = "Locations";
	private static final String HERPCAPTURE_TABLE = "HerpCapture";
	private static final int DATABASE_VERSION = 2;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_ARTHROPODCAPTURE_TABLE);
			db.execSQL(CREATE_ARTHROPOD_TABLE);
			db.execSQL(CREATE_CHECKDATES_TABLE);
			db.execSQL(CREATE_HERPS_TABLE);
			db.execSQL(CREATE_LOCATIONS_TABLE);
			db.execSQL(CREATE_HERPCAPTURE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS ArthropodCapture");
			db.execSQL("DROP TABLE IF EXISTS Arthropods");
			db.execSQL("DROP TABLE IF EXISTS CheckDates");
			db.execSQL("DROP TABLE IF EXISTS Herps");
			db.execSQL("DROP TABLE IF EXISTS Locations");
			db.execSQL("DROP TABLE IF EXISTS HerpCapture");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public JsonDBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the database. If it cannot be opened, try to create a new instance
	 * of the database. If it cannot be created, throw an exception to signal
	 * the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public JsonDBAdapter open() throws SQLException {
		if (mDb == null) {
			mDbHelper = new DatabaseHelper(mCtx);
			mDb = mDbHelper.getWritableDatabase();
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
		mDb = null;
	}

	public void deleteall() {
		mDb.delete(ARTHROPODCAPTURE_TABLE, null, null);
		mDb.delete(ARTHROPOD_TABLE, null, null);
		mDb.delete(CHECKDATES_TABLE, null, null);
		mDb.delete(HERPS_TABLE, null, null);
		mDb.delete(LOCATIONS_TABLE, null, null);
		mDb.delete(HERPCAPTURE_TABLE, null, null);
	}
	
	/*
	 * Straight SQL calls to database
	 */

	public void insertDBStatements(String statement) {
		mDb.execSQL(statement);
	}

	public Cursor rawQuery(String query, Object object) {
		return mDb.rawQuery(query, null);
	}

	/*
	 * Create Section
	 */

	public long createAnthropodCapture(int MANT, int UNKI, int THYS, int SOLI,
			int SCOR, int PSEU, int ORTH, int LEPI, int HYMB, int HYMA,
			int HETE, int DIPT, int DIEL, int DERM, int CRUS, int COLE,
			int CHIL, int BLAT, int AUCH, int ARAN, int Changed,
			String CheckDatesId, String Trap, String Predator, String Notes,
			String arhropodcaptureID) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(MANT_NUM, MANT);
		initialValues.put(UNKI_NUM, UNKI);
		initialValues.put(THYS_NUM, THYS);
		initialValues.put(SOLI_NUM, SOLI);
		initialValues.put(SCOR_NUM, SCOR);
		initialValues.put(PSEU_NUM, PSEU);
		initialValues.put(ORTH_NUM, ORTH);
		initialValues.put(LEPI_NUM, LEPI);
		initialValues.put(HYMB_NUM, HYMB);
		initialValues.put(HYMA_NUM, HYMA);
		initialValues.put(HETE_NUM, HETE);
		initialValues.put(DIPT_NUM, DIPT);
		initialValues.put(DIEL_NUM, DIEL);
		initialValues.put(DERM_NUM, DERM);
		initialValues.put(CRUS_NUM, CRUS);
		initialValues.put(COLE_NUM, COLE);
		initialValues.put(CHIL_NUM, CHIL);
		initialValues.put(BLAT_NUM, BLAT);
		initialValues.put(AUCH_NUM, AUCH);
		initialValues.put(ARAN_NUM, ARAN);
		initialValues.put(CHECKDATES_TEXT, CheckDatesId);
		initialValues.put(TRAP_TEXT, Trap);
		initialValues.put(PREDATOR_TEXT, Predator);
		initialValues.put(NOTES_TEXT, Notes);
		initialValues.put(CHANGED_NUM, Changed);
		if (arhropodcaptureID == null){
			initialValues.put(ANTHROPODCAPTUEID_TEXT, UUID.randomUUID().toString());
		} else {
			initialValues.put(ANTHROPODCAPTUEID_TEXT, arhropodcaptureID);
		}
		
		return mDb.insert(ARTHROPODCAPTURE_TABLE, null, initialValues);
	}

	public long createAnthropod(String AntID, String Class, String OrderName,
			String OrderCode, String Description, int Changed) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(CLASS_TEXT, Class);
		initialValues.put(ORDERNAME_TEXT, OrderName);
		initialValues.put(ORDERCODE_TEXT, OrderCode);
		initialValues.put(DESCRIPTION_TEXT, Description);
		initialValues.put(CHANGED_NUM, Changed);
		if (AntID == null){
			initialValues.put(ANTHROPODID_NUM, UUID.randomUUID().toString());
		} else {
			initialValues.put(ANTHROPODID_NUM, AntID);
		}

		return mDb.insert(ARTHROPOD_TABLE, null, initialValues);
	}

	public long createCheckdates(String checkDateID, String handler,
			String recorder, String date, String time, String closed,
			String noCapture, String comments, int changed, String locationId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(HANDLER_TEXT, handler);
		initialValues.put(RECORDER_TEXT, recorder);
		initialValues.put(DATE_TEXT, date);
		initialValues.put(TIME_TEXT, time);
		initialValues.put(CLOSED_TEXT, closed);
		initialValues.put(NOCAPTURE_TEXT, noCapture);
		initialValues.put(COMMENTS_TEXT, comments);
		initialValues.put(LOCATIONID_TEXT, locationId);
		initialValues.put(CHANGED_NUM, changed);
		if (checkDateID == null){
			initialValues.put(CHECKDATEID_TEXT, UUID.randomUUID().toString());
		} else {
			initialValues.put(CHECKDATEID_TEXT, checkDateID);
		}
		
		return mDb.insert(CHECKDATES_TABLE, null, initialValues);
	}

	public long createHerps(String herpId, String taxa, String genus,
			String species, String sppCode, int changed) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(TAXA_TEXT, taxa);
		initialValues.put(GENUS_TEXT, genus);
		initialValues.put(SPECIES_TEXT, species);
		initialValues.put(SPPCODE_TEXT, sppCode);
		initialValues.put(CHANGED_NUM, changed);
		if (herpId == null){
			initialValues.put(HERPID_NUM, UUID.randomUUID().toString());
		} else {
			initialValues.put(HERPID_NUM, herpId);
		}

		return mDb.insert(HERPS_TABLE, null, initialValues);
	}

	public long createLocations(String locationId, String sitename,
			String arrayName, long latitude, long longitude, int changed) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(SITENAME_TEXT, sitename);
		initialValues.put(ARRAYNAME_TEXT, arrayName);
		initialValues.put(LATITIUDE_REAL, latitude);
		initialValues.put(LONGITUDE_REAL, longitude);
		initialValues.put(CHANGED_NUM, changed);
		if (locationId == null){
			initialValues.put(LOCATIONID_NUM, UUID.randomUUID().toString());
		} else {
			initialValues.put(LOCATIONID_NUM, locationId);
		}
		
		return mDb.insert(LOCATIONS_TABLE, null, initialValues);
	}

	public long createHerpcapture(String herpCaptureId, String trap,
			String recapture, int SVL, int VTL, float mass, int hdBody,
			String sex, int OTL, String dead, String hatchling,
			String idNeeded, String notes, String checkDatesId, String herpId,
			String identifiers, int changed) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(TRAP_TEXT, trap);
		initialValues.put(RECAPTURE_TEXT, recapture);
		initialValues.put(SVL_NUM, SVL);
		initialValues.put(VTL_NUM, VTL);
		initialValues.put(MASS_REAL, mass);
		initialValues.put(HDBODY_NUM, hdBody);
		initialValues.put(SEX_TEXT, sex);
		initialValues.put(OTL_NUM, OTL);
		initialValues.put(DEAD_TEXT, dead);
		initialValues.put(HATCHLING_TEXT, hatchling);
		initialValues.put(IDNEEDED_TEXT, idNeeded);
		initialValues.put(NOTES_TEXT, notes);
		initialValues.put(CHECKDATESID_TEXT, checkDatesId);
		initialValues.put(HERPSID_TEXT, herpId);
		initialValues.put(IDENTIFIERS_TEXT, identifiers);
		initialValues.put(CHANGED_NUM, changed);
		if (herpCaptureId == null){
			initialValues.put(HERPCAPTUREID_TEXT, UUID.randomUUID().toString());
		} else {
			initialValues.put(HERPCAPTUREID_TEXT, herpCaptureId);
		}
		
		return mDb.insert(HERPCAPTURE_TABLE, null, initialValues);
	}

	public Cursor fetchAllAnthropodCaptures() {
		return mDb.query(ARTHROPODCAPTURE_TABLE, new String[] { MANT_NUM,
				UNKI_NUM, THYS_NUM, SOLI_NUM, SCOR_NUM, PSEU_NUM, ORTH_NUM,
				LEPI_NUM, HYMB_NUM, HYMA_NUM, HETE_NUM, DIPT_NUM, DIEL_NUM,
				DERM_NUM, CRUS_NUM, COLE_NUM, CHIL_NUM, BLAT_NUM, AUCH_NUM,
				ARAN_NUM, CHECKDATES_TEXT, TRAP_TEXT, PREDATOR_TEXT,
				NOTES_TEXT, CHANGED_NUM , ANTHROPODCAPTUEID_TEXT}, null, null, null, null, null);
	}

	public Cursor fetchAllAnthropod() {
		return mDb.query(ARTHROPOD_TABLE, new String[] { ANTHROPODID_NUM,
				CLASS_TEXT, ORDERNAME_TEXT, ORDERCODE_TEXT, DESCRIPTION_TEXT,
				CHANGED_NUM }, null, null, null, null, null);
	}

	public Cursor fetchAllCheckdates() {
		return mDb.query(CHECKDATES_TABLE, new String[] { CHECKDATEID_TEXT,
				HANDLER_TEXT, RECORDER_TEXT, DATE_TEXT, TIME_TEXT, CLOSED_TEXT,
				NOCAPTURE_TEXT, COMMENTS_TEXT, LOCATIONID_TEXT, CHANGED_NUM },
				null, null, null, null, null);
	}
	
//	public String fetchLatestCheckdateUUID () {
//		Cursor checkdates = mDb.query(CHECKDATES_TABLE, new String[] {CHECKDATEID_TEXT}, null, null, null, null, null);
//		checkdates.moveToLast();
//		return checkdates.getString(0);
//	}
	
//	public void updateLatestCheckdate (String trapState, String comments) {
//		ContentValues updateValues = new ContentValues();
//		updateValues.put(CLOSED_TEXT, trapState);
//		updateValues.put(COMMENTS_TEXT, comments);
//		
//		String UUID = fetchLatestCheckdateUUID();
//		String whereClause = CHECKDATEID_TEXT + "='" + UUID + "'";
//		
//		mDb.update(CHECKDATES_TABLE, updateValues, whereClause, null);
//	}

	public Cursor fetchAllHerps() {
		return mDb.query(HERPS_TABLE, new String[] { HERPID_NUM, TAXA_TEXT,
				GENUS_TEXT, SPECIES_TEXT, SPPCODE_TEXT, CHANGED_NUM }, null,
				null, null, null, null);
	}

	public Cursor fetchAllLocations() {
		return mDb.query(LOCATIONS_TABLE, new String[] { LOCATIONID_NUM,
				SITENAME_TEXT, ARRAYNAME_TEXT, LATITIUDE_REAL, LONGITUDE_REAL,
				CHANGED_NUM }, null, null, null, null, null);
	}

	public Cursor fetchAllHerpcapture() {
		return mDb.query(HERPCAPTURE_TABLE, new String[] { HERPCAPTUREID_TEXT,
				TRAP_TEXT, RECAPTURE_TEXT, SVL_NUM, VTL_NUM, MASS_REAL,
				HDBODY_NUM, SEX_TEXT, OTL_NUM, DEAD_TEXT, HATCHLING_TEXT,
				IDNEEDED_TEXT, NOTES_TEXT, CHECKDATESID_TEXT, HERPSID_TEXT,
				IDENTIFIERS_TEXT, CHANGED_NUM }, null, null, null, null, null);
	}

	public Cursor fetchSelectAnthropod() {
		return mDb.query(ARTHROPOD_TABLE, new String[] { ORDERNAME_TEXT,
				ORDERCODE_TEXT, DESCRIPTION_TEXT }, null, null, null, null, ORDERCODE_TEXT);
	}
	
	public Cursor fetchSelectHerps(String taxa) {
		return mDb.query(HERPS_TABLE, new String[] { SPPCODE_TEXT,
				GENUS_TEXT, SPECIES_TEXT }, TAXA_TEXT + "='" + taxa + "'", null, null, null, SPPCODE_TEXT);
	}
	
	public boolean anthropodCaptureExists(String arthropodCaptureID){
		String sql = "SELECT * FROM " + ARTHROPODCAPTURE_TABLE + " WHERE " + ANTHROPODCAPTUEID_TEXT + " = '" + arthropodCaptureID + "'";
		Cursor data = rawQuery(sql, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		} else {
			data.close();
		    return false;
		}
	}
	
	public boolean anthropodExists(String arthropodID){
		String sql = "SELECT * FROM " + ARTHROPOD_TABLE + " WHERE " + ANTHROPODID_NUM + " = '" + arthropodID + "'";
		Cursor data = rawQuery(sql, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		} else {
			data.close();
		    return false;
		}
	}
	
	public boolean checkdateExists(String checkdateID){
		String sql = "SELECT * FROM " + CHECKDATES_TABLE + " WHERE " + CHECKDATEID_TEXT + " = '" + checkdateID + "'";
		Cursor data = rawQuery(sql, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		} else {
			data.close();
		    return false;
		}
	}
	
	public boolean herpExists(String herpID){
		String sql = "SELECT * FROM " + HERPS_TABLE + " WHERE " + HERPID_NUM + " = '" + herpID + "'";
		Cursor data = rawQuery(sql, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		} else {
			data.close();
		    return false;
		}
	}
	
	public boolean locationExists(String locationID){
		String sql = "SELECT * FROM " + LOCATIONS_TABLE + " WHERE " + LOCATIONID_NUM + " = '" + locationID + "'";
		Cursor data = rawQuery(sql, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		} else {
			data.close();
		    return false;
		}
	}
	
	public boolean herpCaptureExists(String herpCapID){
		String sql = "SELECT * FROM " + HERPCAPTURE_TABLE + " WHERE " + HERPCAPTUREID_TEXT + " = '" + herpCapID + "'";
		Cursor data = rawQuery(sql, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		} else {
			data.close();
		    return false;
		}
	}
	
	public Boolean updateAnthropodCapture(int MANT, int UNKI, int THYS, int SOLI,
			int SCOR, int PSEU, int ORTH, int LEPI, int HYMB, int HYMA,
			int HETE, int DIPT, int DIEL, int DERM, int CRUS, int COLE,
			int CHIL, int BLAT, int AUCH, int ARAN, int Changed,
			String CheckDatesId, String Trap, String Predator, String Notes,
			String arhropodcaptureID) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(MANT_NUM, MANT);
		updateValues.put(UNKI_NUM, UNKI);
		updateValues.put(THYS_NUM, THYS);
		updateValues.put(SOLI_NUM, SOLI);
		updateValues.put(SCOR_NUM, SCOR);
		updateValues.put(PSEU_NUM, PSEU);
		updateValues.put(ORTH_NUM, ORTH);
		updateValues.put(LEPI_NUM, LEPI);
		updateValues.put(HYMB_NUM, HYMB);
		updateValues.put(HYMA_NUM, HYMA);
		updateValues.put(HETE_NUM, HETE);
		updateValues.put(DIPT_NUM, DIPT);
		updateValues.put(DIEL_NUM, DIEL);
		updateValues.put(DERM_NUM, DERM);
		updateValues.put(CRUS_NUM, CRUS);
		updateValues.put(COLE_NUM, COLE);
		updateValues.put(CHIL_NUM, CHIL);
		updateValues.put(BLAT_NUM, BLAT);
		updateValues.put(AUCH_NUM, AUCH);
		updateValues.put(ARAN_NUM, ARAN);
		updateValues.put(CHECKDATES_TEXT, CheckDatesId);
		updateValues.put(TRAP_TEXT, Trap);
		updateValues.put(PREDATOR_TEXT, Predator);
		updateValues.put(NOTES_TEXT, Notes);
		updateValues.put(CHANGED_NUM, Changed);
		
		return mDb.update(ARTHROPODCAPTURE_TABLE, updateValues, ANTHROPODCAPTUEID_TEXT + "='" + arhropodcaptureID + "'", null) > 0;
	}
	
	public Boolean updateAnthropod(String AntID, String Class, String OrderName,
			String OrderCode, String Description, int Changed) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(CLASS_TEXT, Class);
		updateValues.put(ORDERNAME_TEXT, OrderName);
		updateValues.put(ORDERCODE_TEXT, OrderCode);
		updateValues.put(DESCRIPTION_TEXT, Description);
		updateValues.put(CHANGED_NUM, Changed);

		return mDb.update(ARTHROPOD_TABLE, updateValues, ANTHROPODID_NUM + "='" + AntID + "'", null) > 0;
	}

	public Boolean updateCheckdates(String checkDateID, String handler,
			String recorder, String date, String time, String closed,
			String noCapture, String comments, int changed, String locationId) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(HANDLER_TEXT, handler);
		updateValues.put(RECORDER_TEXT, recorder);
		updateValues.put(DATE_TEXT, date);
		updateValues.put(TIME_TEXT, time);
		updateValues.put(CLOSED_TEXT, closed);
		updateValues.put(NOCAPTURE_TEXT, noCapture);
		updateValues.put(COMMENTS_TEXT, comments);
		updateValues.put(LOCATIONID_TEXT, locationId);
		updateValues.put(CHANGED_NUM, changed);
		
		return mDb.update(CHECKDATES_TABLE, updateValues, CHECKDATEID_TEXT + "='" + checkDateID + "'", null) > 0;
	}
	
	public Boolean updateCheckdateLocationInfo (String checkDateId, String trapState, String locationComments) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(JsonDBAdapter.CLOSED_TEXT, trapState);
		updateValues.put(JsonDBAdapter.COMMENTS_TEXT, locationComments);
		
		return mDb.update(JsonDBAdapter.CHECKDATES_TABLE, updateValues, JsonDBAdapter.CHECKDATEID_TEXT + "='" + checkDateId + "'", null) > 0;
	}

	public Boolean updateHerps(String herpId, String taxa, String genus,
			String species, String sppCode, int changed) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(TAXA_TEXT, taxa);
		updateValues.put(GENUS_TEXT, genus);
		updateValues.put(SPECIES_TEXT, species);
		updateValues.put(SPPCODE_TEXT, sppCode);
		updateValues.put(CHANGED_NUM, changed);

		return mDb.update(HERPS_TABLE, updateValues, HERPID_NUM + "='" + herpId + "'", null) > 0;
	}

	public Boolean updateLocations(String locationId, String sitename,
			String arrayName, long latitude, long longitude, int changed) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(SITENAME_TEXT, sitename);
		updateValues.put(ARRAYNAME_TEXT, arrayName);
		updateValues.put(LATITIUDE_REAL, latitude);
		updateValues.put(LONGITUDE_REAL, longitude);
		updateValues.put(CHANGED_NUM, changed);

		return mDb.update(LOCATIONS_TABLE, updateValues, LOCATIONID_NUM + "='" + locationId + "'", null) > 0;
	}

	public Boolean updateHerpcapture(String herpCaptureId, String trap,
			String recapture, int SVL, int VTL, float mass, int hdBody,
			String sex, int OTL, String dead, String hatchling,
			String idNeeded, String notes, String checkDatesId, String herpId,
			String identifiers, int changed) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(TRAP_TEXT, trap);
		updateValues.put(RECAPTURE_TEXT, recapture);
		updateValues.put(SVL_NUM, SVL);
		updateValues.put(VTL_NUM, VTL);
		updateValues.put(MASS_REAL, mass);
		updateValues.put(HDBODY_NUM, hdBody);
		updateValues.put(SEX_TEXT, sex);
		updateValues.put(OTL_NUM, OTL);
		updateValues.put(DEAD_TEXT, dead);
		updateValues.put(HATCHLING_TEXT, hatchling);
		updateValues.put(IDNEEDED_TEXT, idNeeded);
		updateValues.put(NOTES_TEXT, notes);
		updateValues.put(CHECKDATESID_TEXT, checkDatesId);
		updateValues.put(HERPSID_TEXT, herpId);
		updateValues.put(IDENTIFIERS_TEXT, identifiers);
		updateValues.put(CHANGED_NUM, changed);

		return mDb.update(HERPCAPTURE_TABLE, updateValues, HERPCAPTUREID_TEXT + "='" + herpCaptureId + "'", null) > 0;
	}

	public Boolean anyToUpdate(){
		Cursor data = mDb.query(ARTHROPODCAPTURE_TABLE, new String[] { CHANGED_NUM }, CHANGED_NUM + "='" + 1 + "'", null, null, null, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		}
		data.close();
		data = mDb.query(ARTHROPOD_TABLE, new String[] { CHANGED_NUM }, CHANGED_NUM + "='" + 1 + "'", null, null, null, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		}
		data.close();
		data = mDb.query(CHECKDATES_TABLE, new String[] { CHANGED_NUM }, CHANGED_NUM + "='" + 1 + "'", null, null, null, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		}
		data.close();
		data = mDb.query(HERPS_TABLE, new String[] { CHANGED_NUM }, CHANGED_NUM + "='" + 1 + "'", null, null, null, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		}
		data.close();
		data = mDb.query(LOCATIONS_TABLE, new String[] { CHANGED_NUM }, CHANGED_NUM + "='" + 1 + "'", null, null, null, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		}
		data.close();
		data = mDb.query(HERPCAPTURE_TABLE, new String[] { CHANGED_NUM }, CHANGED_NUM + "='" + 1 + "'", null, null, null, null);
		if (data.moveToFirst()) {
			data.close();
		    return true;
		}
		data.close();
		return false;
	}
    
	public boolean deleteArthropodCapture(String arthropodCaptureID){
		return mDb.delete(ARTHROPODCAPTURE_TABLE, ANTHROPODCAPTUEID_TEXT + "='" + arthropodCaptureID + "'", null) > 0;
	}
	
	public boolean deleteArthropod(String arthropodID){
		return mDb.delete(ARTHROPOD_TABLE, ANTHROPODID_NUM + "='" + arthropodID + "'", null) > 0;
	}
	
	public boolean deleteCheckdate(String checkdateID){
		return mDb.delete(CHECKDATES_TABLE, CHECKDATEID_TEXT + "='" + checkdateID + "'", null) > 0;
	}
	
	public boolean deleteHerp(String herpID){
		return mDb.delete(HERPS_TABLE, HERPID_NUM + "='" + herpID + "'", null) > 0;
	}
	
	public boolean deleteLocation(String locationID){
		return mDb.delete(LOCATIONS_TABLE, LOCATIONID_NUM + "='" + locationID + "'", null) > 0;
	}
	
	public boolean deleteHerpCapture(String herpCapID){
		return mDb.delete(HERPCAPTURE_TABLE, HERPCAPTUREID_TEXT + "='" + herpCapID + "'", null) > 0;
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		return mDb.update(table, values, whereClause, whereArgs);
	}
}
