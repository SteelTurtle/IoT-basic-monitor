package org.gorillacorp.iotmonitor.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class IoTSupervisor extends AbstractActor {

    private final LoggingAdapter log = Logging
            .getLogger(getContext().getSystem(),this);

    //create root actor
    public static Props props(){
        return Props.create(IoTSupervisor.class);
    }

    @Override
    public void preStart(){
        log.info("IoT Application started");
    }

    @Override
    public void postStop(){
        log.info("IoT Application stopped");
    }

    // the root actor does not handle any message
    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
