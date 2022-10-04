package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hazelcast.HazelcastConstants;
import org.apache.camel.component.hazelcast.HazelcastOperation;
import org.apache.camel.impl.cluster.ClusteredRoutePolicy;
import org.apache.camel.spi.RoutePolicy;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class XMLQProducerRoute extends RouteBuilder {

    private final String myName;
    private final AtomicInteger msgCount = new AtomicInteger();

    public XMLQProducerRoute(CamelContext camelContext, String myName) {
        super(camelContext);
        this.myName = myName;
        log.info("My name is [{}]", this.myName);
    }

    @Override
    public void configure() throws Exception {
        RoutePolicy poli = ClusteredRoutePolicy.forNamespace("xmlOfferRoute");
        from("timer://xofferTimer?fixedRate=true&period=" + new Random().nextInt(1000))
                .routePolicy(poli)
                .routeId("xmlOfferRoute")
                .setHeader(HazelcastConstants.OPERATION, constant(HazelcastOperation.OFFER))
                .setBody(e -> String.format("XML Message from %s # %d", myName, msgCount.getAndIncrement()))
                .toF("hazelcast-%sxmlQ?hazelcastInstance=#hazelcastInstance", HazelcastConstants.QUEUE_PREFIX);
    }
}
