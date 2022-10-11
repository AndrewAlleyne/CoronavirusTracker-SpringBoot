package com.covidtracker.coronavirustrackerspringboot.controllers;

import com.covidtracker.coronavirustrackerspringboot.models.LocationStats;
import com.covidtracker.coronavirustrackerspringboot.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.util.List;

//User send their request to the controller which then updates using the html view/template.

//Controller vs RestController - Both can be used, but we do not want a JSON response. We want a html response.

@Controller
public class HomeController {

    @Autowired
    private CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model){
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();
        int totalCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);



        model.addAttribute("totalCases", decimalFormat.format(totalCases));
        model.addAttribute("locationStats", coronaVirusDataService.getAllStats());
        return "home";
    }
}
