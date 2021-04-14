package com.flutter.gbsdinspector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flutter.gbsdinspector.model.SavepointAnalysis;
import com.flutter.gbsdinspector.service.FlinkSavepointAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/savepoint")
public class FlinkSavepointController {

    private final ObjectWriter objectWriter;

    public FlinkSavepointController() {
        this.objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    }

    @GetMapping
    public String index(Model model
    ) throws Exception {
        return "index";
    }

    @GetMapping("/inspect")
    public String inspect(
            @RequestParam(name = "path") String path,
            Model model
    ) {

        model.addAttribute("path", path);

        try {
            SavepointAnalysis stats = FlinkSavepointAnalysisService.analyseSavepoint(path);

            model.addAttribute("analysis", stats);
            model.addAttribute("analysisRaw", objectWriter.writeValueAsString(stats));
        } catch (Exception e) {
            log.error("Exception thrown", e);
            model.addAttribute("error", e);
        }

        return "savepoint";
    }
}
