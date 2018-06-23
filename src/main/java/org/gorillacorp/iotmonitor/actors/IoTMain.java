package org.gorillacorp.iotmonitor.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;

public class IoTMain {
    public static void main(String[] args) throws IOException {
        // Initialize the actor system
        ActorSystem actorSystem = ActorSystem.create("ioTSystem");

        // Create top level supervisor actor
        try{
            ActorRef supervisor = actorSystem.actorOf(IoTSupervisor.props(),
                    "iotSupervisor");
            System.out.println("Press ENTER to exit the system");
            System.in.read();
        } finally {
            actorSystem.terminate();
        }
    }
}
