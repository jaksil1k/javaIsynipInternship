import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class Task2 {

    private static final int CONNECTION_TIMEOUT = 5000;

    public static void main(String[] args) throws IOException {

        String command  = "", text = "";

        boolean isE = false, isS = false;
        for (String s : args){
            if (s.equals("-e")){
                isE = true;
            }else if (s.equals("-s")){
                isS = true;
            }else {
                if (isE){
                    command = s;
                } else if (isS){
                    text = s;
                } else {
                    throw new IOException("unknown command");
                }
            }
        }

        if (command.equals("add")) {
            sendRequest(text);
        } else if (command.equals("search")){
            System.out.println(getInput(text));
        }

    }

    private static String getInput(String text) throws IOException {


        final URL url = new URL("http://localhost:9200/tak2/_search" + text);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();


        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setReadTimeout(CONNECTION_TIMEOUT);
        con.setDoOutput(true);

        BufferedWriter httpRequestBodyWriter =
                new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        httpRequestBodyWriter.write("{ \"text': \": \"" + text + "\" }");
        httpRequestBodyWriter.close();


        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }


    private static void sendRequest(String text) throws IOException {

        final URL url = new URL("http://localhost:9200/task2/_doc/2");
        final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("PUT");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        // Writing the post data to the HTTP request body
        BufferedWriter httpRequestBodyWriter =
                new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
        httpRequestBodyWriter.write("{ \"text': \": \"" + text + "\" }");
        httpRequestBodyWriter.close();

        // Reading from the HTTP response body
        Scanner httpResponseScanner = new Scanner(urlConnection.getInputStream());
        while(httpResponseScanner.hasNextLine()) {
            System.out.printf(httpResponseScanner.nextLine());
        }
        httpResponseScanner.close();
    }
}
