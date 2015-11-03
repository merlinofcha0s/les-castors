package fr.castor.dto.helper;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import fr.castor.dto.AbstractDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Aide à la deserialization des DTOs en JSON
 *
 * @author Casaucau Cyril
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
                    return new Date(json.getAsLong());
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

    /**
     * Méthode générique d'aide à la désérialization des DTOs
     *
     * @param json  La String JSON contenant les datas
     * @param clazz Le type de classe
     * @param <T>   Le type generics de l'objet qui doit étendre abtract DTO
     * @return L'objet serializé a partir du JSONs
     */
    public static <T extends AbstractDTO> T deserializeDTO(String json, Class<T> clazz) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, clazz);
    }

    /**
     * Méthode générique d'aide à la désérialization de liste de DTOs
     *
     * @param json la string json contenant la liste d'objet
     * @param <T>  Le type generics de l'objet qui doit étendre abstract DTO
     * @param <V>  Le type de list generic
     * @return la liste créée à partir du JSON.
     */
    public static <V extends List<T>, T> List<T> deserializeDTOList(String json, TypeToken<V> token) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, token.getType());
    }
}
