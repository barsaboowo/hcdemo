# HazelCast Camel Kubernetes Integration

* This is a reference implementation for a shared work queue using an embedded hazelcast queue implementation and Camel.
* The queue is distributed among instances via the hazelcast cluster.
* Hazelcast service discovery is achieved using ClusterIP Service under Hazelcast Kubernetes Service Discovery SPI.
* Camel is using Hazelcast endpoints to produce and consume from in-memory shared work queues.
* Thereby the workload can be distributed evenly across any number of instances.
* Instances are discovered automatically and added to the cluster if the deployment is scaled in Kubernetes.
* Camel is also using Kubernetes as a load balancer for Producer routes using the ClusterPolicy.

### Reference Documentation

* [Hazelcast](https://docs.hazelcast.com/home/)
* [Camel Hazelcast Queue Component](https://camel.apache.org/components/3.18.x/hazelcast-queue-component.html#_sample_for_take)

