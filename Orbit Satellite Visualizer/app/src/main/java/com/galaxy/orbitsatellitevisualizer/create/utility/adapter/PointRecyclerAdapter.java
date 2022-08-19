package com.galaxy.orbitsatellitevisualizer.create.utility.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galaxy.orbitsatellitevisualizer.create.utility.model.shape.Point;
import com.galaxy.orbitsatellitevisualizer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class in charge of the adapter of the point recyclerview of the class CreateStoryBoardShapeActivity
 */
public class PointRecyclerAdapter extends RecyclerView.Adapter<PointRecyclerAdapter.ViewHolder> {

    private static final String TAG_DEBUG = "PointRecyclerAdapter";

    private List<Point> points;

    public PointRecyclerAdapter(List<Point> points) {
        this.points = points;
    }

    public void addPoint(Point point){
        points.add(point);
        notifyItemInserted(points.size() - 1);
    }

    public void deleteLastPoint(){
        points.remove(points.size() - 1);
        notifyItemRemoved(points.size());
    }

    public void deleteAllPoints(){
        List<Point> initPoints = points.subList(0, 2);
        points = new ArrayList<>(initPoints);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_create_storyboard_action_balloon_point, parent, false);
        return new ViewHolder(view, new MyLongitudeTextListener(), new MyLatitudeTextListener(), new MyAltitudeTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pointPosition = "Point " + (position + 1);
        holder.textView.setText(pointPosition);

        Point point = points.get(holder.getAdapterPosition());

        holder.myLongitudeTextListener.updatePosition(holder.getAdapterPosition());
        holder.longitude.setText(String.valueOf(point.getLongitude()));

        holder.myLatitudeTextListener.updatePosition(holder.getAdapterPosition());
        holder.latitude.setText(String.valueOf(point.getLatitude()));

        holder.myAltitudeTextListener.updatePosition(holder.getAdapterPosition());
        holder.altitude.setText(String.valueOf(point.getAltitude()));

    }

    @Override
    public int getItemCount() {
        return points.size();
    }


    /**
     * This is the most efficient way to have the view holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        EditText longitude, latitude, altitude;

        MyLongitudeTextListener myLongitudeTextListener;
        MyLatitudeTextListener myLatitudeTextListener;
        MyAltitudeTextListener myAltitudeTextListener;

        ViewHolder(View itemView, MyLongitudeTextListener myLongitudeTextListener,
                   MyLatitudeTextListener myLatitudeTextListener, MyAltitudeTextListener myAltitudeTextListener) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            this.longitude = itemView.findViewById(R.id.longitude);
            this.latitude = itemView.findViewById(R.id.latitude);
            this.altitude = itemView.findViewById(R.id.altitude);

            this.myLongitudeTextListener = myLongitudeTextListener;
            this.myLatitudeTextListener = myLatitudeTextListener;
            this.myAltitudeTextListener = myAltitudeTextListener;

            longitude.addTextChangedListener(myLongitudeTextListener);
            latitude.addTextChangedListener(myLatitudeTextListener);
            altitude.addTextChangedListener(myAltitudeTextListener);

        }

    }

    /**
     * This embedded class is in charge of the knowing the changes in longitude editText of a point
     */
    public class MyLongitudeTextListener implements TextWatcher {
        private int position;

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try {
                Point point = points.get(position);
                point.setLongitude(Double.parseDouble(charSequence.toString()));
                points.set(position, point);
            } catch (Exception e){
                Log.w(TAG_DEBUG, "Empty String");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }

    /**
     * This embedded class is in charge of the knowing the changes in latitude editText of a point
     */
    public class MyLatitudeTextListener implements TextWatcher {
        private int position;

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try{
                Point point = points.get(position);
                point.setLatitude(Double.parseDouble(charSequence.toString()));
                points.set(position, point);
            } catch (Exception e){
                Log.w(TAG_DEBUG, "Empty String");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }

    /**
     * This embedded class is in charge of the knowing the changes in altitude editText of a point
     */
    public class MyAltitudeTextListener implements TextWatcher {
        private int position;

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try {
                Point point = points.get(position);
                point.setAltitude(Double.parseDouble(charSequence.toString()));
                points.set(position, point);
            } catch (Exception e){
                Log.w(TAG_DEBUG, "Empty String");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }

}
