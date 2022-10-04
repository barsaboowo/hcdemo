package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.component.kubernetes.KubernetesConfiguration;
import org.apache.camel.component.kubernetes.cluster.KubernetesClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

    @Autowired
    @Qualifier("appName")
    private String myName;

    private final CamelContext camelContext;
    private final KubernetesConfiguration kubernetesConfiguration;

    @PostConstruct
    public void init() throws Exception {
        log.info("Starting route service");

        KubernetesClusterService clusterService = new KubernetesClusterService(kubernetesConfiguration);
        
        camelContext.addService(clusterService);
        camelContext.addRoutes(new XMLQConsumerRoute(camelContext));
        camelContext.addRoutes(new JSONQConsumerRoute(camelContext));
        camelContext.addRoutes(new CSVQConsumerRoute(camelContext));

        Random r = new Random();
        Thread.sleep(r.nextInt(5000));
        camelContext.addRoutes(new XMLQProducerRoute(camelContext, myName));
        Thread.sleep(r.nextInt(5000));
        camelContext.addRoutes(new JSONQProducerRoute(camelContext, myName));
        Thread.sleep(r.nextInt(5000));
        camelContext.addRoutes(new CSVQProducerRoute(camelContext, myName));
    }
}
