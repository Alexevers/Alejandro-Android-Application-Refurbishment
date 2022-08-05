package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.maps;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

public class ListMapFragment extends Fragment  {

    private View view;

    private RecyclerView mRecyclerView;

    private FirebaseFirestore mFirestore;

    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_map, container, false);

        searchView = view.findViewById(R.id.list_map_search);
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        mFirestore = FirebaseFirestore.getInstance();

        setHasOptionsMenu(true);
        setupRecyclerView(view);


        return view;

    }


    private void setupRecyclerView(View view){

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(view.getContext());

        mRecyclerView = view.findViewById(R.id.recycler_view_list_map);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        final List<NamedLocation> LIST_LOCATIONS = new ArrayList<>();

        mFirestore.collection("homeless")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                String latitude = document.getString("homelessLatitude");
                                String longitude = document.getString("homelessLongitude");
                                final String address = document.getString("homelessAddress");
                                final String username = document.getString("homelessUsername");

                                final LatLng position = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                                final NamedLocation namedLocation = new NamedLocation(username, position, address);
                                LIST_LOCATIONS.add(namedLocation);
                                final MapAdapter mapAdapter = new MapAdapter(LIST_LOCATIONS);
                                searchText(mapAdapter);
                                mRecyclerView.setAdapter(mapAdapter);

                            }
                        }
                    }
                });

        mRecyclerView.setRecyclerListener(mRecycleListener);

    }


    private void searchText(final MapAdapter mapAdapter){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mapAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    private class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> implements Filterable {

        List<NamedLocation> namedLocations;
        List<NamedLocation> namedLocationsAll;


        private MapAdapter(List<NamedLocation> namedLocations) {
            super();
            this.namedLocations = namedLocations;
            this.namedLocationsAll = new ArrayList<>(namedLocations);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_list_map_row, parent, false));
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder == null) {
                return;
            }
            holder.bindView(position);
        }

        @Override
        public int getItemCount() {
            return namedLocations.size();
        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<NamedLocation> filteredList = new ArrayList<>();
                if (constraint.toString().isEmpty()){
                    filteredList.addAll(namedLocationsAll);
                }else{
                    for (NamedLocation namedLocation : namedLocationsAll){
                        if (namedLocation.getName().toLowerCase().contains(constraint.toString().trim()) || namedLocation.getAddress().toLowerCase().contains(constraint.toString().trim())){
                            filteredList.add(namedLocation);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                namedLocations.clear();
                namedLocations.addAll((Collection<? extends NamedLocation>) results.values);
                notifyDataSetChanged();
            }
        };


        class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

            MapView mapView;
            TextView title;
            TextView address;
            GoogleMap map;
            View layout;

            private ViewHolder(View itemView) {
                super(itemView);
                layout = itemView;
                mapView = layout.findViewById(R.id.lite_list_row_map);
                title = layout.findViewById(R.id.lite_list_row_text);
                address = layout.findViewById(R.id.lite_list_row_address);
                if (mapView != null) {
                    // Initialise the MapView
                    mapView.onCreate(null);
                    mapView.onResume();
                    // Set the map ready callback to receive the GoogleMap object
                    mapView.getMapAsync(this);
                    mapView.onPause();




                }
            }

            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(view.getContext());
                map = googleMap;
                map.animateCamera(CameraUpdateFactory.zoomTo(13),  2000, null);
                setMapLocation();

            }


            private void setMapLocation() {
                if (map == null) return;

                NamedLocation data = (NamedLocation) mapView.getTag();
                if (data == null) return;

                // Add a marker for this item and set the camera
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(data.location, 13f));
                map.addMarker(new MarkerOptions().position(data.location));

                // Set the map type back to normal.
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }

            private void bindView(int pos) {
                NamedLocation item = namedLocations.get(pos);
                // Store a reference of the ViewHolder object in the layout.
                layout.setTag(this);
                // Store a reference to the item in the mapView's tag. We use it to get the
                // coordinate of a location, when setting the map location.
                mapView.setTag(item);
                setMapLocation();
                title.setText(item.name);
                address.setText(item.getAddress());
            }
        }
    }


    private RecyclerView.RecyclerListener mRecycleListener = new RecyclerView.RecyclerListener() {

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            MapAdapter.ViewHolder mapHolder = (MapAdapter.ViewHolder) holder;
            if (mapHolder != null && mapHolder.map != null) {
                // Clear the map and free up resources by changing the map type to none.
                // Also reset the map when it gets reattached to layout, so the previous map would
                // not be displayed.
                mapHolder.map.clear();
                mapHolder.map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        }
    };


    private static class NamedLocation {

        public final String name;
        public final LatLng location;
        public final String address;

        NamedLocation(String name, LatLng location, String address) {
            this.name = name;
            this.location = location;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public LatLng getLocation() {
            return location;
        }
    }

}
