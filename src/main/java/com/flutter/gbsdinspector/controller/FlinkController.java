package com.flutter.gbsdinspector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flutter.gbsdinspector.model.EventView;
import com.flutter.gbsdinspector.model.MarketView;
import com.flutter.gbsdinspector.model.ReadableEventEvict;
import com.flutter.gbsdinspector.model.SelectionView;
import com.flutter.gbsdinspector.service.FlinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/")
public class FlinkController {

    private final ObjectWriter objectWriter;

    public FlinkController() {
        this.objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    }

    @GetMapping
    public String index(Model model
    ) throws Exception {
        return "index";
    }

    @GetMapping("/inspect")
    public String inspect(
            @RequestParam(name = "host") String host,
            @RequestParam(name = "port") Integer port,
            @RequestParam(name = "jobId") String jobId,
            @RequestParam(name = "state") String state,
            @RequestParam(name = "key") Long key,
            Model model
    ) {

        model.addAttribute("host", host);
        model.addAttribute("port", port);
        model.addAttribute("jobId", jobId);
        model.addAttribute("state", state);
        model.addAttribute("key", key);

        try {
            FlinkService service = FlinkService.init(host, port);
            if ("event".equalsIgnoreCase(state)) {
                getEventInfo(jobId, key, model, service);
            } else if ("market".equalsIgnoreCase(state)) {
                getMarketInfo(jobId, key, model, service);
            } else if ("selection".equalsIgnoreCase(state)) {
                getSelectionInfo(jobId, key, model, service);
            }
        } catch (Exception e) {
            log.error("Exception thrown", e);
            model.addAttribute("error", e);
        }

        return "state";
    }

    private void getSelectionInfo(String jobId, Long key, Model model, FlinkService service) throws Exception {
        final SelectionView selectionView = service.querySelectionState(jobId, key);
        model.addAttribute("selectionView", objectWriter.writeValueAsString(selectionView));
        getMarketInfo(jobId, selectionView.getMarketId(), model, service);
    }

    private void getMarketInfo(String jobId, Long key, Model model, FlinkService service) throws Exception {
        final MarketView marketView = service.queryMarketState(jobId, key);
        model.addAttribute("marketView", objectWriter.writeValueAsString(marketView));
        getEventInfo(jobId, marketView.getEventId(), model, service);
    }

    private void getEventInfo(String jobId, Long key, Model model, FlinkService service) throws Exception {
        final EventView event = service.queryEventState(jobId, key);
        model.addAttribute("eventView", objectWriter.writeValueAsString(event));
        final ReadableEventEvict readableEventEvict = ReadableEventEvict.fromEventEvict(service.queryEventEvictState(jobId, key));
        model.addAttribute("eventEvict", objectWriter.writeValueAsString(readableEventEvict));
        // this one might be null, since having event created without any markets/selections might be a valid case
        try {
            final Map<Long, Long> selectionsToMarketsMap = service.querySelectionsToMarketsEvictState(jobId, key);
            model.addAttribute("selectionsToMarketsEvictionMap", objectWriter.writeValueAsString(selectionsToMarketsMap));
        } catch (Exception e) {
            log.error("Exception thrown", e);
        }
    }
}
