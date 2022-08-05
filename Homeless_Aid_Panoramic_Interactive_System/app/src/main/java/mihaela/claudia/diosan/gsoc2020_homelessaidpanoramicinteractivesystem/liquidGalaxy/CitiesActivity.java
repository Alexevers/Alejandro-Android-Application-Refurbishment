package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.adapters.CitiesCardsAdapter;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils.Cities;

import static mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand.CRITICAL_MESSAGE;


public class CitiesActivity extends AppCompatActivity {

    /*Firebase*/
    private FirebaseFirestore mFirestore;
    /*SearchView*/
    private SearchView searchView;

    SharedPreferences preferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        initViews();
        mFirestore = FirebaseFirestore.getInstance();
        preferences = this.getSharedPreferences("cityInfo", MODE_PRIVATE);

        setRecyclerView();
    }

    private void initViews(){
        searchView =findViewById(R.id.city_search);
        searchView.onActionViewExpanded();
        searchView.clearFocus();

    }

    

    private void setRecyclerView(){
        RecyclerView.LayoutManager mLayoutManager ;
        final RecyclerView recyclerView = findViewById(R.id.recycler_view_cities);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mLayoutManager = new GridLayoutManager(this, 2);
        }else {
            mLayoutManager = new GridLayoutManager(this, 4);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        final List<Cities> cities = new ArrayList<>();


        mFirestore.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String city = document.getString("city");
                                final String country = document.getString("country");
                                final String image = document.getString("image");
                                final String latitude = document.getString("latitude");
                                final String longitude = document.getString("longitude");
                                final String altitude = document.getString("altitude");

                                final Cities oneCity = new Cities(city, country, image, latitude, longitude, altitude);
                                cities.add(oneCity);


                                final CitiesCardsAdapter citiesCardsAdapter = new CitiesCardsAdapter(cities);
                                searchText(citiesCardsAdapter);
                                recyclerView.setAdapter(citiesCardsAdapter);

                                citiesCardsAdapter.setOnItemClickListener(new CitiesCardsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                       SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("city",  cities.get(position).getCity()).apply();
                                        editor.putString("country", cities.get(position).getCountry()).apply();
                                        editor.apply();
                                        POIController.cleanKmls();
                                        POI cityPoi = createPOI(cities.get(position).getLatitude(), cities.get(position).getLongitude(), cities.get(position).getAltitude());
                                        POIController.getInstance().moveToPOI(cityPoi,null);
                                        startActivity(new Intent(CitiesActivity.this, CityActivity.class));

                                    }
                                });

                            }
                        }
                    }
                });

    }


    private POI createPOI(String latitude, String longitude, String altitude){

        POI poi = new POI()
                .setLongitude(Double.parseDouble(longitude))
                .setLatitude(Double.parseDouble(latitude))
                .setAltitude(Double.parseDouble(altitude))
                .setHeading(0.0d)
                .setTilt(40.0d)
                .setRange(800.0d)
                .setAltitudeMode("relativeToSeaFloor");

        return poi;
    }


    private void searchText(final CitiesCardsAdapter citiesCardsAdapter){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                citiesCardsAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }






}