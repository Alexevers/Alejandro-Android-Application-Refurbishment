package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.logic;

import com.google.android.gms.maps.model.LatLng;

public class Homeless {
    private String image;
    private String homelessUsername;
    private String homelessPhoneNumber;
    private String homelessBirthday;
    private String homelessLifeHistory;
    private String homelessAddress;
    private String homelessSchedule;
    private String homelessNeed;
    private String homelessLongitude;
    private String homelessLatitude;

    public Homeless(){

    }

    public Homeless(String image, String homelessUsername, String phone, String birthday, String lifeHistory, String locationAddress, String schedule, String need){
        this.image = image;
        this.homelessUsername = homelessUsername;
        this.homelessPhoneNumber = phone;
        this.homelessBirthday =birthday;
        this.homelessLifeHistory = lifeHistory;
        this.homelessAddress = locationAddress;
        this.homelessSchedule = schedule;
        this.homelessNeed = need;

    }


    public Homeless(String homelessUsername, String homelessLongitude, String homelessLatitude){
        this.homelessUsername = homelessUsername;
        this.homelessLongitude = homelessLongitude;
        this.homelessLatitude = homelessLatitude;
    }

    public Homeless(String homelessUsername, String homelessAddress, String homelessNeed, String image){
        this.homelessUsername = homelessUsername;
        this.homelessAddress = homelessAddress;
        this.homelessNeed = homelessNeed;
        this.image = image;
    }

    public String getImage(){
        return image;
    }

    public String getHomelessUsername() {
        return homelessUsername;
    }

    public String getHomelessPhoneNumber() {
        return homelessPhoneNumber;
    }

    public String getHomelessBirthday() {
        return homelessBirthday;
    }

    public String getHomelessLifeHistory() {
        return homelessLifeHistory;
    }

    public String getHomelessAddress() {
        return homelessAddress;
    }

    public String getHomelessNeed() {
        return homelessNeed;
    }

    public String getHomelessSchedule() {
        return homelessSchedule;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getHomelessLongitude() {
        return homelessLongitude;
    }

    public String getHomelessLatitude() {
        return homelessLatitude;
    }
}

