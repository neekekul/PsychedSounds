package finalproject.android.wku.edu.psychedsounds;
//LUKE KEEN
//5/8/2018
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    // Define a tag that is used to log any kind of error or comment
    private static final String LOGTAG = "FavoriteActivity";

    //Array list that holds Sound objects (This is referring to the class "Sound" in this same project)
    ArrayList<Sound> soundList = new ArrayList<>();

    //Instantiate my Recycler View
    RecyclerView soundS;

    //Instantiate a recyclerAdapter (This is referring to the class "recyclerAdapter" in this same project) with "soundList" as its parameter
    recyclerAdapter soundsAdapter = new recyclerAdapter(soundList);

    //Instantiate a layout manager for my RecyclerView
    RecyclerView.LayoutManager soundsManager;

    private View layout;

    //Instantiate a PsychedDatabase to support database usage
    PsychedDatabase psychedDatabase = new PsychedDatabase(this);

    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //setup the toolbar
        Toolbar myToolBar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);

        ActionBar ab = getSupportActionBar();
        //this is where the up button is displayed.
        ab.setDisplayHomeAsUpEnabled(true);

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        //fill the array list with values from the favorites table
        populateArrayList();

        //connect my RecyclerView to the actual layout file
        soundS = (RecyclerView) findViewById(R.id.soundBoard);

        //use the layout manager to set a grid layout with a column count of 3
        soundsManager = new GridLayoutManager(this, 3);

        //attach the layout manager
        soundS.setLayoutManager(soundsManager);

        //attach the adapter
        soundS.setAdapter(soundsAdapter);
    }

    // Fill the soundList with all information given in the MAIN_TABLE
    private void populateArrayList(){

        soundList.clear();

        // Get a cursor filled with all information from the MAIN_TABLE
        Cursor cursor = psychedDatabase.getFavoriteSounds();

        // Check if the cursor is empty or failed to convert the data
        if (cursor.getCount() == 0){

            Log.e(LOGTAG, "Cursor is empty or failed to convert data");
            cursor.close();
        }

        // Prevent the method from adding SoundObjects again everytime the Activity starts
        if (cursor.getCount() != soundList.size() ){

            // Add each item of MAIN_TABLE to soundList and refresh the RecyclerView by notifying the adapter about changes
            while (cursor.moveToNext() ){

                String NAME = cursor.getString(cursor.getColumnIndex("favoriteName"));
                Integer ID = cursor.getInt(cursor.getColumnIndex("favoriteId"));

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

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Intent intent;
            if (e2.getX() > e1.getX()){

                // User chose the "Favorite" action, send them to the favorites activity
                intent = new Intent(FavoriteActivity.this, MainActivity.class);
                finish();
                startActivity(intent);

            }else if (e2.getX() < e1.getX()){

                intent = new Intent(FavoriteActivity.this, MainActivity.class);
                finish();
                startActivity(intent);

            }
            return true;
        }
    }
}
