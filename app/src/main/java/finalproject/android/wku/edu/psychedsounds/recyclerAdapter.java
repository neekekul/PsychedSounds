package finalproject.android.wku.edu.psychedsounds;
//LUKE KEEN
//5/8/2018
import android.content.ClipData;
import android.content.ClipDescription;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by neeke on 3/14/2018.
 */

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.SoundboardViewholder> {

    //arraylist of sound objects
    private ArrayList<Sound> objects;

    private static final String SOUND_TAG = "sound";

    //constructor
    public recyclerAdapter(ArrayList<Sound> objects){

        this.objects = objects;

    }

    //on creation
    @Override
    public SoundboardViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        //the views are based on the sound vessel

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_vessel, null);
        item.setTag(SOUND_TAG);

        return new SoundboardViewholder(item);
    }

    //bind the views
    @Override
    public void onBindViewHolder(SoundboardViewholder holder, int position) {

        final Sound object = objects.get(position);

        final Integer id = object.getId();

        holder.titles.setText(object.getName());

        //when a view is clicked start the sound
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Event.startAux(view, id);

            }
        });

        //on longclick view the poppup menu is shown instead of the sound being played
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Event.poppupLauncher(view, object);
                //create it from the ImageViews tag
                ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

                String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
                ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                view.startDrag( data,  //data to be dragged
                        shadowBuilder,  //drag shadow
                        view,          //locality
                        0       //no flags
                );

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    //class for containing the actual clickable view
    public class SoundboardViewholder extends RecyclerView.ViewHolder{

        TextView titles;

        public SoundboardViewholder(View itemView) {
            super(itemView);

            titles = (TextView) itemView.findViewById(R.id.buttonLabel);

        }
    }

}
