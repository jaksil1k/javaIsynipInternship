import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class Task2 {

    private static final int CONNECTION_TIMEOUT = 5000;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        String nextWord = "", nextText = "";
        boolean isE = false;
        for (int i = 0;i < text.length();++i){
            if (text.charAt(i) == '-'){
                i++;
                if (text.charAt(i) == 'e'){
                    i += 2;
                    nextWord = findWord(text, i);
                    isE = true;
                    i += nextWord.length();
                }
                else if (text.charAt(i) == 's'){
                    if (!isE){
                        throw new IOException("firstly -e <method> then -s <text>");
                    }
                    i += 2;
                    nextText = findText(text, i);
                    break;
                }
                else {
                    throw new IOException("no such command");
                }
            }
        }
        if (nextWord.equals("add")) {
            sendRequest(nextText);
        } else if (nextWord.equals("search")){
            System.out.println(getInput(nextText));
        }

    }

    private static String findText(String text, int i) {
        StringBuilder ans = new StringBuilder("");

        for (;i < text.length();i++){
            char a = text.charAt(i);
            ans.append(a);
        }

        return ans.toString();
    }

    private static String findWord(String text, int i) {
        StringBuilder ans = new StringBuilder("");
        for (;i < text.length();i++){
            char a = text.charAt(i);
            if (a == ' '){
                return ans.toString();
            }
            ans.append(a);
        }
        return ans.toString();
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
            System.out.println(httpResponseScanner.nextLine());
        }
        httpResponseScanner.close();
    }
}
