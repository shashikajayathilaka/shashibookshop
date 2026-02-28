package controller.customer;

import model.Customer;

import java.util.List;

public class CustomerServiceImpl implements CustomerService{
    @Override
    public boolean addCustomer(Customer customer) {
        return false;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        return false;
    }

    @Override
    public boolean deleteCustomer(String id) {
        return false;
    }

    @Override
    public Customer searchCustomerById(String id) {
        return null;
    }

    @Override
    public List<Customer> getAll() {
        return List.of();
    }
}
