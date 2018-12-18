package driverside.ojekkeren.com.ojekkerendriverside;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andi on 5/1/2016.
 */
public class POJOOverviewPolyLine {
    @SerializedName("points")
    public String points;

    public String getPoints() {
        return points;
    }
}
