package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.donor.HomeDonor;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.GetSessionTask;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.VisitPoiTask;

import static mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand.CRITICAL_MESSAGE;

public class CityActivity extends AppCompatActivity implements View.OnClickListener {

    TextView city_tv, country_tv;
    MaterialCardView homeless, donors, volunteers,liveOverview ;
    ImageView goHome;

    SharedPreferences preferences;
    SharedPreferences defaultPrefs;

    /*Firebase*/
    private FirebaseFirestore mFirestore;    private Map<String,String> homelessInfo = new HashMap<>();
    private Map<String,String> volunteerInfo = new HashMap<>();
    String homeless_slave, local_statistics_slave, logos_slave, global_statistics_slave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        homeless_slave = defaultPrefs.getString("homeless_preference","");
        local_statistics_slave = defaultPrefs.getString("local_preference","");
        logos_slave = defaultPrefs.getString("logos_preference","");
        global_statistics_slave = defaultPrefs.getString("global_preference","");

        initViews();
        setActualLocation();
        mFirestore = FirebaseFirestore.getInstance();
        GetSessionTask getSessionTask = new GetSessionTask(this);
        getSessionTask.execute();

        homeless.setOnClickListener(this);
        donors.setOnClickListener(this);
        volunteers.setOnClickListener(this);
        goHome.setOnClickListener(this);
        liveOverview.setOnClickListener(this);

    }

    private void initViews(){
        city_tv = findViewById(R.id.city_text);
        country_tv = findViewById(R.id.country_text);
        homeless = findViewById(R.id.homeless_cv);
        donors = findViewById(R.id.donors_cv);
        volunteers = findViewById(R.id.volunteers_cv);
        goHome = findViewById(R.id.go_home_iv);
        liveOverview = findViewById(R.id.live_overview_cv);

    }

    private void setActualLocation(){

        preferences = this.getSharedPreferences("cityInfo", MODE_PRIVATE);
        String city = preferences.getString("city","");
        String country = preferences.getString("country","");

        city_tv.setText(city);
        country_tv.setText(country);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homeless_cv:
                POIController.cleanKmls();
                showAllHomeless();
                startActivity(new Intent(CityActivity.this, HomelessActivity.class));
                break;
            case R.id.donors_cv:
                POIController.cleanKmls();
                showAllDonors();
                startActivity(new Intent(CityActivity.this, DonorsActivity.class));
                break;
            case R.id.volunteers_cv:
                POIController.cleanKmls();
                showAllVolunteers();
                startActivity(new Intent(CityActivity.this, VolunteersActivity.class));
                break;
            case R.id.go_home_iv:
                startActivity(new Intent(CityActivity.this, MainActivityLG.class));
                break;
            case R.id.live_overview_cv:
                String city = preferences.getString("city","");
                POIController.cleanKmls();
                cleanKmls(homeless_slave, local_statistics_slave, global_statistics_slave);

                showLocalStatistics(city);
                showAllHomeless();
                showHomelessInfo(city);
                liveOverview(city);
                break;
        }
    }


    public static void cleanKmls(String homeless_slave, String local_statistics_slave, String global_statistics_slave){

        POIController.cleanKmls();
        POIController.cleanKmlSlave(homeless_slave);
        POIController.cleanKmlSlave(local_statistics_slave);
        POIController.cleanKmlSlave(global_statistics_slave);


    }

    private void showLocalStatistics(String city){
        mFirestore.collection("cities").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String city = document.getString("city");
                                final String cityWS = document.getString("cityWS");
                                final String latitude = document.getString("latitude");
                                final String longitude = document.getString("longitude");
                                final String altitude = document.getString("altitude");
                                final String homeless = document.getString("homelessNumber");
                                final String donors = document.getString("donorsNumber");
                                final String volunteers = document.getString("volunteersNumber");
                                final String foodSt = document.getString("foodSt");
                                final String clothesSt = document.getString("clothesSt");
                                final String workSt = document.getString("workSt");
                                final String lodgingSt = document.getString("lodgingSt");
                                final String hygieneSt = document.getString("hygieneSt");
                                final String image = document.getString("image");


                                POI cityPOI = createPOI(cityWS, latitude, longitude, altitude);
                                String sentence = "cd /var/www/html/hapis/balloons/statistics/cities/ ;curl -o " + cityPOI.getName() + " " + image;
                                LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, CRITICAL_MESSAGE, null));
                                POIController.getInstance().showBalloonOnSlave(cityPOI, null, MainActivityLG.buildCityStatistics(city, homeless, donors, volunteers, foodSt, clothesSt, workSt, lodgingSt, hygieneSt), "http://lg1:81/hapis/balloons/statistics/cities/", cityPOI.getName(), local_statistics_slave);

                            }
                        }
                    }
                });
    }



    private void liveOverview(String city){
        mFirestore.collection("cities").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String city = document.getString("city");
                                final String cityWS = document.getString("cityWS");
                                final String latitude = document.getString("latitude");
                                final String longitude = document.getString("longitude");
                                final String altitude = document.getString("altitude");
                                final String image = document.getString("image");

                                POI cityPOI = createPOI(cityWS, latitude, longitude, altitude);

                                String command = buildCommand(cityPOI);
                                VisitPoiTask visitPoiTask = new VisitPoiTask(command,cityPOI, true, CityActivity.this, CityActivity.this);
                                visitPoiTask.execute();
                            }
                        }
                    }
                });
    }


    private void showHomelessInfo(String city){
        mFirestore.collection("homeless").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String username = document.getString("homelessUsername");

                                personallyTransactions(username);
                                throughVolunteerTransactions(username);


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

                                POI userPoi = createPOI(username, latitude, longitude,"0.0d");
                                POIController.downloadProfilePhoto(userPoi.getName(), image);
                                POIController.getInstance().showBalloonOnSlave(userPoi, null, HomelessActivity.buildTransactions(lifeHistory,birthday, location, schedule, need, personallyDonations, throughVolunteerDonations),"http://lg1:81/hapis/balloons/transactions/homeless/",username,homeless_slave);
                                String sentence = "sleep 20";
                                LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, CRITICAL_MESSAGE, null)); }

                            }
                        }
                });

    }

    private void personallyTransactions(String homelessUsername){

        mFirestore.collection("personallyDonations").whereEqualTo("donatesTo",homelessUsername )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String personallyDonations = String.valueOf(task.getResult().size());
                            homelessInfo.put("personallyDonations", personallyDonations);
                            mFirestore.collection("homeless").document(homelessUsername).set(homelessInfo, SetOptions.merge());

                        }
                    }
                });
    }


    private void throughVolunteerTransactions(String homelessUsername){

        mFirestore.collection("throughVolunteerDonations").whereEqualTo("donatesTo",homelessUsername )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
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
                "</LookAt>' > /tmp/query.txt ; sleep 25";
    }

    private POI createPOI(String name, String latitude, String longitude, String altitude){

        POI poi = new POI()
                .setLongitude(Double.parseDouble(longitude))
                .setName(name)
                .setLatitude(Double.parseDouble(latitude))
                .setAltitude(Double.parseDouble(altitude))
                .setHeading(0.0d)
                .setTilt(70.0d)
                .setRange(300.0d)
                .setAltitudeMode("relativeToSeaFloor");

        return poi;
    }



    private void showAllHomeless(){

        String city = preferences.getString("city","");

        mFirestore.collection("homeless").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String name = document.getString("homelessUsername");
                                final String longitude = document.getString("homelessLongitude");
                                final String latitude = document.getString("homelessLatitude");

                                 POI Homeless = createPOI(name,latitude, longitude, "0.0d" );
                                 POIController.getInstance().sendPlacemark(Homeless, null, defaultPrefs.getString("SSH-IP", "192.168.1.76"), "placemarks/homeless");
                                 POIController.getInstance().showPlacemark(Homeless,null, "https://i.ibb.co/1nsNbxr/homeless-icon.png", "placemarks/homeless");
                            }}
                    }
                });
    }

    private void showAllDonors(){

        String city = preferences.getString("city","");

        mFirestore.collection("donors").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String name = document.getString("username");
                                final String longitude = document.getString("longitude");
                                final String latitude = document.getString("latitude");
                                final String email = document.getString("email");

                                personallyTransactionsD(email);
                                throughVolunteerTransactionsD(email);

                                POI Homeless = new POI()
                                        .setName(name)
                                        .setLongitude(Double.parseDouble(longitude))
                                        .setLatitude(Double.parseDouble(latitude))
                                        .setAltitude(0.0d)
                                        .setHeading(0d)
                                        .setTilt(40.0d)
                                        .setRange(100.0d)
                                        .setAltitudeMode("relativeToSeaFloor ");
                                POIController.getInstance().sendPlacemark(Homeless, null, defaultPrefs.getString("SSH-IP", "192.168.1.76"), "placemarks/donors");
                                POIController.getInstance().showPlacemark(Homeless,null, "https://i.ibb.co/Bg4Lnvk/donor-icon.png", "placemarks/donors");
                            }}
                    }
                });
    }


    private void personallyTransactionsD(String email){

        mFirestore.collection("personallyDonations").whereEqualTo("donorEmail",email )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String personallyDonation = String.valueOf(task.getResult().size());
                            homelessInfo.put("personallyDonation", personallyDonation);
                            mFirestore.collection("donors").document(email).set(homelessInfo, SetOptions.merge());

                        }
                    }
                });
    }


    private void throughVolunteerTransactionsD(String email){

        mFirestore.collection("throughVolunteerDonations").whereEqualTo("donorEmail",email )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String throughVolunteerDonations = String.valueOf(task.getResult().size());
                            homelessInfo.put("throughVolunteerDonations", throughVolunteerDonations);
                            mFirestore.collection("donors").document(email).set(homelessInfo, SetOptions.merge());
                        }
                    }
                });
    }


    private void showAllVolunteers(){

        String city = preferences.getString("city","");

        mFirestore.collection("volunteers").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String name = document.getString("username");
                                final String longitude = document.getString("longitude");
                                final String latitude = document.getString("latitude");
                                final String email = document.getString("email");

                                homelessCreated(email);

                                POI Homeless = new POI()
                                        .setName(name)
                                        .setLongitude(Double.parseDouble(longitude))
                                        .setLatitude(Double.parseDouble(latitude))
                                        .setAltitude(0.0d)
                                        .setHeading(0d)
                                        .setTilt(40.0d)
                                        .setRange(100.0d)
                                        .setAltitudeMode("relativeToSeaFloor ");
                                POIController.getInstance().sendPlacemark(Homeless, null, defaultPrefs.getString("SSH-IP", "192.168.1.76"), "placemarks/volunteers");
                                POIController.getInstance().showPlacemark(Homeless,null, "https://i.ibb.co/xf1S6cn/volunteer-icon.png", "placemarks/volunteers");
                            }}
                    }
                });
    }

    private void homelessCreated(String email){

        mFirestore.collection("homeless").whereEqualTo("volunteerEmail",email )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String homelessCreated = String.valueOf(task.getResult().size());
                            volunteerInfo.put("homelessCreated", homelessCreated);
                            mFirestore.collection("volunteers").document(email).set(volunteerInfo, SetOptions.merge());

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CityActivity.this, CitiesActivity.class));
    }
}