package driverside.ojekkeren.com.ojekkerendriverside;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andi on 5/1/2016.
 */
public class POJOLegsGoogle {
    private List<POJOSteps> steps;

    public List<POJOSteps> getSteps() {
        return steps;
    }

    private Distance distance;
    private Duration duration;
    private String endAddress;
    private LatLng endLocation;
    private String startAddress;
    private LatLng startLocation;



    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

}
