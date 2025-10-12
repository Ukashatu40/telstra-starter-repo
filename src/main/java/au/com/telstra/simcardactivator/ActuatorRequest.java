package au.com.telstra.simcardactivator;

public class ActuatorRequest {
    private String iccid;

    // Add Getters and Setters
    // Note: The Actuator only needs the ICCID.

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
}
