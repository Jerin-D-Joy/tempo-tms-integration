package com.siemens.internal.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class RestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> parseJson(String json) {
        Map<String, Object> data = new HashMap<>();
        try {
            final MapType type = objectMapper.getTypeFactory().constructMapType(
                    Map.class, String.class, Object.class
            );
            data.putAll(objectMapper.readValue(json, type));
        } catch (Exception ex) {
            LOGGER.error("Parsing JSON Failed : ", ex);
        }
        return data;
    }

    public JsonNode parseJsonAsJsonNode(String json) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (Exception ex) {
            LOGGER.error("#RestClient : #parseJsonAsJsonNode : # Error : ", ex);
        }
        return jsonNode;
    }

}
