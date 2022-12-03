package encodeDecode;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;

/**
 * Contains static methods which are used to decode data from JSON format.
 */
public class Decoder {
    public Decoder() {}

    /**
     * Method used to decode general JSON commands.
     * @param input the JSON string to be parsed.
     * @param type the request/result object corresponding to the type of data
     *             we are reading in.
     * @return object of the class (type) which contains info from JSON string.
     * @param <T> class type.
     */
    public static <T> T decode(String input, Class<T> type) {
        return (new Gson()).fromJson(input, type);
    }

    /**
     * Specific method used to read in data from location JSON files for fill method.
     * @param inFile the JSON file to be read.
     * @return LocationList object that contains information read in from JSON file.
     * @throws IOException in case of file reading error.
     */
    public static LocationList decodeLocations(File inFile) throws IOException {
        try (FileReader fileReader = new FileReader(inFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            Gson myGson = new Gson();
            LocationList locations = myGson.fromJson(bufferedReader, LocationList.class);
            return locations;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

//    /**
//     * Specific method used to read in data from name JSON files for fill method.
//     * @param inFile the JSON file to be read.
//     * @return NamesList object that contains information read in from JSON file.
//     * @throws IOException in case of file reading error.
//     */
//    public static NamesList decodeNames(File inFile) throws IOException {
//        try (FileReader fileReader = new FileReader(inFile);
//            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
//            Gson myGson = new Gson();
//            NamesList names = myGson.fromJson(bufferedReader, NamesList.class);
//            return names;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new IOException();
//        }
//    }
    public static NamesList decodeNames(File filename) throws IOException {
        try(FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            NamesList nameList = gson.fromJson(bufferedReader, NamesList.class);
            return nameList;
        } catch(IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }
}
