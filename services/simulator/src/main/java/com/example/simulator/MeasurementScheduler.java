package com.example.simulator;

import com.example.simulator.producer.MeasurementEvent;
import com.example.simulator.producer.MeasurementProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MeasurementScheduler {

    private final MeasurementProducer producer;
    private final DeviceClient deviceClient;

    // pointer de timp de unde începem să generăm istoric
    private volatile long currentPointerMillis;

    // 3 luni în urmă
    private static final long MONTHS_BACK = 3;

    public MeasurementScheduler(MeasurementProducer producer, DeviceClient deviceClient) {
        this.producer = producer;
        this.deviceClient = deviceClient;

        // calculăm "acum - 3 luni", trunchiat la începutul orei
        LocalDateTime threeMonthsAgo = LocalDateTime.now()
                .minusMonths(MONTHS_BACK)
                .truncatedTo(ChronoUnit.HOURS);

        this.currentPointerMillis = threeMonthsAgo
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    /**
     * Rulează la fiecare 5 secunde.
     * La fiecare execuție:
     *  - luăm TOATE device-urile din device-service
     *  - pentru toate device-urile trimitem câte o măsurătoare
     *    cu timestamp = currentPointerMillis
     *  - apoi mutăm pointerul cu +1 oră
     *  - dacă pointerul trece de "acum", îl resetăm la "acum - 3 luni"
     */
    @Scheduled(fixedRate = 5000)
    public void generateMeasurements() {
        // luăm device-urile din device-service
        List<Long> deviceIds = deviceClient.getAllDeviceIds();

        if (deviceIds == null || deviceIds.isEmpty()) {
            System.err.println("[SIMULATOR] No devices found when generating measurements.");
            return;
        }

        long pointer = currentPointerMillis;

        for (Long deviceId : deviceIds) {
            // consum random între 1 și 10 – poți ajusta după gust
            double consumption = ThreadLocalRandom.current().nextDouble(1.0, 10.0);

            MeasurementEvent event = new MeasurementEvent(
                    deviceId,
                    pointer,
                    consumption
            );

            producer.sendMeasurement(event);
        }

        // mutăm pointerul la ora următoare
        long oneHourMillis = 60L * 60L * 1000L;
        long newPointer = pointer + oneHourMillis;

        long nowMillis = Instant.now().toEpochMilli();
        long threeMonthsAgoMillis = LocalDateTime.now()
                .minusMonths(MONTHS_BACK)
                .truncatedTo(ChronoUnit.HOURS)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        // dacă am depășit "acum", o luăm de la capăt, de la "acum - 3 luni"
        if (newPointer > nowMillis) {
            newPointer = threeMonthsAgoMillis;
        }

        currentPointerMillis = newPointer;
    }
}
