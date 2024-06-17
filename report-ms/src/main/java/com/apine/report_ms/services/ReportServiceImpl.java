package com.apine.report_ms.services;


import ch.qos.logback.classic.spi.IThrowableProxy;
import com.apine.report_ms.helpers.ReportHelper;
import com.apine.report_ms.models.Company;
import com.apine.report_ms.models.WebSite;
import com.apine.report_ms.repositories.CompaniesRepository;
import com.apine.report_ms.repositories.CompanyFallbackRepository;
import com.apine.report_ms.streams.ReportPublisher;
import com.netflix.discovery.EurekaClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService{


    private final CompaniesRepository companiesRepository;
    private final ReportHelper reportHelper;
    private final CompanyFallbackRepository companyFallbackRepository;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final ReportPublisher reportPublisher;

    @Override
    public String makeReport(String name) {
        //return reportHelper.readTemplate(this.companiesRepository.getByName(name).orElseThrow());
        var circuitBreaker = this.circuitBreakerFactory.create("companies-circuitBreakerFactory");
       return circuitBreaker.run(
               () -> this.makeReportMain(name),
               throwable -> this.makeReportFallback(name, throwable)
       );

    }

    @Override
    public String saveReport(String report) {

        var format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var placeholder = this.reportHelper.getPlaceholdersFromTemplate(report);
        var webSites = Stream.of(placeholder.get(3))
                .map(webSite -> WebSite.builder().name(webSite).build())
                .toList();

        var company = Company.builder()
                .name(placeholder.get(0))
                .foundationDate(LocalDate.parse(placeholder.get(1), format))
                .founder(placeholder.get(2))
                .webSites(webSites)
                .build();

        this.reportPublisher.publishReport(report);
        this.companiesRepository.postByName(company);
        return null;
    }

    @Override
    public void deleteReport(String name) {
        this.companiesRepository.deleteByName(name);
    }



    public String makeReportMain(String name) {
        return reportHelper.readTemplate(this.companiesRepository.getByName(name).orElseThrow());
    }


    public String makeReportFallback(String name, Throwable error) {
        log.warn(error.getMessage());
        return reportHelper.readTemplate(this.companyFallbackRepository.getByName(name));

    }

}
