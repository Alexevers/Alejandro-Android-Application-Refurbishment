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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.adapters.CitiesCardsAdapter;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.adapters.LgUserAdapter;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.GetSessionTask;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.VisitPoiTask;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils.Cities;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils.LgUser;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.logic.Homeless;


public class HomelessActivity extends AppCompatActivity {

    /*Firebase*/
    private FirebaseFirestore mFirestore;

    /*SearchView*/
    private SearchView searchView;

    SharedPreferences preferences;
    SharedPreferences defaultPrefs;
    TextView city_tv, country_tv, from_tv;
    ImageView goHome;
    private Map<String, String> homelessInfo = new HashMap<>();
    //  private Session session;

    String city;
    String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lg_users_list);

        initViews();
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        preferences = this.getSharedPreferences("cityInfo", MODE_PRIVATE);
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        GetSessionTask getSessionTask = new GetSessionTask(this);
        getSessionTask.execute();

        setActualLocation();
        setRecyclerView();

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomelessActivity.this, MainActivityLG.class));
            }
        });
    }

    private void initViews() {
        searchView = findViewById(R.id.lg_users_search);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        city_tv = findViewById(R.id.city_text_users);
        country_tv = findViewById(R.id.country_text_users);
        goHome = findViewById(R.id.go_home_iv_users);
        from_tv = findViewById(R.id.city_text_tv);

    }

    private void setActualLocation() {

        preferences = this.getSharedPreferences("cityInfo", MODE_PRIVATE);
        city = preferences.getString("city", "");
        country = preferences.getString("country", "");

        city_tv.setText(city);
        country_tv.setText(country);
        from_tv.setText(getString(R.string.homeless_from));
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager;
        final RecyclerView recyclerView = findViewById(R.id.recycler_view_users_lg);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(this, 2);
        } else {
            mLayoutManager = new GridLayoutManager(this, 4);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        final List<LgUser> users = new ArrayList<>();

        String city = preferences.getString("city", "");


        mFirestore.collection("homeless").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String username = document.getString("homelessUsername");
                                final String latitude = document.getString("homelessLatitude");
                                final String longitude = document.getString("homelessLongitude");
                                final String birthday = document.getString("homelessBirthday");
                                final String location = document.getString("homelessAddress");
                                final String schedule = document.getString("homelessSchedule");
                                final String need = document.getString("homelessNeed");
                                final String lifeHistory = document.getString("homelessLifeHistory");
                                final String personallyDonations = document.getString("personallyDonations");
                                final String throughVolunteerDonations = document.getString("throughVolunteerDonations");
                                final String image = document.getString("image");

                                final int color = getColor(R.color.white);

                                final LgUser user = new LgUser(username, color, latitude, longitude, image, birthday, location, schedule, need, lifeHistory, personallyDonations, throughVolunteerDonations);
                                users.add(user);


                                final LgUserAdapter lgUserAdapter = new LgUserAdapter(users);
                                searchText(lgUserAdapter);
                                recyclerView.setAdapter(lgUserAdapter);

                                lgUserAdapter.setOnItemClickListener(new LgUserAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) throws IOException {
                                        personallyTransactions(users.get(position).getUsername());
                                        throughVolunteerTransactions(users.get(position).getUsername());

                                        String description = description(users.get(position).getBirthday(), users.get(position).getLocation(), users.get(position).getSchedule(), users.get(position).getNeed());
                                        POIController.cleanKmls();
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        POIController.getInstance().moveToPOI(userPoi, null);

                                        POIController.downloadProfilePhoto(userPoi.getName(), users.get(position).getImage());
                                        POIController.getInstance().showPlacemark(userPoi, null, "https://i.ibb.co/1nsNbxr/homeless-icon.png", "placemarks/homeless");
                                        POIController.getInstance().showBalloon(userPoi, null, description, users.get(position).getUsername(), "balloons/basic/homeless");
                                        POIController.getInstance().sendBalloon(userPoi, null, "balloons/basic/homeless");
                                        Toast.makeText(HomelessActivity.this, "Showing basic info of" + " " +  users.get(position).getUsername()  + " " +"on Liquid Galaxy" , Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onBioClick(int position) {
                                        personallyTransactions(users.get(position).getUsername());
                                        throughVolunteerTransactions(users.get(position).getUsername());

                                        POIController.cleanKmls();
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        POIController.getInstance().moveToPOI(userPoi, null);

                                        //  POIController.getInstance().sendPlacemark(userPoi, null, defaultPrefs.getString("SSH-IP", "192.168.1.76"), "balloons/homeless");

                                        POIController.getInstance().showPlacemark(userPoi, null, "https://i.ibb.co/1nsNbxr/homeless-icon.png", "placemarks/homeless");
                                        POIController.getInstance().showBalloon(userPoi, null, buildBio(users.get(position).getLifeHistory(), users.get(position).getBirthday(), users.get(position).getLocation(), users.get(position).getSchedule(), users.get(position).getNeed()), users.get(position).getUsername(), "balloons/bio/homeless");
                                        POIController.getInstance().sendBalloon(userPoi, null, "balloons/bio/homeless");
                                        Toast.makeText(HomelessActivity.this, "Showing Life History for" + " " + users.get(position).getUsername() + " " + "on Liquid Galaxy" , Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onTransactionClick(int position) {
                                        personallyTransactions(users.get(position).getUsername());
                                        throughVolunteerTransactions(users.get(position).getUsername());


                                        POIController.cleanKmls();
                                        personallyTransactions(users.get(position).getUsername());
                                        throughVolunteerTransactions(users.get(position).getUsername());

                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        POIController.getInstance().moveToPOI(userPoi, null);

                                        //  POIController.getInstance().sendPlacemark(userPoi, null, defaultPrefs.getString("SSH-IP", "192.168.1.76"), "balloons/homeless");

                                        POIController.getInstance().showPlacemark(userPoi, null, "https://i.ibb.co/1nsNbxr/homeless-icon.png", "placemarks/homeless");
                                        POIController.getInstance().showBalloon(userPoi, null, buildTransactions(users.get(position).getLifeHistory(), users.get(position).getBirthday(), users.get(position).getLocation(), users.get(position).getSchedule(), users.get(position).getNeed(), users.get(position).getPersonallyDonations(), users.get(position).getThroughVolunteerDonation()), users.get(position).getUsername(), "balloons/transactions/homeless");
                                        POIController.getInstance().sendBalloon(userPoi, null, "balloons/transactions/homeless");

                                        Toast.makeText(HomelessActivity.this, "Showing Transactions for" + " " + users.get(position).getUsername() + " " + "on Liquid Galaxy" , Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onOrbitClick(int position) {
                                        POI userPoi = createPOI(users.get(position).getUsername(), users.get(position).getLatitude(), users.get(position).getLongitude());
                                        String command = buildCommand(userPoi);
                                        VisitPoiTask visitPoiTask = new VisitPoiTask(command, userPoi, true, HomelessActivity.this, HomelessActivity.this);
                                        visitPoiTask.execute();
                                    }
                                });

                            }
                        }
                    }
                });
    }


    private POI createPOI(String name, String latitude, String longitude) {

        POI poi = new POI()
                .setName(name)
                .setLongitude(Double.parseDouble(longitude))
                .setLatitude(Double.parseDouble(latitude))
                .setAltitude(0.0d)
                .setHeading(0.0d)
                .setTilt(70.0d)
                .setRange(200.0d)
                .setAltitudeMode("relativeToSeaFloor");

        return poi;
    }


    private void searchText(final LgUserAdapter lgUserAdapter) {
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

    private String description(String birthday, String location, String schedule, String need) {
        return  "<h2> <b> Basic Info</b></h2>\n" +
                "<p> <b> Birthday: </b> " + birthday + "</p>\n" +
                "<p> <b> Location: </b> " + location + "</p>\n" +
                "<p> <b> Schedule: </b> " + schedule + "</p>\n" +
                "<p> <b> Most important need: </b> " + need + "</p>\n";
    }

    private String buildBio(String lifeHistory, String birthday, String location, String schedule, String need) {
        return "<h2> <b> Basic Info</b></h2>\n" +
                "<p> <b> Birthday: </b> " + birthday + "</p>\n" +
                "<p> <b> Location: </b> " + location + "</p>\n" +
                "<p> <b> Schedule: </b> " + schedule + "</p>\n" +
                "<p> <b> Most important need: </b> " + need + "</p>\n" +
                "<h2><b> Life history </b> </h2>\n" +
                "<p> " + lifeHistory + "</p>\n";
    }

    public static String buildTransactions(String lifeHistory, String birthday, String location, String schedule, String need, String personallyDonations, String throughVolunteerDonations) {

        return "<h2> <b> Basic Info</b></h2>\n" +
                "<p> <b> Birthday: </b> " + birthday + "</p>\n" +
                "<p> <b> Location: </b> " + location + "</p>\n" +
                "<p> <b> Schedule: </b> " + schedule + "</p>\n" +
                "<p> <b> Most important need: </b> " + need + "</p>\n" +
                "<h2><b> Life history </b> </h2>\n" +
                "<p> " + lifeHistory + "</p>\n" +
                "<h2><b> Transactions </b> </h2>\n" +
                "<p><b> Personally Donations: </b> " + personallyDonations + "</p>\n" +
                "<p><b> Through Volunteer Donations: </b> " + throughVolunteerDonations + "</p>\n";
    }

    private void personallyTransactions(String homelessUsername) {

        mFirestore.collection("personallyDonations").whereEqualTo("donatesTo", homelessUsername)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String personallyDonations = String.valueOf(task.getResult().size());
                            homelessInfo.put("personallyDonations", personallyDonations);
                            mFirestore.collection("homeless").document(homelessUsername).set(homelessInfo, SetOptions.merge());

                        }
                    }
                });
    }


    private void throughVolunteerTransactions(String homelessUsername) {

        mFirestore.collection("throughVolunteerDonations").whereEqualTo("donatesTo", homelessUsername)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String throughVolunteerDonations = String.valueOf(task.getResult().size());
                            homelessInfo.put("throughVolunteerDonations", throughVolunteerDonations);
                            mFirestore.collection("homeless").document(homelessUsername).set(homelessInfo, SetOptions.merge());

                        }
                    }
                });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flyToCity(city);
        startActivity(new Intent(HomelessActivity.this, CityActivity.class));

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


