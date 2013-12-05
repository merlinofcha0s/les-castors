package fr.batimen.dto.helper;

import java.lang.reflect.Type;
import java.util.Date;

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

	private DeserializeJsonHelper() {

	}

	public static Gson createGsonObject() {

		GsonBuilder builder = new GsonBuilder().serializeNulls();

		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
				return new Date(json.getAsJsonPrimitive().getAsLong());
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

}
