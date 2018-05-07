package finalproject.android.wku.edu.psychedsounds;
//LUKE KEEN
//5/8/2018
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
An Event is what my project is using to process the media file as a View is pressed on the screen.
 */
public class Event {

    //TAG
   private static final String LOGTAG = "EVENT";

   //Declare a mediaplayer
   private static MediaPlayer aux;

   // Declare a PsychedDatabase to support database usage
   private static PsychedDatabase psychedDatabase;

   //start the player
   public static void startAux(View v, Integer id){

       //try to play the sound
       try{

           if (id != null){

               if (aux != null){
                   aux.reset();
               }

               aux = MediaPlayer.create(v.getContext(), id);
               aux.start();

           }

           //make sure to notify on failure
       }catch(Exception e){

           Log.e(LOGTAG, "Error trying to play sound: "+ e.getMessage());

       }


   }


   //release the mediaplayer
   public static void releaseAux(){

       if (aux != null){
           aux.release();
           aux = null;
       }


   }

   //not implemented fully.
   public static void swipeToFavorite(final View v, final Sound s){
       // Identify the current activity
       if (v.getContext() instanceof FavoriteActivity) {
           //if inside the FavoriteActivity
           psychedDatabase.removeFavorite(v.getContext(), s);
           CharSequence text = "Sound removed from favorites...";
           int duration = Toast.LENGTH_LONG;
           Toast toast = Toast.makeText(v.getContext(), text, duration);
           toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
           toast.show();
       }
       else {
           //any other activity including the MainActivity
           psychedDatabase.addFavorite(s);
           CharSequence text = "Sound added to favorites...";
           int duration = Toast.LENGTH_LONG;
           Toast toast = Toast.makeText(v.getContext(), text, duration);
           toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
           toast.show();
       }
   }

