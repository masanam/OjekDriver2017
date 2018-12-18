package driverside.ojekkeren.com.ojekkerendriverside;

/**
 * Created by andi on 4/29/2016.
 */
public class POJODriversData {
    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    private String orderid;
    private String typeorder;
    private String uid;
    private String phoneNum;
    private String from;
    private String latlangFrom;
    private String latlangTo;
    private String to;

    public String getLatlangFrom() {
        return latlangFrom;
    }

    public void setLatlangFrom(String latlangFrom) {
        this.latlangFrom = latlangFrom;
    }

    public String getLatlangTo() {
        return latlangTo;
    }

    public void setLatlangTo(String latlangTo) {
        this.latlangTo = latlangTo;
    }

    private String addressfrom;

    public String getAddressfrom() {
        return addressfrom;
    }

    public void setAddressfrom(String addressfrom) {
        this.addressfrom = addressfrom;
    }

    public String getAddressto() {

        return addressto;
    }

    public void setAddressto(String addressto) {
        this.addressto = addressto;
    }

    private String addressto;
    private String price;
    private String ordertime;
    private String itemtodeliver;
    private String drivereksekusi;
    private String driverid;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    private String distance;

    public String getTypeorder() {
        return typeorder;
    }

    public void setTypeorder(String typeorder) {
        this.typeorder = typeorder;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getItemtodeliver() {
        return itemtodeliver;
    }

    public void setItemtodeliver(String itemtodeliver) {
        this.itemtodeliver = itemtodeliver;
    }

    public String getDrivereksekusi() {
        return drivereksekusi;
    }

    public void setDrivereksekusi(String drivereksekusi) {
        this.drivereksekusi = drivereksekusi;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }
}
