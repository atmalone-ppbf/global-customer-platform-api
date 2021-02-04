package com.flutter.gbsdinspector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flutter.gbsdinspector.model.*;
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
    ) throws Exception {

        model.addAttribute("host", host);
        model.addAttribute("port", port);
        model.addAttribute("jobId", jobId);
        model.addAttribute("state", state);
        model.addAttribute("key", key);

        try {
            FlinkService service = FlinkService.init(host, port);
            if ("event".equalsIgnoreCase(state)) {
                final EventView event = service.queryEventState(jobId, key);
                model.addAttribute("eventView", objectWriter.writeValueAsString(event));
            } else if ("market".equalsIgnoreCase(state)) {
                final MarketView marketView = service.queryMarketState(jobId, key);
                final EventView eventView = service.queryEventState(jobId, marketView.getEventId());
                model.addAttribute("marketView", objectWriter.writeValueAsString(marketView));
                model.addAttribute("eventView", objectWriter.writeValueAsString(eventView));
            } else if ("selection".equalsIgnoreCase(state)) {
                final SelectionView selectionView = service.querySelectionState(jobId, key);
                final MarketView marketView = service.queryMarketState(jobId, selectionView.getMarketId());
                final EventView eventView = service.queryEventState(jobId, marketView.getEventId());
                model.addAttribute("selectionView", objectWriter.writeValueAsString(selectionView));
                model.addAttribute("marketView", objectWriter.writeValueAsString(marketView));
                model.addAttribute("eventView", objectWriter.writeValueAsString(eventView));
            } else if ("eviction".equalsIgnoreCase(state)) {
                final ReadableEventEvict readableEventEvict = ReadableEventEvict.fromEventEvict(service.queryEventEvictState(jobId, key));
                final Map<Long, Long> selectionsToMarketsMap = service.querySelectionsToMarketsEvictState(jobId, key);
                model.addAttribute("eventEvict", objectWriter.writeValueAsString(readableEventEvict));
                model.addAttribute("selectionsToMarketsEvictionMap", objectWriter.writeValueAsString(selectionsToMarketsMap));
            }
        } catch (Exception e) {
            log.error("Exception thrown", e);
            model.addAttribute("error", e);
        }

        return "state";
    }
}
