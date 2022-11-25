import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Task2 {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private static final int CONNECTION_TIMEOUT = 5000;
    private static int id = 1;

    public static void main(String[] args) throws IOException, InterruptedException {

        String command  = "", text = "";
        boolean isE = false, isS = false;
        for (String s : args){
            if (s.equals("-e")){
                isE = true;
            }else if (s.equals("-s")){
                isS = true;
            }else {
                //System.out.println(s + " " +  isE + " "+  isS);
                if (isE){
                    //System.out.println(s);
                    command = s;
                    isE = false;
                } else if (isS){
                    text = s;
                    isS = false;
                } else {
                    throw new IOException("incorrect input");
                }
            }
        }

        System.out.println(text);

        if (command.equals("add")) {
            sendRequest(text);
        } else if (command.equals("search")){
            getInput(text);
        } else {
            throw new IOException("unknown command");
        }

    }


//    private static void sendInput (String text) throws IOException {
//        final URL url = new URL("http://localhost:9200/task2/_seacrh?pretty");
//        final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setDoOutput(true);
//        urlConnection.setRequestMethod("POST");
//        urlConnection.setRequestProperty("Content-Type", "application/json");
//
//        // Writing the post data to the HTTP request body
//        BufferedWriter httpRequestBodyWriter =
//                new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
//        httpRequestBodyWriter.write("{ \"text': \": \"" + text + "\" }");
//        httpRequestBodyWriter.close();
//
//        // Reading from the HTTP response body
//        try (final BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
//            String inputLine;
//            final StringBuilder content = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            System.out.println(content.toString());
//        } catch (final Exception ex) {
//            ex.printStackTrace();
//            System.out.println("");
////        }
//        }
//    }

    private static void getInput(String text) throws IOException, InterruptedException {


        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:9200/_search"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());
    }


    private static void sendRequest(String text) throws IOException {

        final URL url = new URL("http://localhost:9200/task2/_doc/" + id);
        id++;
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
