package com.alpine.companies_crud.config;


import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(999)
@Slf4j
public class LogObservationHandler implements ObservationHandler<Observation.Context> {
    @Override
    public void onStart(Observation.Context context) {
        log.info("ObservationHandler::onStar: {}", context.getName());
    }

    @Override
    public void onError(Observation.Context context) {
        log.info("ObservationHandler::onError: {}", context.getName());
    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("ObservationHandler::onStop: {}", context.getName());
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }
}
