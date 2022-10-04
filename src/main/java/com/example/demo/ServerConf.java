package com.example.demo;

import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.component.kubernetes.KubernetesConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.Random;

@Configuration
public class ServerConf {

    private static final String[] names = {"Kajula", "Muthungu", "Prabanasa", "Tenaratasa", "Danata", "Subuku", "Ratataka", "Katalaka"};


    @Bean("appName")
    String appName(){
        return names[new Random().nextInt(8)] + " " + names[new Random().nextInt(8)];
    }

    @Bean
    Config hcConfig(@Value("${kube.namespace}") String namespace, @Value("${kube.servicename}") String serviceName){
        Config config = new Config();
        config.setClusterName("qDemo-cluster");
        config.setInstanceName("qDemo");
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
                .setProperty("namespace", namespace)
                .setProperty("service-name", serviceName)
                .setProperty("kubernetes-api-retries", "100");

        config.getQueueConfigs().put("xmlQ", new QueueConfig().setBackupCount(1).setMaxSize(1000000));
        config.getQueueConfigs().put("jsonQ", new QueueConfig().setBackupCount(1).setMaxSize(1000000));
        config.getQueueConfigs().put("csvQ", new QueueConfig().setBackupCount(1).setMaxSize(1000000));

        return config;
    }

    @Bean
    KubernetesConfiguration kubernetesConfiguration(@Value("${kube.namespace}") String namespace){
        KubernetesConfiguration kubernetesConfiguration = new KubernetesConfiguration();
        kubernetesConfiguration.setNamespace(namespace);

        return kubernetesConfiguration;
    }

    @Bean("hazelcastInstance")
    HazelcastInstance hazelcastInstance(Config conf){
        return Hazelcast.newHazelcastInstance(conf);
    }

    @PreDestroy
    void shutdown(){
        Hazelcast.shutdownAll();
    }
}
