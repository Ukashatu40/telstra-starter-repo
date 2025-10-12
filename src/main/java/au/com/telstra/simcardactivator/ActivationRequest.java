package au.com.telstra.simcardactivator;

public class ActivationRequest {
    private String iccid; // unique global identifier
    private String customerEmail;

    // Add Getters and Setters (IDE can generate these)

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
