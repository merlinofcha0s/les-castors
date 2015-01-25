package fr.batimen.dto.helper;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

/**
 * Aide à la deserialization des DTOs en JSON
 * 
 * @author Casaucau Cyril
 * 
 */
public class DeserializeJsonHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeserializeJsonHelper.class);

    private DeserializeJsonHelper() {

    }

    public static Gson createGsonObject() {

        GsonBuilder builder = new GsonBuilder().serializeNulls();

        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
                try {
                    return sdf.parse(json.getAsString());
                } catch (ParseException e) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("Erreur lors du parsing d'un date en JSON : " + json.getAsString(), e);
                        LOGGER.warn("Essai de parsing de la date : " + json.getAsString() + " en long!");
                    }
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            }
        });

        return builder.create();
    }

    public static int[] parseIntArray(String intArray) {

        Gson gson = createGsonObject();

        return gson.fromJson(intArray, int[].class);
    }

    public static Long parseLongNumber(String longNumber) {

        Gson gson = createGsonObject();

        return gson.fromJson(longNumber, Long.class);
    }

    public static String parseString(String string) {
        Gson gson = createGsonObject();
        return gson.fromJson(string, String.class);
    }

}
