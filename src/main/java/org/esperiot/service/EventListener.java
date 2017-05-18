package org.esperiot.service;

import com.espertech.esper.client.EPServiceProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class EventListener implements MqttCallback {

    private final Logger log = LoggerFactory.getLogger(EventListener.class);

    @Inject
    private EPServiceProvider epService;

    @Inject
    private ObjectMapper mapper;

    @Override
    public void connectionLost(Throwable cause) {
        log.error("Connection to Mqtt Broker lost! Cause: {}", cause.getCause());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String body = new String(message.getPayload());
        log.info("Topic: {}, Message: {}", topic, body);

        try {
            String eventName = topic.substring(topic.lastIndexOf('/') + 1);
            Class<?> cls = epService.getEPAdministrator()
                .getConfiguration()
                .getEventType(eventName)
                .getUnderlyingType();

            Object event = mapper.readValue(body, cls);
            epService.getEPRuntime().sendEvent(event);

        } catch (Exception e) {
            log.error("Can not resolve Mqtt message", e);
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
