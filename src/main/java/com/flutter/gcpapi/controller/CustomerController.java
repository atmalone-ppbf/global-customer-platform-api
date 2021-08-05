package com.flutter.gcpapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flutter.gcpapi.model.Customer;
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
            boolean isCustomerIdSearch = false;

            //Determine if search string is nickname or customer id
            //Create the key
            if(NumberUtils.isCreatable(searchString)){
                Double customerId = Double.valueOf(searchString);
                isCustomerIdSearch = true;
                key = brandId + customerId;
            }else{
                key = brandId + searchString;
            }


            if (isCustomerIdSearch) {
                CustomerService service = CustomerService.init("localhost", 8080);
                final Customer customer = service.querySelectionState(key);
            }
        } catch (Exception e) {
            log.error("Exception thrown", e);
        }

        return "state";
    }
}
