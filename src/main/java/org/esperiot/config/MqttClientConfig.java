package org.esperiot.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.esperiot.service.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

@Configuration
public class MqttClientConfig implements EnvironmentAware {

    private final Logger log = LoggerFactory.getLogger(MqttClientConfig.class);

    private RelaxedPropertyResolver propertyResolver;

    @Inject
    private EventListener eventListener;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "mqtt.");
    }

    @Bean(destroyMethod = "disconnect")
    MqttClient mqttClient() {
        log.debug("MqttClient client is connecting");

        String serverURI = propertyResolver.getProperty("serverURI");

        if (serverURI == null) {
            log.error("Your mqtt.serverURI is not configured properly");
            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }

        try {

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            MqttClient mqttClient = new MqttClient(serverURI, "CepManager");
            mqttClient.connect(connOpts);
            mqttClient.setCallback(eventListener);
            mqttClient.subscribe("/incoming/#", 2);
            log.info("Connected to Mqtt server, {}", serverURI);
            return mqttClient;
        } catch (MqttException me) {
            log.error("Mqtt Client creation failed: \n code: {} \n msg: {} ", me.getReasonCode(), me.getMessage());
            throw new BeanCreationException("mqttClient", "Failed to create a MqttClient", me);
        }

    }
}
