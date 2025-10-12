package au.com.telstra.simcardactivator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ActivationController {

    private final RestTemplate restTemplate;
    private static final String ACTUATOR_URL = "http://localhost:8444/actuate";

    @Autowired // Inject RestTemplate
    public ActivationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/api/v1/activate")
    public String activateSim(@RequestBody ActivationRequest request) {

        // 1. Prepare payload for the Actuator
        ActuatorRequest actuatorPayload = new ActuatorRequest();
        actuatorPayload.setIccid(request.getIccid());

        try {
            // 2. Submit POST request to the Actuator
            ActuatorResponse response = restTemplate.postForObject(
                    ACTUATOR_URL,
                    actuatorPayload,
                    ActuatorResponse.class
            );

            // 3. Print the outcome
            boolean success = response != null && response.isSuccess();
            System.out.println("Activation request for ICCID " + request.getIccid() +
                    " reported: " + (success ? "SUCCESS" : "FAILURE"));

            return "SIM activation request processed. Success: " + success;

        } catch (Exception e) {
            // Handle connection errors, etc.
            System.err.println("Error calling Actuator for ICCID " + request.getIccid() + ": " + e.getMessage());
            return "SIM activation request failed due to system error.";
        }
    }
}