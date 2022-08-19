package com.galaxy.orbitsatellitevisualizer.create.utility.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.galaxy.orbitsatellitevisualizer.create.utility.model.Action;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.ActionIdentifier;
import com.galaxy.orbitsatellitevisualizer.R;

import java.util.List;

/**
 * This is the class in charge of the adapter of the action recyclerview of the class CreateStoryBoardActivity
 */
public class ActionRecyclerAdapter extends RecyclerView.Adapter<ActionRecyclerAdapter.ViewHolder> {

    private static final String TAG_DEBUG = "ActionRecyclerAdapter";


    private AppCompatActivity activity;
    private List<Action> actions;
    private OnNoteListener mOnNoteListener;

    public ActionRecyclerAdapter(AppCompatActivity activity, List<Action> actions, OnNoteListener onNoteListener) {
        this.activity = activity;
        this.actions = actions;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_create_storyboard_action_icon, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.w(TAG_DEBUG, "onBindViewHolder called");
        Action currentItem = actions.get(position);
        String number = "#" + (position + 1);
        holder.textView.setText(number);
        int type = currentItem.getType();
        if(type == ActionIdentifier.LOCATION_ACTIVITY.getId()){
            holder.imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_icon_action_location));
        }else if(type == ActionIdentifier.MOVEMENT_ACTIVITY.getId()){
            holder.imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_icon_action_movements));
        }else if(type == ActionIdentifier.BALLOON_ACTIVITY.getId()){
            holder.imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_icon_action_graphic));
        }else if(type == ActionIdentifier.SHAPES_ACTIVITY.getId()){
            holder.imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_icon_action_shape));
        }else {
            Log.w(TAG_DEBUG, "ERROR IN TYPE");
        }
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }


    /**
     * This is the most efficient way to have the view holder and the click listener
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;
        OnNoteListener mOnNoteListener;

        ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            //this.imageView = itemView.findViewById(R.id.imageView);
            //this.textView = itemView.findViewById(R.id.textView);
            this.mOnNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.w(TAG_DEBUG, "onClick: " + getAdapterPosition());
            mOnNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
