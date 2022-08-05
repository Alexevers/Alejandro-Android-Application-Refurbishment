package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.logic;

public class Delivery {
    private String donatesTo;
    private String donorUsername;
    private String donorEmail;
    private String donorPhone;
    private String donationType;
    private String donationLocation;
    private String donationHour;
    private String donationDate;

    public Delivery(){

    }

    public Delivery(String homelessUsername, String donorUsername, String donorEmail, String donorPhone, String donationType, String location, String date, String time){
        this.donatesTo = homelessUsername;
        this.donorUsername = donorUsername;
        this.donorEmail = donorEmail;
        this.donorPhone = donorPhone;
        this.donationType = donationType;
        this.donationLocation = location;
        this.donationHour = time;
        this.donationDate = date;

    }

    public String getDonatesTo() {
        return donatesTo;
    }

    public String getDonorUsername() {
        return donorUsername;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public String getDonationType() {
        return donationType;
    }

    public String getDonationLocation() {
        return donationLocation;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public String getDonationHour() {
        return donationHour;
    }
}
