package finalproject.android.wku.edu.psychedsounds;
//LUKE KEEN
//5/8/2018
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Define a tag that is used to log any kind of error or comment
    private static final String LOGTAG = "MainActivity";

    //Array list that holds Sound objects (This is referring to the class "Sound" in this same project)
    ArrayList<Sound> soundList = new ArrayList<>();

    //Instantiate my Recycler View
    RecyclerView soundS;

    //Instantiate a recyclerAdapter (This is referring to the class "recyclerAdapter" in this same project)
    // with "soundList" as its parameter
    recyclerAdapter soundsAdapter = new recyclerAdapter(soundList);

    //Instantiate a layout manager for my RecyclerView
    RecyclerView.LayoutManager soundsManager;

    private View layout;

    //Instantiate a PsychedDatabase to support database usage
    PsychedDatabase psychedDatabase = new PsychedDatabase(this);

    //gesture
    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup the toolbar
        Toolbar myToolBar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);

        myToolBar.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                //signal for the start of a drag and drop operation.
                case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
                //the drag point has entered the bounding box of the View
                case DragEvent.ACTION_DRAG_ENTERED:
                //do nothing
                break;
                //the user has moved the drag shadow outside the bounding box of the View
                case DragEvent.ACTION_DRAG_EXITED:
                //do nothing
                break;
                //drag shadow has been released,the drag point is within the bounding box of the View
                case DragEvent.ACTION_DROP:
                // if the view is the first hats resting location, we accept the drag item
                if(v == findViewById(R.id.my_toolbar)) {

                } else {
                    Context context = getApplicationContext();
                    Toast.makeText(context, "You can't drop the image here",
                            Toast.LENGTH_LONG).show();
                    break;
                }
                break;
                //the drag and drop operation has concluded.
                case DragEvent.ACTION_DRAG_ENDED:
                //do nothing
                default:
                break;
            }
                return true;
            }
        });

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        // If the gets an update or runs for the first time fill the database with all SoundObjects
        if (appUpdate()){

            psychedDatabase.injectSounds(this);

            psychedDatabase.updateFavorites();
        }

        layout = findViewById(R.id.layout);

        //fill the arraylist with the database information
        populateArrayList();

        //connect my RecyclerView to the actual layout file
        soundS = (RecyclerView) findViewById(R.id.soundBoard);

        //use the layout manager to set a grid layout with a column count of 4
        soundsManager = new GridLayoutManager(this, 4);

        //attach the layout manager
        soundS.setLayoutManager(soundsManager);

        //attach the adapter
        soundS.setAdapter(soundsAdapter);

        //ask the user for the correct permission, if they have not already done so
        getPermissions();

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu up.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //prepare an intent
        Intent intent;
        //switch statement for the actions in my menu.
        switch (item.getItemId()) {
            case R.id.action_favorite:
                // User chose the "Favorite" action, send them to the favorites activity
                intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // Fill the soundList with all information given in the MAIN_TABLE
    private void populateArrayList(){

        soundList.clear();

        // Get a cursor filled with all information from the MAIN_TABLE
        Cursor cursor = psychedDatabase.getPyschedSounds();

        // Check if the cursor is empty or failed to convert the data
        if (cursor.getCount() == 0){

            Log.e(LOGTAG, "Cursor is empty or failed to convert data");
            cursor.close();
        }

        // Prevent the method from adding Sounds again, everytime the Activity starts
        if (cursor.getCount() != soundList.size() ){

            // Add each item of MAIN_TABLE to soundList and refresh the RecyclerView by notifying the adapter about changes
            while (cursor.moveToNext() ){

                String NAME = cursor.getString(cursor.getColumnIndex("soundName"));
                Integer ID = cursor.getInt(cursor.getColumnIndex("soundId"));

                soundList.add(new Sound(NAME, ID));

                soundsAdapter.notifyDataSetChanged();
            }

            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Event.releaseAux();
    }

    //permissions method/tool
    private void getPermissions(){

        // Check if the users Android version is equal to or higher than Android 6 (Marshmallow)
        // Since Android 6 you have to request permissions at runtime to provide a better security
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            }

            // Check if the permission to write the users settings is not granted
            // You need this permission to set a sound as ringtone or the like
            if (!Settings.System.canWrite(this)){

                // Displays a little bar on the bottom of the activity with an OK button that will open a so called permission management screen
                Snackbar.make(layout,"Psyched Sounds needs access to your settings", Snackbar.LENGTH_INDEFINITE).setAction("OKAY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + view.getContext().getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).show();

            }

        }

    }

    // Check if the app has been updated
    private boolean appUpdate(){

        // We are saving the current app version into a preference file
        // There are two ways to get a handle to a SharedPreferences, we are creating
        // a unique preference file that is not bound to a context
        // Check the android developer documentation if you want to find out more

        // Define a name for the preference file and a key name to save the version code to it
        final String PREFS_NAME = "VersionPref";
        final String PREF_VERSION_CODE_KEY = "version_code";
        // Define a value that is set if the key does not exist
        final int DOESNT_EXIST = -1;

        // Get the current version code from the package
        int currentVersionCode = 0;
        try{

            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

        } catch (PackageManager.NameNotFoundException e){

            Log.e(LOGTAG, e.getMessage());
        }

        // Get the SharedPreferences from the preference file
        // Creates the preference file if it does not exist
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Get the saved version code or set it if it does not exist
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Create an editor to edit the shared preferences on app update
        SharedPreferences.Editor edit = prefs.edit();

        //Check for updates
        if (savedVersionCode == DOESNT_EXIST){

            psychedDatabase.appUpdate();
            // First run of the app
            // Set the saved version code to the current version code
            edit.putInt(PREF_VERSION_CODE_KEY, currentVersionCode);
            edit.commit();
            return true;
        }
        else if (currentVersionCode > savedVersionCode){

            // App update
            psychedDatabase.appUpdate();
            edit.putInt(PREF_VERSION_CODE_KEY, currentVersionCode);
            edit.commit();
            return true;
        }

        return false;
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Intent intent;
            if (e2.getX() > e1.getX()){

                // User chose the "Favorite" action, send them to the favorites activity
                intent = new Intent(MainActivity.this, FavoriteActivity.class);
                finish();
                startActivity(intent);

            }else if (e2.getX() < e1.getX()){

                intent = new Intent(MainActivity.this, FavoriteActivity.class);
                finish();
                startActivity(intent);

            }
            return true;
        }
    }

}
