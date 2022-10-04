package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hazelcast.HazelcastConstants;

@Slf4j
@RequiredArgsConstructor
public class XMLQConsumerRoute extends RouteBuilder {

    private final CamelContext camelContext;

    @Override
    public void configure() throws Exception {
        fromF("hazelcast-%sxmlQ?queueConsumerMode=Poll&hazelcastInstance=#hazelcastInstance&poolSize=" + Runtime.getRuntime().availableProcessors(), HazelcastConstants.QUEUE_PREFIX)
                .routeId("xmlConsumerRoute")
                .process(e -> log.info("Processed " + e.getIn().getBody(String.class)));
    }
}
