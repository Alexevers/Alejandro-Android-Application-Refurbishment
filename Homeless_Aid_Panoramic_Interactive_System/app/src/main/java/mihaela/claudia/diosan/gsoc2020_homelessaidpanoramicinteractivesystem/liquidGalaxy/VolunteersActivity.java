package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.adapters.LgUserAdapter;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.GetSessionTask;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.VisitPoiTask;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils.LgUser;

public class VolunteersActivity extends AppCompatActivity {


    /*Firebase*/
    private FirebaseFirestore mFirestore;

    /*SearchView*/
    private SearchView searchView;

    SharedPreferences preferences, defaultPrefs;
    TextView city_tv, country_tv,from_tv, test_statistics;
    ImageView goHome;
    String city, country;

    private Map<String,String> volunteerInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lg_users_list);

        initViews();
        mFirestore = FirebaseFirestore.getInstance();
        preferences = this.getSharedPreferences("cityInfo", MODE_PRIVATE);
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        GetSessionTask getSessionTask = new GetSessionTask(this);
        getSessionTask.execute();

        setActualLocation();
        setRecyclerView();

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VolunteersActivity.this, MainActivityLG.class));
            }
        });
    }

    private void initViews(){
        searchView =findViewById(R.id.lg_users_search);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        city_tv = findViewById(R.id.city_text_users);
        country_tv = findViewById(R.id.country_text_users);
        goHome = findViewById(R.id.go_home_iv_users);
        from_tv = findViewById(R.id.city_text_tv);

    }

    private void setActualLocation(){

        preferences = this.getSharedPreferences("cityInfo", MODE_PRIVATE);
         city = preferences.getString("city","");
         country = preferences.getString("country","");

        city_tv.setText(city);
        country_tv.setText(country);
        from_tv.setText(getString(R.string.volunteers_from));
    }

    private void setRecyclerView(){
        RecyclerView.LayoutManager mLayoutManager;
        final RecyclerView recyclerView = findViewById(R.id.recycler_view_users_lg);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mLayoutManager = new GridLayoutManager(this, 2);
        }else {
            mLayoutManager = new GridLayoutManager(this, 4);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        final List<LgUser> users = new ArrayList<>();

        String city = preferences.getString("city","");


        mFirestore.collection("volunteers").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String username = document.getString("username");
                                final String latitude = document.getString("latitude");
                                final String longitude = document.getString("longitude");
                                final String phone = document.getString("phone");
                                final String email = document.getString("email");
                                final String firstName = document.getString("firstName");
                                final String lastName = document.getString("lastName");
                                final String location = document.getString("address");
                                final String homelessCreated = document.getString("homelessCreated");
                                final int color = getColor(R.color.white);


                                final LgUser user = new LgUser(username, latitude, longitude, location, email, phone, firstName, lastName, homelessCreated, color);
                                users.add(user);

                                final LgUserAdapter lgUserAdapter = new LgUserAdapter(users);
                                searchText(lgUserAdapter);
                                recyclerView.setAdapter(lgUserAdapter);

                                lgUserAdapter.setOnItemClickListener(new LgUserAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {

                                        String description = description(users.get(position).getEmail(), users.get(position).getLocation());
                                        POIController.cleanKmls();
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        POIController.getInstance().moveToPOI(userPoi, null);

                                        POIController.getInstance().showPlacemark(userPoi,null, "https://i.ibb.co/xf1S6cn/volunteer-icon.png", "placemarks/volunteers");
                                        POIController.getInstance().showBalloon(userPoi, null, description,"volunteer", "balloons/basic/volunteers");
                                        POIController.getInstance().sendBalloon(userPoi, null, "balloons/basic/volunteers");

                                        Toast.makeText(VolunteersActivity.this, "Showing basic info of" + " " + users.get(position).getUsername() + " " + "on Liquid Galaxy" , Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onBioClick(int position) {

                                        POIController.cleanKmls();
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        POIController.getInstance().moveToPOI(userPoi, null);

                                        POIController.getInstance().showPlacemark(userPoi,null, "https://i.ibb.co/xf1S6cn/volunteer-icon.png", "placemarks/volunteers");
                                        POIController.getInstance().showBalloon(userPoi, null, buildBio(users.get(position).getFirstName(), users.get(position).getLastName(), users.get(position).getPhone(), users.get(position).getEmail(), users.get(position).getLocation()), "volunteer", "balloons/bio/volunteers");
                                        POIController.getInstance().sendBalloon(userPoi, null, "balloons/bio/volunteers");

                                        Toast.makeText(VolunteersActivity.this, "Showing Extra Info of" + " " + users.get(position).getUsername() + " " + "on Liquid Galaxy" , Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onTransactionClick(int position) {

                                        POIController.cleanKmls();
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        POIController.getInstance().moveToPOI(userPoi, null);

                                        POIController.getInstance().showPlacemark(userPoi,null, "https://i.ibb.co/xf1S6cn/volunteer-icon.png", "placemarks/volunteers");
                                        POIController.getInstance().showBalloon(userPoi, null, buildTransactions(users.get(position).getFirstName(), users.get(position).getLastName(), users.get(position).getPhone(), users.get(position).getEmail(), users.get(position).getLocation(), users.get(position).getHomelessCreated()),"volunteer", "balloons/transactions/volunteers");
                                        POIController.getInstance().sendBalloon(userPoi, null, "balloons/transactions/volunteers");

                                        Toast.makeText(VolunteersActivity.this, "Showing Transactions for" + " " + users.get(position).getUsername() + " " + "on Liquid Galaxy" , Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onOrbitClick(int position) {
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        String command = buildCommand(userPoi);
                                        VisitPoiTask visitPoiTask = new VisitPoiTask(command, userPoi, true,VolunteersActivity.this, VolunteersActivity.this);
                                        visitPoiTask.execute();

                                    }
                                });
                            }
                        }
                    }
                });
    }

    private POI createPOI(String name, String latitude, String longitude){

        POI poi = new POI()
                .setName(name)
                .setLongitude(Double.parseDouble(longitude))
                .setLatitude(Double.parseDouble(latitude))
                .setAltitude(0.0d)
                .setHeading(0.0d)
                .setTilt(60.0d)
                .setRange(300.0d)
                .setAltitudeMode("relativeToSeaFloor");

        return poi;
    }

    private String buildCommand(POI poi) {
        return "echo 'flytoview=<gx:duration>3</gx:duration><gx:flyToMode>smooth</gx:flyToMode><LookAt><longitude>" + poi.getLongitude() + "</longitude>" +
                "<latitude>" + poi.getLatitude() + "</latitude>" +
                "<altitude>" + poi.getAltitude() + "</altitude>" +
                "<heading>" + poi.getHeading() + "</heading>" +
                "<tilt>" + poi.getTilt() + "</tilt>" +
                "<range>" + poi.getRange() + "</range>" +
                "<gx:altitudeMode>" + poi.getAltitudeMode() + "</gx:altitudeMode>" +
                "</LookAt>' > /tmp/query.txt";
    }


    private void searchText(final LgUserAdapter lgUserAdapter){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                lgUserAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    private String description(String email, String location){
        return  "<h2> <b> Basic Info</b></h2>\n" +
                "<p> <b> Email: </b> " + email + "</p>\n" +
                "<p> <b> Location: </b> " + location + "</p>\n" ;
    }

    private String buildBio(String firstName, String lastName, String phone, String email, String location){
        return  "<h2> <b> Basic Info</b></h2>\n" +
                "<p> <b> Email: </b> " + email + "</p>\n" +
                "<p> <b> Location: </b> " + location + "</p>\n" +
                "<h2> <b> Extra Info</b></h2>\n" +
                "<p> <b> First Name: </b> " + firstName + "</p>\n" +
                "<p> <b> Last Name: </b> " + lastName + "</p>\n" +
                "<p> <b> Phone Number: </b> " + phone + "</p>\n" ;
    }

    private String buildTransactions(String firstName, String lastName, String phone, String email, String location, String createdHomeless){

        return  "<h2> <b> Basic Info</b></h2>\n" +
                "<p> <b> Email: </b> " + email + "</p>\n" +
                "<p> <b> Location: </b> " + location + "</p>\n" +
                "<h2> <b> Extra Info</b></h2>\n" +
                "<p> <b> First Name: </b> " + firstName + "</p>\n" +
                "<p> <b> Last Name: </b> " + lastName + "</p>\n" +
                "<p> <b> Phone Number: </b> " + phone + "</p>\n" +
                "<h2><b> Transactions </b> </h2>\n" +
                "<p><b> Created homeless profiles: </b> " +  createdHomeless + "</p>\n";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flyToCity(city);
        startActivity(new Intent(VolunteersActivity.this, CityActivity.class));

    }

    private void flyToCity(String city){
        mFirestore.collection("cities").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String city = document.getString("city");
                                final String latitude = document.getString("latitude");
                                final String longitude = document.getString("longitude");

                                POIController.cleanKmls();
                                POI cityPoi = createPOI(city, latitude, longitude);
                                POIController.getInstance().moveToPOI(cityPoi,null);

                            }
                        }
                    }
                });
    }

}