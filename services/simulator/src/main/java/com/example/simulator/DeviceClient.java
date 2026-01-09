package com.example.simulator;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceClient {

    private final RestTemplate restTemplate;

    public DeviceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // DTO minimal – ne interesează doar id-ul
    public static class DeviceDto {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    /**
     * Ia toate device-urile din device-service și întoarce doar lista de ID-uri.
     */
    public List<Long> getAllDeviceIds() {
        try {
            // ⚠️ AICI modifici endpoint-ul dacă la tine este diferit, ex: "/api/devices"
            String url = "http://device-service:8082/devices";

            ResponseEntity<DeviceDto[]> response =
                    restTemplate.getForEntity(url, DeviceDto[].class);

            DeviceDto[] body = response.getBody();
            if (body == null) {
                return Collections.emptyList();
            }

            return Arrays.stream(body)
                    .map(DeviceDto::getId)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            // dacă pică request-ul, măcar nu dăm peste cap simulatorul
            System.err.println("[SIMULATOR] Failed to load devices from device-service: " + ex.getMessage());
            return Collections.emptyList();
        }
    }
}
