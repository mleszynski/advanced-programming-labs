package encodeDecode;

import java.io.*;

/**
 * Class containing static methods used to read and write data from strings.
 */
public class IOHandler {
    /**
     * Static method that writes information to a string through an output stream.
     * @param str String object to be written to the output stream.
     * @param os OutputStream object to be used.
     * @throws IOException in case of IO failure.
     */
    public static void writeToString(String str, OutputStream os) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(os);
        BufferedWriter buffer = new BufferedWriter(writer);
        buffer.write(str);
        buffer.flush();
        os.close();
    }

    /**
     * Static method that reads information in from a given input stream.
     * @param is the input stream to read information from.
     * @return String representation of parsed information.
     * @throws IOException in case of IO failure.
     */
    public static String readFromString(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(is);
        char[] buffer = new char[1024];
        int len;
        while ((len = reader.read(buffer)) > 0) {
            builder.append(buffer, 0, len);
        }
        return builder.toString();
    }
}
