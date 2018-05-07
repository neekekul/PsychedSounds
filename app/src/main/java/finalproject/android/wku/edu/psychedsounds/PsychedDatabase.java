package finalproject.android.wku.edu.psychedsounds;
//LUKE KEEN
//5/8/2018
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class PsychedDatabase extends SQLiteOpenHelper {

    // Define a tag that is used to log any kind of error or comment
    private static final String LOGTAG = "PSYCHEDDATABASE";

    // Define a database name and version number
    private static final String DATABASE_NAME = "psychedsoundboard.db";
    private static final int DATABASE_VERSION = 1;

    // MAIN_TABLE contains a set of main sounds for the soundboard
    private static final String MAIN_TABLE = "main_table";

    // Define information about the main table
    private static final String MAIN_ID = "_id";
    private static final String MAIN_NAME = "soundName";
    private static final String MAIN_ITEM_ID = "soundId";


    // FAVORITES_TABLE contains all sounds that were set as favorites by the user
    private static final String FAVORITES_TABLE = "favorites_table";

    // Define information about the favorites table
    private static final String FAVORITES_ID = "_id";
    private static final String FAVORITES_NAME = "favoriteName";
    private static final String FAVORITES_ITEM_ID = "favoriteId";

    // Define the SQL statements to create both tables
    private static final String SQL_CREATE_MAIN_TABLE = "CREATE TABLE IF NOT EXISTS " + MAIN_TABLE + "(" + MAIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MAIN_NAME + " TEXT, " + MAIN_ITEM_ID + " INTEGER unique);";

    // The sound resource id in FAVORITES_TABLE is not unique because we have to set it again on every app update because every resource id changes if you add new resources
    private static final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE IF NOT EXISTS " + FAVORITES_TABLE + "(" + FAVORITES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FAVORITES_NAME + " TEXT, " + FAVORITES_ITEM_ID + " INTEGER);";

    // Create a constructor to start an instance of PsychedDatabase that will create the database
    public PsychedDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOGTAG, "Database successfully initialised: " + getDatabaseName());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try{
            // Execute the table creation statements
            sqLiteDatabase.execSQL(SQL_CREATE_MAIN_TABLE);
            sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);

        } catch(Exception e){

            Log.e(LOGTAG, "Failed to create database: " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // I am using the app version instead of the database version to upgrade the database, so this method is unnecessary
        // If it gets called it should only delete the main table that will be refilled again in the MainActivity,
        // or any other soundboard activities.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MAIN_TABLE);
        onCreate(sqLiteDatabase);

    }

    public void injectSounds(Context context){

        //populate a String list with a string array that I made in my string resource file
        List<String> names = Arrays.asList(context.getResources().getStringArray(R.array.soundTitles));

        //create all the sound objects that I want and reference my actual .wav files in the raw resource directory
        Sound[] soundObjectsMain = {new Sound(names.get(0), R.raw.airyhit),
                new Sound(names.get(1), R.raw.alarmring),
                new Sound(names.get(2), R.raw.ambition),
                new Sound(names.get(3), R.raw.anothaone),
                new Sound(names.get(4), R.raw.argh),
                new Sound(names.get(5), R.raw.aye),
                new Sound(names.get(6), R.raw.ayy),
                new Sound(names.get(7), R.raw.balloonhit),
                new Sound(names.get(8), R.raw.bang),
                new Sound(names.get(9), R.raw.bass),
                new Sound(names.get(10), R.raw.blessup),
                new Sound(names.get(11), R.raw.bonk),
                new Sound(names.get(12), R.raw.bopdelay),
                new Sound(names.get(13), R.raw.bounce),
                new Sound(names.get(14), R.raw.bubbly),
                new Sound(names.get(15), R.raw.bush),
                new Sound(names.get(16), R.raw.cash),
                new Sound(names.get(17), R.raw.cha),
                new Sound(names.get(18), R.raw.church),
                new Sound(names.get(19), R.raw.churchbelle),
                new Sound(names.get(20), R.raw.churchbellf),
                new Sound(names.get(21), R.raw.contemplation),
                new Sound(names.get(22), R.raw.coolchristmas),
                new Sound(names.get(23), R.raw.cosmic),
                new Sound(names.get(24), R.raw.creepin),
                new Sound(names.get(25), R.raw.crow),
                new Sound(names.get(26), R.raw.cuba),
                new Sound(names.get(27), R.raw.dbz),
                new Sound(names.get(28), R.raw.diamonds),
                new Sound(names.get(29), R.raw.dogbark),
                new Sound(names.get(30), R.raw.eaglecry),
                new Sound(names.get(31), R.raw.eerie),
                new Sound(names.get(32), R.raw.elephantus),
                new Sound(names.get(33), R.raw.fireplace),
                new Sound(names.get(34), R.raw.flee),
                new Sound(names.get(35), R.raw.glassshatterone),
                new Sound(names.get(36), R.raw.glassshattertwo),
                new Sound(names.get(37), R.raw.godstab),
                new Sound(names.get(38), R.raw.grease),
                new Sound(names.get(39), R.raw.ha),
                new Sound(names.get(40), R.raw.hailie),
                new Sound(names.get(41), R.raw.hammerhit),
                new Sound(names.get(42), R.raw.hay),
                new Sound(names.get(43), R.raw.heartbeat),
                new Sound(names.get(44), R.raw.helloll),
                new Sound(names.get(45), R.raw.hey),
                new Sound(names.get(46), R.raw.hi),
                new Sound(names.get(47), R.raw.hiccup),
                new Sound(names.get(48), R.raw.holdup),
                new Sound(names.get(49), R.raw.holla),
                new Sound(names.get(50), R.raw.hollup),
                new Sound(names.get(51), R.raw.horn),
                new Sound(names.get(52), R.raw.hostility),
                new Sound(names.get(53), R.raw.hush),
                new Sound(names.get(54), R.raw.ilikethat),
                new Sound(names.get(55), R.raw.jaimaicandrum),
                new Sound(names.get(56), R.raw.keepitonehunnid),
                new Sound(names.get(57), R.raw.knifesliceone),
                new Sound(names.get(58), R.raw.knifeslicetwo),
                new Sound(names.get(59), R.raw.listen),
                new Sound(names.get(60), R.raw.lit),
                new Sound(names.get(61), R.raw.london),
                new Sound(names.get(62), R.raw.louder),
                new Sound(names.get(63), R.raw.lurch),
                new Sound(names.get(64), R.raw.metallichit),
                new Sound(names.get(65), R.raw.mind),
                new Sound(names.get(66), R.raw.naveshot),
                new Sound(names.get(67), R.raw.nowtoyourlocalforcast),
                new Sound(names.get(68), R.raw.oh),
                new Sound(names.get(69), R.raw.ohdear),
                new Sound(names.get(70), R.raw.ohmygosh),
                new Sound(names.get(71), R.raw.oj),
                new Sound(names.get(72), R.raw.onehunnid),
                new Sound(names.get(73), R.raw.over),
                new Sound(names.get(74), R.raw.paah),
                new Sound(names.get(75), R.raw.pimp),
                new Sound(names.get(76), R.raw.pinknoise),
                new Sound(names.get(77), R.raw.pitchdrophit),
                new Sound(names.get(78), R.raw.pitchyslide),
                new Sound(names.get(79), R.raw.poprocks),
                new Sound(names.get(80), R.raw.poproll),
                new Sound(names.get(81), R.raw.rip),
                new Sound(names.get(82), R.raw.roadsahead),
                new Sound(names.get(83), R.raw.sam),
                new Sound(names.get(84), R.raw.schoolbell),
                new Sound(names.get(85), R.raw.scratchyone),
                new Sound(names.get(86), R.raw.scratchytwo),
                new Sound(names.get(87), R.raw.scream),
                new Sound(names.get(88), R.raw.shelldrop),
                new Sound(names.get(89), R.raw.shortsweep),
                new Sound(names.get(90), R.raw.sineflop),
                new Sound(names.get(91), R.raw.six),
                new Sound(names.get(92), R.raw.soul),
                new Sound(names.get(93), R.raw.streaks),
                new Sound(names.get(94), R.raw.streets),
                new Sound(names.get(95), R.raw.sundaymorning),
                new Sound(names.get(96), R.raw.sunset),
                new Sound(names.get(97), R.raw.swordslice),
                new Sound(names.get(98), R.raw.swordslicetwo),
                new Sound(names.get(99), R.raw.syruphit),
                new Sound(names.get(100), R.raw.thezoo),
                new Sound(names.get(101), R.raw.toronto),
                new Sound(names.get(102), R.raw.trippy),
                new Sound(names.get(103), R.raw.trophies),
                new Sound(names.get(104), R.raw.turntableone),
                new Sound(names.get(105), R.raw.turntabletwo),
                new Sound(names.get(106), R.raw.twinkle),
                new Sound(names.get(107), R.raw.undertheweather),
                new Sound(names.get(108), R.raw.victory),
                new Sound(names.get(109), R.raw.warhorn),
                new Sound(names.get(110), R.raw.waterdrop),
                new Sound(names.get(111), R.raw.waterdroptwo),
                new Sound(names.get(112), R.raw.waves),
                new Sound(names.get(113), R.raw.weep),
                new Sound(names.get(114), R.raw.whitenoiseflat),
                new Sound(names.get(115), R.raw.whitenoisehit),
                new Sound(names.get(116), R.raw.whowantsthebass),
                new Sound(names.get(117), R.raw.widedrop),
                new Sound(names.get(118), R.raw.work),
                new Sound(names.get(119), R.raw.zapone),
                new Sound(names.get(120), R.raw.zaptwo),
                new Sound(names.get(121), R.raw.zapthree)};



        //use the object array to populate the main table using injectionMain();
        for(Sound x: soundObjectsMain){
                injectionMain(x);
        }


    }

    //inject sounds into the main table
    private void injectionMain(Sound x){

        // Get a writable instance of the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the soundId already exists in the table then add it to the table if it does not exist
        if (!checkDuplicates(db, MAIN_TABLE, MAIN_ITEM_ID, x.getId()) ){

            try {

                // Put the information into a ContentValues object
                ContentValues contentValues = new ContentValues();

                contentValues.put(MAIN_NAME, x.getName());
                contentValues.put(MAIN_ITEM_ID, x.getId());

                // Insert the Sound x into the MAIN_TABLE
                db.insert(MAIN_TABLE, null, contentValues);
            } catch (Exception e){

                Log.e(LOGTAG, "(MAIN) Failed to insert sound: " + e.getMessage());
            } finally {

                db.close();
            }
        }

    }



    //check the database for duplicate sounds before adding any more.
    private boolean checkDuplicates(SQLiteDatabase db, String tableName, String idRow, Integer soundId){

        int count = -1;
        Cursor cursor = null;

        try {

            // Get all rows from the selected table that contain the given sound id
            String query = "SELECT * FROM " + tableName + " WHERE " + idRow + " = " + soundId;
            cursor = db.rawQuery(query, null);

            // If the entry with the given sound id exists get the rows _id as count value
            if (cursor.moveToFirst()){

                count = cursor.getInt(0);
            }

            // Return true if sound exists in the selected table (i.e. we counted more than zero duplicates)
            return (count > 0);

        } finally {

            // close the cursor after the check
            if (cursor != null){

                cursor.close();
            }
        }

    }

    // Returns a Cursor with all entries of the MAIN_TABLE
    // Cursor will be closed after Sounds were added to the ArrayList in MainActivity
    public Cursor getPyschedSounds(){
        // Get a readable instance of the database
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + MAIN_TABLE + " ORDER BY " + MAIN_NAME, null);
    }

    // Add a sound to favorites / FAVORITES_TABLE
    public void addFavorite(Sound x){

        // Get a writable instance of the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the soundId already exists in the table then add it to the table if it does not already exist
        if (!checkDuplicates(db, FAVORITES_TABLE, FAVORITES_ITEM_ID, x.getId()) ) {

            try{

                // Put the information into a ContentValues object
                ContentValues contentValues = new ContentValues();

                contentValues.put(FAVORITES_NAME, x.getName());
                contentValues.put(FAVORITES_ITEM_ID, x.getId());

                // Insert the Sound into the FAVORITES_TABLE
                db.insert(FAVORITES_TABLE, null, contentValues);
            } catch (Exception e){

                Log.e(LOGTAG, "(FAVORITES) Failed to insert sound: " + e.getMessage());
            } finally {

                db.close();
            }
        }
    }

    // Remove a sound from favorites / FAVORITES_TABLE
    public void removeFavorite(Context context, Sound x){

        // Get a writable instance of the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the soundId allready exists in the table then remove it from the table if it exists
        if (checkDuplicates(db, FAVORITES_TABLE, FAVORITES_ITEM_ID, x.getId()) ) {

            try {

                // Remove entry from database table
                db.delete(FAVORITES_TABLE, FAVORITES_ITEM_ID + " = " + x.getId(), null);

                // Restart the activity to display changes
                Activity activity = (Activity) context;
                Intent intent = activity.getIntent();
                // Disable activity transitioning animations
                activity.overridePendingTransition(0,0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.finish();
                activity.overridePendingTransition(0,0);
                context.startActivity(intent);

            } catch (Exception e){

                Log.e(LOGTAG, "(FAVORITES) Failed to remove sound: " + e.getMessage());
            } finally {

                db.close();
            }
        }
    }

    // Returns a Cursor with all entries of the FAVORITES_TABLE
    // Cursor will be closed after Sounds are added to the ArrayList in the MainActivity
    public Cursor getFavoriteSounds(){

        // Get a readable instance of the database
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + FAVORITES_TABLE + " ORDER BY " + FAVORITES_NAME, null);
    }

    // When adding sounds to the soundboard and updating, the resource ids might change..
    // This method will update the resource ids in the FAVORITES_TABLE
    public void updateFavorites(){

        // Get a writable instance of the database
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            // Get all data from the FAVORITES_TABLE
            Cursor favorite_content = db.rawQuery("SELECT * FROM " + FAVORITES_TABLE, null);

            // Check if the cursor is empty or failed to convert the data
            if (favorite_content.getCount() == 0){

                Log.d(LOGTAG, "Cursor is empty or failed to convert data");
                favorite_content.close();
            }


            while (favorite_content.moveToNext()){

                // Set a String that will contain the name of the current sound
                String entryName = favorite_content.getString(favorite_content.getColumnIndex(FAVORITES_NAME));

                // Get the entry of MAIN_TABLE where the name of the current favorite sound appears
                Cursor updateEntry = db.rawQuery("SELECT * FROM " + MAIN_TABLE + " WHERE " + MAIN_NAME + " = '" + entryName + "'", null);

                // You can log the name of the sound that is in the update order right now for debug reasons
                Log.d(LOGTAG, "Currently working on: " + entryName);

                // Check if the cursor is empty or failed to convert the data
                if (updateEntry.getCount() == 0){

                    Log.d(LOGTAG, "Cursor is empty or failed to convert data");
                    updateEntry.close();
                }

                // Move to the cursors first position (should only have 1 position)
                updateEntry.moveToFirst();

                // Check if the resource ids match and update the favorite resource id if necessary
                if (favorite_content.getInt(favorite_content.getColumnIndex(FAVORITES_ITEM_ID)) != updateEntry.getInt(updateEntry.getColumnIndex(MAIN_ITEM_ID)) ){

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FAVORITES_ITEM_ID, updateEntry.getInt(updateEntry.getColumnIndex(MAIN_ITEM_ID)));
                    db.update(FAVORITES_TABLE, contentValues, FAVORITES_NAME + " = '" + entryName + "'", null);

                    // You can log the name of the sound that has been updated for debug reasons
                    Log.d(LOGTAG, "Updated sound: " + entryName);
                }
                // You can log the name of the sound if it is allready up to date for debug reasons
                else {

                    Log.d(LOGTAG, "Already up to date: " + entryName);
                }
            }


        } catch (Exception e) {

            Log.e(LOGTAG, "Failed to update favorites: " + e.getMessage());
        } finally {

            db.close();
        }
    }

    // Gets called when app is updated and recreates the MAIN_TABLE
    public void appUpdate(){

        try {

            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("DROP TABLE IF EXISTS " + MAIN_TABLE);

            db.execSQL(SQL_CREATE_MAIN_TABLE);

            db.close();

        } catch (Exception e) {

            Log.e(LOGTAG, "Failed to update the main table on app update: " + e.getMessage());
        }
    }
}
