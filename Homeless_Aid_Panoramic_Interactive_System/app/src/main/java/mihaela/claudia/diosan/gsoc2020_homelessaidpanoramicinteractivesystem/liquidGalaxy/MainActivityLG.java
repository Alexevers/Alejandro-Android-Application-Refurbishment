package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.HashMap;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGUtils;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks.VisitPoiTask;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.login.LoginActivity;

import static mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand.CRITICAL_MESSAGE;

public class MainActivityLG extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView cities, globalStatistics, cityStatistics;
    MaterialButton stopStatistics;
    SharedPreferences preferences;

    private Map<String, String> cityInfo = new HashMap<>();
    private Map<String, String> globalInfo = new HashMap<>();

    /*Firebase*/
    private FirebaseFirestore mFirestore;

    final Handler handler = new Handler();
    Runnable runnable;
    String sentence = "sleep 15";


    public static final POI EARTH_POI = new POI()
            .setName("Earth")
            .setLongitude(-3.629954d)  //10.52668d
            .setLatitude(40.769083d)  //40.085941d
            .setAltitude(0.0d)
            .setHeading(0.0d)      //90.0d
            .setTilt(0.0d)
            .setRange(10000000.0d)  //10000000.0d
            .setAltitudeMode("relativeToSeaFloor");

    public static final POI STATISTICS = new POI()
            .setName("GLOBAL_STATISTICS")
            .setLongitude(40.331103d)
            .setLatitude(75.297993d)
            .setAltitude(0.0d)
            .setHeading(90.0d)
            .setTilt(0.0d)
            .setRange(10000000.0d)
            .setAltitudeMode("relativeToSeaFloor");

    String logos_slave, homeless_slave, local_statistics_slave, global_statistics_slave, live_overview_homeless, hostname;

    public MainActivityLG() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_l_g);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        logos_slave = preferences.getString("logos_preference", "");
        homeless_slave = preferences.getString("homeless_preference", "");
        local_statistics_slave = preferences.getString("local_preference", "");
        global_statistics_slave = preferences.getString("global_preference", "");
        live_overview_homeless = preferences.getString("live_overview_homeless", "");

        String user = preferences.getString("SSH-USER", "lg");
        String password = preferences.getString("SSH-PASSWORD", "lqgalaxy");
        hostname = preferences.getString("SSH-IP", "");
        String port = preferences.getString("SSH-PORT", "22");


        initViews();
        cleanKmls(logos_slave, homeless_slave, local_statistics_slave, global_statistics_slave, live_overview_homeless, hostname);
        POIController.getInstance().moveToPOI(EARTH_POI, null);
        mFirestore = FirebaseFirestore.getInstance();



        LGConnectionManager lgConnectionManager = new LGConnectionManager();
        lgConnectionManager.setData(user, password, hostname, Integer.parseInt(port));


        cities.setOnClickListener(this);
        globalStatistics.setOnClickListener(this);
        cityStatistics.setOnClickListener(this);
        stopStatistics.setOnClickListener(this);

    }

    public static void cleanKmls(String logos_slave, String homeless_slave, String local_statistics_slave, String global_statistics_slave, String live_overview_homeless, String hostname) {

        POIController.cleanKmls();
        POIController.cleanKmlSlave(homeless_slave);
        POIController.cleanKmlSlave(local_statistics_slave);
        POIController.cleanKmlSlave(global_statistics_slave);
        POIController.cleanKmlSlave(live_overview_homeless);
        POIController.setLogos(logos_slave);


    }

    @Override
    protected void onResume() {
        super.onResume();

        String user = preferences.getString("SSH-USER", "lg");
        String password = preferences.getString("SSH-PASSWORD", "lqgalaxy");
        String hostname = preferences.getString("SSH-IP", "192.168.1.76");
        String port = preferences.getString("SSH-PORT", "22");

        LGConnectionManager lgConnectionManager = new LGConnectionManager();
        lgConnectionManager.setData(user, password, hostname, Integer.parseInt(port));

    }

    private void initViews() {
        cities = findViewById(R.id.cities_cv);
        globalStatistics = findViewById(R.id.statistics_cv);
        cityStatistics = findViewById(R.id.city_statistics);
        stopStatistics = findViewById(R.id.stop_statistics);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cities_cv:
                startActivity(new Intent(MainActivityLG.this, CitiesActivity.class));
                POIController.getInstance().moveToPOI(EARTH_POI, null);
                break;
            case R.id.statistics_cv:
                POIController.cleanKmls();
                cleanKmls(logos_slave, homeless_slave, local_statistics_slave, global_statistics_slave, live_overview_homeless, hostname);
                setGlobalStatistics();
                globalStatistics();
                break;
            case R.id.city_statistics:
                POIController.cleanKmls();
                cleanKmls(logos_slave, homeless_slave, local_statistics_slave, global_statistics_slave, live_overview_homeless, hostname);
                stopStatistics.setVisibility(View.VISIBLE);

                mFirestore.collection("cities")
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

                                        getCityHomelessNumber(city);
                                        getCityDonorsNumber(city);
                                        getCityVolunteersNumber(city);
                                        getCityFood(city);
                                        getCityClothes(city);
                                        getCityWork(city);
                                        getCityLodging(city);
                                        getCityHygiene(city);

                                        handler.postDelayed( runnable = new Runnable() {
                                            public void run() {
                                                POI cityPOI = createPOI(cityWS, latitude, longitude, altitude);
                                              /*  String sentence = "cd /var/www/html/hapis/balloons/statistics/cities/ ;curl -o " + cityPOI.getName() + " " + image;
                                                LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, CRITICAL_MESSAGE, null));
*/
                                                POIController.getInstance().showBalloonOnSlave(cityPOI, null, buildCityStatistics(city, homeless, donors, volunteers, foodSt, clothesSt, workSt, lodgingSt, hygieneSt), "http://lg1:81/hapis/balloons/statistics/cities/", cityPOI.getName(), local_statistics_slave);
                                                POIController.getInstance().moveToPOI(cityPOI, null);
                                                LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, CRITICAL_MESSAGE, null));
                                            }
                                        }, 1000);



                                        stopStatistics.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                handler.removeCallbacksAndMessages(null);
                                                handler.getLooper().quit();
                                                stopStatistics.setVisibility(View.GONE);
                                                POIController.cleanKmls();
                                                MainActivityLG.cleanKmls(logos_slave, homeless_slave, local_statistics_slave, global_statistics_slave, live_overview_homeless, hostname);
                                                POIController.getInstance().moveToPOI(EARTH_POI, null);
                                                Toast.makeText(MainActivityLG.this, "Local Statistics Stop", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }
                            }
                        });


                break;
        }

    }


    private void setGlobalStatistics() {
        getTotalCities();
        getHomelessNumber();
        getDonorsNumber();
        getVolunteersNumber();
        getFood();
        getWork();
        getLodging();
        getClothes();
        getHygiene();
        getPersonallyNumber();
        getThroughVolunteerNumber();
    }

    private void globalStatistics() {
        mFirestore.collection("statistics")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final String cities = document.getString("cities");
                                final String homeless = document.getString("homeless");
                                final String donors = document.getString("donors");
                                final String volunteers = document.getString("volunteers");
                                final String food = document.getString("food");
                                final String clothes = document.getString("clothes");
                                final String work = document.getString("work");
                                final String lodging = document.getString("lodging");
                                final String hygiene = document.getString("hygieneProducts");
                                final String personallyDonations = document.getString("personallyStatistics");
                                final String throughVolunteerDonations = document.getString("throughVolunteerStatistics");
                                final String image = document.getString("image");


                                POIController.getInstance().moveToPOI(STATISTICS, null);
                                String sentence = "cd /var/www/html/hapis/balloons/statistics/ ;curl -o " + STATISTICS.getName() + " " + image;
                                LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, CRITICAL_MESSAGE, null));
                                POIController.getInstance().showBalloonOnSlave(STATISTICS, null, buildGlobalStatistics(cities, homeless, donors, volunteers, food, clothes, work, lodging, hygiene, personallyDonations, throughVolunteerDonations), "http://lg1:81/hapis/balloons/statistics/", STATISTICS.getName(), global_statistics_slave);
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lg_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_lg:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_tools_lg:
                startActivity(new Intent(this, ToolsActivity.class));
                return true;
            case R.id.help_lg:
                startActivity(new Intent(this, HelpActivity.class));
                return true;
            case R.id.action_about_lg:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private POI createPOI(String name, String latitude, String longitude, String altitude) {

        POI poi = new POI()
                .setLongitude(Double.parseDouble(longitude))
                .setName(name)
                .setLatitude(Double.parseDouble(latitude))
                .setAltitude(Double.parseDouble(altitude))
                .setHeading(15.0d)
                .setTilt(60.0d)
                .setRange(1200.0d)
                .setAltitudeMode("relativeToSeaFloor");

        return poi;
    }

    private String buildGlobalStatistics(String cities, String homeless, String donors, String volunteers, String food, String clothes, String work, String lodging, String hygiene_products, String personallyStatistics, String throughVolunteerStatistics) {
        return "<h2> <b> CITIES</b></h2>\n" +
                "<p> <b> Total cities: </b> " + cities + "</p>\n" +
                "<h2> <b> USERS</b></h2>\n" +
                "<p> <b> Total homeless: </b> " + homeless + "</p>\n" +
                "<p> <b> Total donors: </b> " + donors + "</p>\n" +
                "<p> <b> Total volunteers: </b> " + volunteers + "</p>\n" +
                "<h2> <b> NEEDS</b></h2>\n" +
                "<p> <b> Food: </b> " + food + "</p>\n" +
                "<p> <b> Clothes: </b> " + clothes + "</p>\n" +
                "<p> <b> Work: </b> " + work + "</p>\n" +
                "<p> <b> Lodging: </b> " + lodging + "</p>\n" +
                "<p> <b> Hygiene products: </b> " + hygiene_products + "</p>\n" +
                "<h2> <b> DONATIONS</b></h2>\n" +
                "<p> <b> Personally Donations: </b> " + personallyStatistics + "</p>\n" +
                "<p> <b> Through Volunteer Donations: </b> " + throughVolunteerStatistics + "</p>\n";
    }


    public static String buildCityStatistics(String city, String homeless, String donors, String volunteers, String food, String clothes, String work, String lodging, String hygiene_products) {

        return "<h2> <b> Local statistics from: </b> " + city + "</h2>\n" +
                "<h2> <b> USERS</b></h2>\n" +
                "<p> <b> Total homeless: </b> " + homeless + "</p>\n" +
                "<p> <b> Total donors: </b> " + donors + "</p>\n" +
                "<p> <b> Total volunteers: </b> " + volunteers + "</p>\n" +
                "<h2> <b> NEEDS</b></h2>\n" +
                "<p> <b> Food: </b> " + food + "</p>\n" +
                "<p> <b> Clothes: </b> " + clothes + "</p>\n" +
                "<p> <b> Work: </b> " + work + "</p>\n" +
                "<p> <b> Lodging: </b> " + lodging + "</p>\n" +
                "<p> <b> Hygiene products: </b> " + hygiene_products + "</p>\n";
    }

    private void getTotalCities() {

        mFirestore.collection("cities").
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String cities = String.valueOf(task.getResult().size());
                            globalInfo.put("cities", cities);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }


    private void getHomelessNumber() {

        mFirestore.collection("homeless").
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String homeless = String.valueOf(task.getResult().size());
                            globalInfo.put("homeless", homeless);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }


    private void getCityHomelessNumber(String city) {

        mFirestore.collection("homeless").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String homelessNumber = String.valueOf(task.getResult().size());
                            cityInfo.put("homelessNumber", homelessNumber);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getDonorsNumber() {
        mFirestore.collection("donors").
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String donors = String.valueOf(task.getResult().size());
                            globalInfo.put("donors", donors);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityDonorsNumber(String city) {
        mFirestore.collection("donors").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String donorsNumber = String.valueOf(task.getResult().size());
                            cityInfo.put("donorsNumber", donorsNumber);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getVolunteersNumber() {
        mFirestore.collection("volunteers").
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String volunteers = String.valueOf(task.getResult().size());
                            globalInfo.put("volunteers", volunteers);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityVolunteersNumber(String city) {
        mFirestore.collection("volunteers").whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String volunteersNumber = String.valueOf(task.getResult().size());
                            cityInfo.put("volunteersNumber", volunteersNumber);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
                        }
                    }
                });
    }


    private void getFood() {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_food))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String food = String.valueOf(task.getResult().size());
                            globalInfo.put("food", food);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityFood(String city) {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_food))
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String foodSt = String.valueOf(task.getResult().size());
                            cityInfo.put("foodSt", foodSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
                        }
                    }
                });
    }


    private void getClothes() {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_clothes))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String clothes = String.valueOf(task.getResult().size());
                            globalInfo.put("clothes", clothes);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityClothes(String city) {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_clothes))
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String clothesSt = String.valueOf(task.getResult().size());
                            cityInfo.put("clothesSt", clothesSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
                        }
                    }
                });
    }


    private void getWork() {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_work))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String work = String.valueOf(task.getResult().size());
                            globalInfo.put("work", work);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityWork(String city) {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_work))
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String workSt = String.valueOf(task.getResult().size());
                            cityInfo.put("workSt", workSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
                        }
                    }
                });
    }


    private void getLodging() {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_lodging))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String lodging = String.valueOf(task.getResult().size());
                            globalInfo.put("lodging", lodging);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityLodging(String city) {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_lodging))
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String lodgingSt = String.valueOf(task.getResult().size());
                            cityInfo.put("lodgingSt", lodgingSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getHygiene() {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_hygiene_products))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String hygieneProducts = String.valueOf(task.getResult().size());
                            globalInfo.put("hygieneProducts", hygieneProducts);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }

    private void getCityHygiene(String city) {
        mFirestore.collection("homeless")
                .whereEqualTo("homelessNeed", getString(R.string.chip_hygiene_products))
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String hygieneSt = String.valueOf(task.getResult().size());
                            cityInfo.put("hygieneSt", hygieneSt);
                            mFirestore.collection("cities").document(city).set(cityInfo, SetOptions.merge());
                        }
                    }
                });
    }


    private void getPersonallyNumber() {
        mFirestore.collection("personallyDonations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String personallyStatistics = String.valueOf(task.getResult().size());
                            globalInfo.put("personallyStatistics", personallyStatistics);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });
    }


    private void getThroughVolunteerNumber() {
        mFirestore.collection("throughVolunteerDonations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String throughVolunteerStatistics = String.valueOf(task.getResult().size());
                            globalInfo.put("throughVolunteerStatistics", throughVolunteerStatistics);
                            mFirestore.collection("statistics").document("global").set(globalInfo, SetOptions.merge());
                        }
                    }
                });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivityLG.this, LoginActivity.class));

    }
}




