package com.flutter.gcpapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flutter.gcpapi.model.Customer;
import com.flutter.gcpapi.model.NicknamedAccount;
import com.flutter.gcpapi.service.CustomerService;
import com.flutter.gcpapi.service.NicknameService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final ObjectWriter objectWriter;

    public CustomerController() {
        this.objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    }

    @GetMapping()
    public String inspect(
            @RequestParam(name = "accountId") String accountId,
            @RequestParam(name = "brand") String brand
    ) throws Exception {
        String customerJson = "customer not found";
        try {
            String key;
            boolean isNicknameSearch = false;

            //Determine if search string is nickname or customer id
            //Create the key
            if (NumberUtils.isCreatable(accountId)) {
                Double accountIdFromSearch = Double.valueOf(accountId);
                key = brand + accountIdFromSearch;
            } else {
                isNicknameSearch = true;
                key = brand + accountId;
            }

            if (isNicknameSearch) {
                //The search is for an account with brand+Nickname
                //Call customer nickname service to get NICKNAMED_ACCOUNT_STATE_DESCRIPTOR then get customer from the matched account.
                final NicknameService nicknameService = NicknameService.init(8089);
                //Get the nicknamed account from state
                final Map<String, NicknamedAccount> nicknamedAccountMap = nicknameService.queryNicknamedAccountState(key);

                NicknamedAccount nicknamedAccount = nicknamedAccountMap.get(accountId);
                key = nicknamedAccount.getBrand() + nicknamedAccount.getAccountId();
            }

            //get the customer account from the nicknamed account
            CustomerService service = CustomerService.init(8089);
            final Customer customer = service.queryCustomerState(key);
            Gson gson = new Gson();
            customerJson = gson.toJson(customer);
        } catch (Exception e) {
            log.error("Exception thrown", e);
        }
        return customerJson;
    }
}
