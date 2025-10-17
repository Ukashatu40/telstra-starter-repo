package au.com.telstra.simcardactivator;

import au.com.telstra.simcardactivator.model.Customer;
import au.com.telstra.simcardactivator.model.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ActivationController {

    private final RestTemplate restTemplate;
    private static final String ACTUATOR_URL = "http://localhost:8444/actuate";

    CustomerRepository customerRepository;

    @Autowired // Inject RestTemplate
    public ActivationController(RestTemplate restTemplate, CustomerRepository customerRepository) {
        this.restTemplate = restTemplate;
        this.customerRepository = customerRepository;
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

            if(success){
                Customer customer = new Customer();
                customer.setIccid(request.getIccid());
                customer.setCustomerEmail(request.getCustomerEmail());
                customer.setActive(success);

                // Save the customer data
                customerRepository.save(customer);
            }

            return "SIM activation request processed. Success: " + success;

        } catch (Exception e) {
            // Handle connection errors, etc.
            System.err.println("Error calling Actuator for ICCID " + request.getIccid() + ": " + e.getMessage());
            return "SIM activation request failed due to system error.";
        }
    }

    @GetMapping("/api/v1/activate")
    public List<Customer> getActivatedCustomers(){
        return (List<Customer>) customerRepository.findAll();
    }
}