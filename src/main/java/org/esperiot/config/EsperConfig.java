package org.esperiot.config;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsperConfig {

    private final Logger log = LoggerFactory.getLogger(EsperConfig.class);

    @Bean(destroyMethod = "destroy")
    EPServiceProvider epService() {


        com.espertech.esper.client.Configuration conf = new com.espertech.esper.client.Configuration();
        conf.configure("esper.cfg.xml");

        return EPServiceProviderManager.getDefaultProvider(conf);
    }
}