   //handles the poppup menu when you long click a View/ a Sound
   public static void poppupLauncher(final View v, final Sound s){

       //finish instantiating the database
       psychedDatabase = new PsychedDatabase(v.getContext());

       //create a poppup just for this specific Sound view
       PopupMenu poppup = new PopupMenu(v.getContext(), v);

       // Identify the current activity and inflate the right popup menu
       if (v.getContext() instanceof FavoriteActivity) {
           //when in the favorite activity
           poppup.getMenuInflater().inflate(R.menu.favorite_longclick, poppup.getMenu());
       }
       else {
           //when in any other activity including the MainActivity
           poppup.getMenuInflater().inflate(R.menu.long_click, poppup.getMenu());
       }

       //set a click listener for the different poppup menu items
       poppup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {

               //if you are sharing the sound or setting the sound as some type of tone on your phone,
               //then the sound needs to be saved onto the device
               if (item.getItemId() == R.id.share || item.getItemId() == R.id.set){

                   //name of the file to be saved
                   final String fileName = s.getName() + ".wav";

                   //get external storage and create my own personal directory for the user.
                   //Named: psyched_sounds/
                   //if the directory doesnt already exist I want to make sure to do that
                   File storage = Environment.getExternalStorageDirectory();
                   File directory = new File(storage.getAbsolutePath() + "/psyched_sounds/");
                   directory.mkdirs();

                   //create a file
                   final File file = new File(directory, fileName);

                   //input stream that is linked to the current sound
                   InputStream in = v.getContext().getResources().openRawResource(s.getId());

                   try{

                       //output stream and buffer size
                       OutputStream out = new FileOutputStream(file);
                       byte[] buffer = new byte[1024];

                       //write it out
                       // if (int) InputStream.read() returns -1 stream is at the end of file
                       int len;
                       while ((len = in.read(buffer, 0, buffer.length)) != -1){
                            out.write(buffer, 0, len);
                       }

                       //Toast informing the user that the sound has actually been saved to the phone
                       CharSequence text = "Sound saved to internal storage...";
                       int duration = Toast.LENGTH_LONG;
                       Toast toast = Toast.makeText(v.getContext(), text, duration);
                       toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
                       toast.show();

                       //close my streams
                       in.close();
                       out.close();

                   }catch (IOException e){

                       Log.e(LOGTAG, "File was not saved correctly: " + e.getMessage());

                   }

                   //only if the the user chose share
                   if (item.getItemId() == R.id.share){

                       //check users phone version, if it is 5.1 or higher
                       // If it is you'll have to use FileProvider to get the sharing function to work properly
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
                           final String AUTHORITY = v.getContext().getPackageName() + ".fileprovider";
                           Uri uri = FileProvider.getUriForFile(v.getContext(), AUTHORITY, file);

                           //create intent
                           final Intent share = new Intent(Intent.ACTION_SEND);

                           share.putExtra(Intent.EXTRA_STREAM, uri);
                           //define the type of intent to be .wav
                           share.setType("audio/wav");
                           // Start a new chooser dialog where the user can choose an app to share the sound
                           v.getContext().startActivity(Intent.createChooser(share, "Share sound with: "));

                       }else{

                           // Uri refers to a name or location
                           // .parse() analyzes a given uri string and creates a Uri from it

                           // Define a "link" (Uri) to the saved file

                           final Intent share = new Intent(Intent.ACTION_SEND);

                           Uri oldUri = Uri.parse(file.getAbsolutePath());

                           share.putExtra(Intent.EXTRA_STREAM, oldUri);
                           //define the type of intent to be .wav
                           share.setType("audio/wav");
                           // Start a new chooser dialog where the user can choose an app to share the sound
                           v.getContext().startActivity(Intent.createChooser(share, "Share sound with: "));


                       }

                   }

                   if (item.getItemId() == R.id.set){

                       AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), AlertDialog.THEME_HOLO_LIGHT);
                       builder.setTitle("Set as…");
                       builder.setItems(new CharSequence[]{"Ringtone", "Notification", "Alarm"}, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {

                               switch (i){

                                   //Ringtone
                                   case 0:
                                       alterSystemSound(v, fileName, file, 1);
                                       break;
                                   //Notification
                                   case 1:
                                       alterSystemSound(v, fileName, file, 2);
                                       break;
                                   //Alarm
                                   case 2:
                                       alterSystemSound(v, fileName, file, 3);
                                       break;

                               }


                           }
                       });
                       builder.create();
                       builder.show();

                   }

               }

               // Add sound to favorites / Remove sound from favorites
               if (item.getItemId() == R.id.favorite){

                   // Identify the current activity
                   if (v.getContext() instanceof FavoriteActivity) {
                       //if inside the FavoriteActivity
                       psychedDatabase.removeFavorite(v.getContext(), s);
                       CharSequence text = "Sound removed from favorites...";
                       int duration = Toast.LENGTH_LONG;
                       Toast toast = Toast.makeText(v.getContext(), text, duration);
                       toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
                       toast.show();
                   }
                   else {
                       //any other activity including the MainActivity
                       psychedDatabase.addFavorite(s);
                       CharSequence text = "Sound added to favorites...";
                       int duration = Toast.LENGTH_LONG;
                       Toast toast = Toast.makeText(v.getContext(), text, duration);
                       toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
                       toast.show();
                   }
               }

               return true;
           }
       });

       poppup.show();
   }


   public static void alterSystemSound(View v, String fileName, File file, int action){

       try{

           // Put all informations about the audio into ContentValues
           ContentValues values = new ContentValues();

           // DATA stores the path to the file on disk
           values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
           //TITLE
           values.put(MediaStore.MediaColumns.TITLE, fileName);
           // MIME_TYPE stores the type of the data send via the MediaProvider
           //all audio file types essentially
           values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");

           switch (action){

               // Ringtone
               case 1:
                   values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                   values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                   values.put(MediaStore.Audio.Media.IS_ALARM, false);
                   break;
               // Notification
               case 2:
                   values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                   values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                   values.put(MediaStore.Audio.Media.IS_ALARM, false);
                   break;
               // Alarm
               case 3:
                   values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                   values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                   values.put(MediaStore.Audio.Media.IS_ALARM, true);
                   break;
           }

           values.put(MediaStore.Audio.Media.IS_MUSIC, false);


           // Define a link(Uri) to the saved file and modify this link a little bit
           // DATA is set by ContenValues and therefore has to be replaced
           Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
           v.getContext().getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + file.getAbsolutePath() + "\"", null);
           // Fill the Uri with all the information from ContentValues
           Uri finalUri = v.getContext().getContentResolver().insert(uri, values);

           // Finally set the audio as one of the system audio types
           switch (action){

               // Ringtone
               case 1:
                   RingtoneManager.setActualDefaultRingtoneUri(v.getContext(), RingtoneManager.TYPE_RINGTONE, finalUri);
                   break;
               // Notification
               case 2:
                   RingtoneManager.setActualDefaultRingtoneUri(v.getContext(), RingtoneManager.TYPE_NOTIFICATION, finalUri);
                   break;
               // Alarm
               case 3:
                   RingtoneManager.setActualDefaultRingtoneUri(v.getContext(), RingtoneManager.TYPE_ALARM, finalUri);
                   break;
           }



       }catch(Exception e){

           Log.e(LOGTAG, "System sound failed to be altered: " + e.getMessage());

       }

   }



}
