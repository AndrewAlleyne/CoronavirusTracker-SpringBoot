package com.covidtracker.coronavirustrackerspringboot.services;

import com.covidtracker.coronavirustrackerspringboot.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;

//When spring starts it will create an instance of this class.
@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    //Allows spring to execute method after dependency injection is performed.
    @PostConstruct
    @Scheduled(cron = "* * *  * * *")
    public void fetchVirusData() throws IOException, InterruptedException {

        List<LocationStats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL)).build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Display the response body.
        //System.out.println(httpResponse.body());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            String province = record.get("Province/State");
            String country = record.get("Country/Region");
            String region = record.get("Country/Region");
            String latitude = record.get("Lat");
            String longitude = record.get("Long");

            LocationStats locationStat = new LocationStats();

            locationStat.setState(province);
            locationStat.setCountry(country);
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            int latestCases = Integer.parseInt
                    (record.get(record.size() - 1));
            int previousCases = Integer.parseInt
                    (record.get(record.size() - 2));
            locationStat.setCaseDiff(latestCases - previousCases);

            newStats.add(locationStat);
        }
        //user is able to fetch information while we are getting new information.
        this.allStats = newStats;
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }

}
