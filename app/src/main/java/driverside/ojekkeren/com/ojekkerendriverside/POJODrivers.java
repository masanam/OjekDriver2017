package driverside.ojekkeren.com.ojekkerendriverside;

/**
 * Created by andi on 4/29/2016.
 */
public class POJODrivers {
    private int id;
    private String drivernik;
    private String password;
    private String phonenum;
    private String drivername;
    private String lokasisekaranglat;
    private String lokasisekaranglong;
    private int sedang_proses_orderid;
    private int status;
    private String isLogged;
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(String isLogged) {
        this.isLogged = isLogged;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrivernik() {
        return drivernik;
    }

    public void setDrivernik(String drivernik) {
        this.drivernik = drivernik;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getLokasisekaranglat() {
        return lokasisekaranglat;
    }

    public void setLokasisekaranglat(String lokasisekaranglat) {
        this.lokasisekaranglat = lokasisekaranglat;
    }

    public String getLokasisekaranglong() {
        return lokasisekaranglong;
    }

    public void setLokasisekaranglong(String lokasisekaranglong) {
        this.lokasisekaranglong = lokasisekaranglong;
    }

    public int getSedang_proses_orderid() {
        return sedang_proses_orderid;
    }

    public void setSedang_proses_orderid(int sedang_proses_orderid) {
        this.sedang_proses_orderid = sedang_proses_orderid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
