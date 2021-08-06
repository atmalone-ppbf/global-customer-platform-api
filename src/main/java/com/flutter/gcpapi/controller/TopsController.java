package com.flutter.gcpapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flutter.gcpapi.model.Customer;
import com.flutter.gcpapi.model.NicknamedAccount;
import com.flutter.gcpapi.service.CustomerService;
import com.flutter.gcpapi.service.NicknameService;
import com.flutter.gcpapi.service.TopsService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/search")
public class TopsController {

    private final ObjectWriter objectWriter;

    public TopsController() {
        this.objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    }

    @GetMapping("/tops")
    public String inspect() throws Exception {
        String tops = "tops empty";
        try {
            String key = "";
            //The search is for an account with brand+Nickname
            //Call customer nickname service to get NICKNAMED_ACCOUNT_STATE_DESCRIPTOR then get customer from the matched account.
            final TopsService topsService = TopsService.init(8089);
            //Get the nicknamed account from state
            final Map<String, NicknamedAccount> topsMap = topsService.queryTopsState(key);

            Gson gson = new Gson();
            tops = gson.toJson(topsMap);
        } catch (Exception e) {
            log.error("Exception thrown", e);
        }
        return tops;
    }
}
