package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils;

import java.util.List;

public class Cities {
    private String city;
    private String country;
    private String image;
    private String latitude;
    private String longitude;
    private String altitude;


    public Cities(){}

    public Cities(String city, String country, String image, String latitude, String longitude, String altitude) {
        this.city = city;
        this.country = country;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public Cities(String city, String latitude, String longitude, String altitude) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getImage() {
        return image;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAltitude() {
        return altitude;
    }

}
