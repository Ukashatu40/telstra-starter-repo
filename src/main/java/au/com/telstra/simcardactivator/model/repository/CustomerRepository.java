package au.com.telstra.simcardactivator.model.repository;

import au.com.telstra.simcardactivator.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByCustomerEmail(String customerEmail);

    Customer findById(long id);
}