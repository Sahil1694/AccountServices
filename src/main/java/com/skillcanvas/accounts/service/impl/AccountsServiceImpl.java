package com.skillcanvas.accounts.service.impl;

import com.skillcanvas.accounts.constants.AccountsConstants;
import com.skillcanvas.accounts.dto.AccountsDto;
import com.skillcanvas.accounts.dto.CustomerDto;
import com.skillcanvas.accounts.entiity.Accounts;
import com.skillcanvas.accounts.entiity.Customer;
import com.skillcanvas.accounts.exception.CustomerAlreadyExistsException;
import com.skillcanvas.accounts.exception.ResourceNotFoundException;
import com.skillcanvas.accounts.mapper.AccountsMapper;
import com.skillcanvas.accounts.mapper.CustomerMapper;
import com.skillcanvas.accounts.repository.AccountsRepository;
import com.skillcanvas.accounts.repository.CustomerRepository;
import com.skillcanvas.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     * Create a new account
     * @param customerDto
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer>optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already exists with mobile number "+customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer =  customerRepository.save(customer);
        accountsRepository.save(createNewAccounts(savedCustomer));
    }

    /**
     * Fetch account details
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDto fetchAccountsDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomerDto customerDto =  CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts , new AccountsDto()));

        return customerDto;
    }

    /**
     * Create a new account
     * @param customer
     * @return the new account details
     */
    private Accounts createNewAccounts(Customer customer){
        Accounts newAccounts = new Accounts();
        newAccounts.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccounts.setAccountNumber(randomAccNumber);
        newAccounts.setAccountType(AccountsConstants.SAVINGS);
        newAccounts.setBranchAddress(AccountsConstants.ADDRESS);
        newAccounts.setCreatedAt(LocalDateTime.now());
        newAccounts.setCreatedBy("Anonymous");
        return newAccounts;
    }


}
