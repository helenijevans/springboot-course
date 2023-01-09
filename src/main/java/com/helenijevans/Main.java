package com.helenijevans;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication // this line makes it a SpringBoot app
// an interface that encapsulates three configuration imports to have the needed behaviour
// this annotation is only needed in the main java file
@RestController // this annotation will allow the class to expose rest endpoints
// every method in this class will also return a JSON object
public class Main {

    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/") //the annotation needed to create an endpoint with a route
    // This annotation and RestController are part of the Spring Web MVC
    public GreetResponse greeting() {
        return new GreetResponse("hello");
        // will return {"greet": "hello"}
        // where "greet" is the parameter name given in GreetResponse
    }

    @GetMapping("api/v1/customers") 
    public List<Customer> GetCustomer (){
        return customerRepository.findAll();
        // returns empty list
    }

    record NewCustomerRequest(Integer id, String name, String email, Integer age){}

    //how to create a POST function
    // if you pass an ID value, then the generate sequence seems to override
    // yet the nextval functionality is not working... food for thought
    @PostMapping("api/v1/customers") 
    public void addCustomer(@RequestBody NewCustomerRequest request){
        Customer customer = new Customer();
        customer.setId(request.id);
        customer.setName(request.name);
        customer.setEmail(request.email);
        customer.setAge(request.age);
        customerRepository.save(customer);
    }

    @DeleteMapping("api/v1/customers/{customerId}") 
    public void deleteCustomer(@PathVariable("customerId") Integer id){
        customerRepository.deleteById(id);
    }

    @PutMapping("api/v1/customers/{customerId}") 
    public void updateCustomer(@PathVariable("customerId") Integer id, @RequestBody NewCustomerRequest request){
        for (Customer checkCustomer: GetCustomer()){
            if (checkCustomer.id == id){
                Customer customer = new Customer();
                customer.setId(request.id);
                customer.setName(request.name);
                customer.setEmail(request.email);
                customer.setAge(request.age);
                customerRepository.save(customer);
            }
        }
    }

    record GreetResponse(String greet){};
}
