package encodeDecode;

import com.google.gson.Gson;
import request.*;
import result.*;

/**
 * Class which contains all of the static methods used to write request and
 * result object data in JSON format.
 */
public class Encoder {
    public Encoder() {}

    /**
     * Converts a LoadRequest object to a JSON string.
     * @param request request to be converted.
     * @return string representing LoadRequest data in JSON format.
     */
    public static String encode(LoadRequest request) {
        return (new Gson()).toJson(request);
    }

    /**
     * Converts a LoginRequest object to a JSON string.
     * @param request request to be converted.
     * @return string representing LoginRequest data in JSON format.
     */
    public static String encode(LoginRequest request) {
        return (new Gson()).toJson(request);
    }

    /**
     * Converts a RegisterRequest object to a JSON string.
     * @param request request to be converted.
     * @return string representing RegisterRequest data in JSON format.
     */
    public static String encode(RegisterRequest request) {
        return (new Gson()).toJson(request);
    }

    /**
     * Converts an AllEventResult object to a JSON string.
     * @param result result to be converted.
     * @return string representing AllEventResult data in JSON format.
     */
    public static String encode(AllEventResult result) {
        return (new Gson()).toJson(result);
    }

    /**
     * Converts an AllPersonResult object to a JSON string.
     * @param result result to be converted.
     * @return string representing AllPersonResult data in JSON format.
     */
    public static String encode(AllPersonResult result) {
        return (new Gson()).toJson(result);
    }

    /**
     * Converts a ClearResult object to a JSON string.
     * @param result result to be converted.
     * @return string representing ClearResult data in JSON format.
     */
    public static String encode(ClearResult result) {
        return (new Gson()).toJson(result);
    }

    /**
     * Converts an EventResult object to a JSON string.
     * @param result result to be converted.
     * @return string representing EventResult data in JSON format.
     */
    public static String encode(EventResult result) {
        return (new Gson()).toJson(result);
    }

    /**
     * Converts a FillResult object to a JSON string.
     * @param result result to be converted.
     * @return string representing FillResult data in JSON format.
     */
    public static String encode(FillResult result) {
        return (new Gson()).toJson(result);
    }

    /**
     * Converts a LoadResult object to a JSON string.
     * @param result result to be converted.
     * @return string representing LoadResult data in JSON format.
     */
    public static String encode(LoadResult result) {
        return (new Gson()).toJson(result);
    }

    /**
     * Converts a LoginResult object to a JSON string.
     * @param result result to be converted.
     * @return string representing LoginResult data in JSON format.
     */
    public static String encode(LoginResult result) {
        return (new Gson()).toJson(result);
    }

    /**
     * Converts a PersonResult object to a JSON string.
     * @param result result to be converted.
     * @return string representing PersonResult data in JSON format.
     */
    public static String encode(PersonResult result) {
        return (new Gson()).toJson(result);
    }

    /**
     * Converts a RegisterResult object to a JSON string.
     * @param result result to be converted.
     * @return string representing RegisterResult data in JSON format.
     */
    public static String encode(RegisterResult result) {
        return (new Gson()).toJson(result);
    }
}
