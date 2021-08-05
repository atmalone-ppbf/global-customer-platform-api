package com.flutter.gcpapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flutter.gcpapi.model.Customer;
import com.flutter.gcpapi.model.NicknamedAccount;
import com.flutter.gcpapi.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/search")
public class CustomerController {

    private final ObjectWriter objectWriter;

    public CustomerController() {
        this.objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    }

    @GetMapping("/inspect")
    public String inspect(
            @RequestParam(name = "searchString") String searchString,
            @RequestParam(name = "brandId") String brandId
    ) throws Exception {

        try {

            String key = "";
            boolean isAccountIdSearch = false;

            //Determine if search string is nickname or customer id
            //Create the key
            if(NumberUtils.isCreatable(searchString)){
                Double accountId = Double.valueOf(searchString);
                isAccountIdSearch = true;
                key = brandId + accountId;
            }else{
                key = brandId + searchString;
            }


            CustomerService service = CustomerService.init("localhost", 8080);
            if (isAccountIdSearch) {
                //if the key is a Brand+accountId call service for getting the state of CUSTOMER_STATE_DESCRIPTOR
                final Customer customer = service.queryCustomerState(key);
            }
            else {
                //Call customer nickname service to get NICKNAMED_ACCOUNT_STATE_DESCRIPTOR then get customer from the matched account.
                final NicknamedAccount nicknamedAccount = service.queryNicknamedAccountState(key);
            }
        } catch (Exception e) {
            log.error("Exception thrown", e);
        }

        return "state";
    }
}
