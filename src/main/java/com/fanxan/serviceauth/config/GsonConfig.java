package com.fanxan.serviceauth.config;

import com.nimbusds.jose.shaded.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GsonConfig {
    private GsonConfig() {
    }

    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, (JsonSerializer<ZonedDateTime>) (zonedDateTime, type, context) -> new JsonPrimitive(zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .registerTypeAdapter(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
                    @Override
                    public ZonedDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
                        return ZonedDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    }
                })
                .create();
    }
}