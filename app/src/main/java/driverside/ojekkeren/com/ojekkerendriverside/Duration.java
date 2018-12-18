package driverside.ojekkeren.com.ojekkerendriverside;

/**
 * Created by User Pc on 22/9/2016.
 */
public class Duration {
    public Duration(String text, long value) {
        this.text = text;
        this.value = value;
    }

    private String text;
    private long value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

}
