package com.flutter.gbsdinspector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flutter.gbsdinspector.model.Customer;
import com.flutter.gbsdinspector.service.FlinkService;
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
            boolean isCustomerSearch = false;

            //Determine if search string is nickname or customer id
            if(NumberUtils.isCreatable(searchString)){
                Double customerId = Double.valueOf(searchString);
                isCustomerSearch = true;
                key = brandId + customerId;
            }else{
                key = brandId + searchString;
            }

            FlinkService service = FlinkService.init("localhost", 8080);
            if (isCustomerSearch) {
                final Customer selectionView = service.querySelectionState(key);
                model.addAttribute("selectionView", objectWriter.writeValueAsString(selectionView));
            }
        } catch (Exception e) {
            log.error("Exception thrown", e);
            model.addAttribute("error", e);
        }

        return "state";
    }
}
