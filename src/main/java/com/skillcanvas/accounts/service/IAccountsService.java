package com.skillcanvas.accounts.service;

import com.skillcanvas.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     * Create a new account
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);


    CustomerDto fetchAccountsDetails(String mobileNumber);

}
