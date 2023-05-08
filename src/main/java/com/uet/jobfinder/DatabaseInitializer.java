package com.uet.jobfinder;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @EventListener
    public void onApplicationStartSuccess(ApplicationReadyEvent event) {
        System.out.println("EVERYTHING HAS BEEN CREATED");
    }

}
