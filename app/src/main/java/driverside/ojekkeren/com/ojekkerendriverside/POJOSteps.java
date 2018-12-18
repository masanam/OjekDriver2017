package driverside.ojekkeren.com.ojekkerendriverside;


import javax.xml.datatype.Duration;

/**
 * Created by andi on 5/1/2016.
 */
public class POJOSteps {
    private Location start_location;
    private Location end_location;
    private POJOOverviewPolyLine polyline;


    public Location getStart_location() {
        return start_location;
    }

    public Location getEnd_location() {
        return end_location;
    }


    public POJOOverviewPolyLine getPolyline() {
        return polyline;
    }
}
