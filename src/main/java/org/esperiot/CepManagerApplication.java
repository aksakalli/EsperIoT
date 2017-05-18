package org.esperiot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CepManagerApplication {

    public static void main(String[] args) {


        SpringApplication.run(CepManagerApplication.class, args);

//        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
//        String expression = "select avg(price) from org.esperiot.event.SimpleEvent.win:time(30 sec)";
//        EPStatement statement = epService.getEPAdministrator().createEPL(expression);
////		epService.getEPAdministrator().cre
//        StatementListener listener = new StatementListener();
//        statement.addListener(listener);
//        Configuration config = new Configuration();
////		config.
//
//        SimpleEvent event = new SimpleEvent("shirt", 74.50);
//        epService.getEPRuntime().sendEvent(event);
    }
}
