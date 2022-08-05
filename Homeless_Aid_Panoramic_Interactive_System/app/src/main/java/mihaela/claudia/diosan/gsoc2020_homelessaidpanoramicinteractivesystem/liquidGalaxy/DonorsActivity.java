package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.SetOptions;

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

public class DonorsActivity extends AppCompatActivity {

    /*Firebase*/
    private FirebaseFirestore mFirestore;

    /*SearchView*/
    private SearchView searchView;

    SharedPreferences preferences;
    TextView city_tv, country_tv, from_tv;

    ImageView goHome;

    private Map<String,String> donorInfo = new HashMap<>();
    String city, country;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lg_users_list);

        initViews();
        mFirestore = FirebaseFirestore.getInstance();
        preferences = this.getSharedPreferences("cityInfo", MODE_PRIVATE);


        GetSessionTask getSessionTask = new GetSessionTask(this);
        getSessionTask.execute();

        setActualLocation();
        setRecyclerView();

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DonorsActivity.this, MainActivityLG.class));
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
        from_tv.setText(getString(R.string.donors_from));
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


        mFirestore.collection("donors").whereEqualTo("city", city)
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
                                final String personallyDonations = document.getString("personallyDonation");
                                final String throughVolunteerDonations = document.getString("throughVolunteerDonations");

                                final int color = getColor(R.color.white);

                                final LgUser user = new LgUser(username,color, latitude, longitude, location, email, phone, firstName, lastName, personallyDonations, throughVolunteerDonations);
                                users.add(user);


                                final LgUserAdapter lgUserAdapter = new LgUserAdapter(users);
                                searchText(lgUserAdapter);
                                recyclerView.setAdapter(lgUserAdapter);

                                lgUserAdapter.setOnItemClickListener(new LgUserAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {

                                        personallyTransactions(users.get(position).getEmail());
                                        throughVolunteerTransactions(users.get(position).getEmail());
                                        String description = description(users.get(position).getEmail(), users.get(position).getLocation());
                                        POIController.cleanKmls();
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        POIController.getInstance().moveToPOI(userPoi, null);

                                        POIController.getInstance().showPlacemark(userPoi,null, "https://i.ibb.co/Bg4Lnvk/donor-icon.png", "placemarks/donors");
                                        POIController.getInstance().showBalloon(userPoi, null, description,"donor", "balloons/basic/donors");
                                        POIController.getInstance().sendBalloon(userPoi, null, "balloons/basic/donors");

                                        Toast.makeText(DonorsActivity.this, "Showing basic info of" + " " + users.get(position).getUsername() + " " + "on Liquid Galaxy" , Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onBioClick(int position) {
                                        personallyTransactions(users.get(position).getEmail());
                                        throughVolunteerTransactions(users.get(position).getEmail());

                                        POIController.cleanKmls();
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        POIController.getInstance().moveToPOI(userPoi, null);

                                        POIController.getInstance().showPlacemark(userPoi,null, "https://i.ibb.co/Bg4Lnvk/donor-icon.png", "placemarks/donors");
                                        POIController.getInstance().showBalloon(userPoi, null, buildBio(users.get(position).getFirstName(), users.get(position).getLastName(), users.get(position).getPhone(), users.get(position).getEmail(), users.get(position).getLocation()), "donor", "balloons/bio/donors");
                                        POIController.getInstance().sendBalloon(userPoi, null, "balloons/bio/donors");
                                        Toast.makeText(DonorsActivity.this, "Showing Extra Info of" + " " + users.get(position).getUsername() + " " + "on Liquid Galaxy" , Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onTransactionClick(int position) {
                                        personallyTransactions(users.get(position).getEmail());
                                        throughVolunteerTransactions(users.get(position).getEmail());


                                        POIController.cleanKmls();
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        POIController.getInstance().moveToPOI(userPoi, null);

                                        POIController.getInstance().showPlacemark(userPoi,null, "https://i.ibb.co/Bg4Lnvk/donor-icon.png", "placemarks/donors");
                                        POIController.getInstance().showBalloon(userPoi, null, buildTransactions(users.get(position).getFirstName(), users.get(position).getLastName(), users.get(position).getPhone(), users.get(position).getEmail(), users.get(position).getLocation(), users.get(position).getPersonallyDonations(), users.get(position).getThroughVolunteerDonation()),"donor", "balloons/transactions/donors");
                                        POIController.getInstance().sendBalloon(userPoi, null, "balloons/transactions/donors");
                                        Toast.makeText(DonorsActivity.this, "Showing Transactions for" + " " + users.get(position).getUsername() + " " + "on Liquid Galaxy" , Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onOrbitClick(int position) {
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        String command = buildCommand(userPoi);
                                        VisitPoiTask visitPoiTask = new VisitPoiTask(command, userPoi, true,DonorsActivity.this, DonorsActivity.this);
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
        return   "<h2> <b> Basic Info</b></h2>\n" +
                "<p> <b> Email: </b> " + email + "</p>\n" +
                "<p> <b> Location: </b> " + location + "</p>\n" +
                "<h2> <b> Extra Info</b></h2>\n" +
                "<p> <b> First Name: </b> " + firstName + "</p>\n" +
                "<p> <b> Last Name: </b> " + lastName + "</p>\n" +
                "<p> <b> Phone Number: </b> " + phone + "</p>\n" ;
    }

    private String buildTransactions(String firstName, String lastName, String phone, String email, String location, String personallyDonations, String throughVolunteerDonations){

        return  "<h2> <b> Basic Info</b></h2>\n" +
                "<p> <b> Email: </b> " + email + "</p>\n" +
                "<p> <b> Location: </b> " + location + "</p>\n" +
                "<h2> <b> Extra Info</b></h2>\n" +
                "<p> <b> First Name: </b> " + firstName + "</p>\n" +
                "<p> <b> Last Name: </b> " + lastName + "</p>\n" +
                "<p> <b> Phone Number: </b> " + phone + "</p>\n" +
                "<h2><b> Transactions </b> </h2>\n" +
                "<p><b> Personally Donations: </b> " + personallyDonations + "</p>\n" +
                "<p><b> Through Volunteer Donations: </b> " + throughVolunteerDonations + "</p>\n";
    }

    private void personallyTransactions(String email){

        mFirestore.collection("personallyDonations").whereEqualTo("donorEmail",email )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String personallyDonation = String.valueOf(task.getResult().size());
                            donorInfo.put("personallyDonation", personallyDonation);
                            mFirestore.collection("donors").document(email).set(donorInfo, SetOptions.merge());

                        }
                    }
                });
    }


    private void throughVolunteerTransactions(String email){

        mFirestore.collection("throughVolunteerDonations").whereEqualTo("donorEmail",email )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String throughVolunteerDonations = String.valueOf(task.getResult().size());
                            donorInfo.put("throughVolunteerDonations", throughVolunteerDonations);
                            mFirestore.collection("donors").document(email).set(donorInfo, SetOptions.merge());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flyToCity(city);
        startActivity(new Intent(DonorsActivity.this, CityActivity.class));

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