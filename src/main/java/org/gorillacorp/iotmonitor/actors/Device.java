package org.gorillacorp.iotmonitor.actors;
import java.util.Optional;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Device extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    final String groupId;
    final String deviceId;

    // When a parent actor creates a Device, we must be sure that the Device actor will also get a
    // unique deviceId and deviceGroupId
    public static Props props(String groupId, String deviceId) {
        return Props.create(Device.class, groupId, deviceId);
    }

    @RequiredArgsConstructor
    public static final class ReadTemperature {
        long requestId;
    }

    public static final class RespondTemperature {
        long requestId;
        Optional<Double> value;

        public RespondTemperature(long requestId, Optional<Double> value) {
            this.requestId = requestId;
            this.value = value;
        }
    }

    @RequiredArgsConstructor
    public static final class RecordTemperature{
        final long requestId;
        final double value;
    }

    @RequiredArgsConstructor
    public static final class TemperatureRecorded{
        final long requestId;
    }

    Optional<Double> lastTemperatureReading = Optional.empty();

    @Override
    public void preStart() {
        log.info("Device actor {}-{} started", groupId, deviceId);
    }

    @Override
    public void postStop() {
        log.info("Device actor {}-{} stopped", groupId, deviceId);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // This stuff translates to "Receive a ReadTemperature message type and respond with a RespondTemperature
                // message type
                .match(ReadTemperature.class, r -> getSender().tell(new RespondTemperature(r.requestId, lastTemperatureReading), getSelf()))
                // This stuff translates to "Receive a RecordTemperature message type and respond with a TemperatureRecorded
                // message type
                .match(RecordTemperature.class, r -> {
                    log.info("Recorded temperature reading {} with {}", r.value, r.requestId);
                    lastTemperatureReading = Optional.of(r.value);
                    getSender().tell(new TemperatureRecorded(r.requestId), getSelf());
                })
                .build();
    }

}