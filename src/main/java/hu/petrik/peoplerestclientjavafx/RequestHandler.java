package hu.petrik.peoplerestclientjavafx;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

    public final class RequestHandler {
        private RequestHandler() {
        }

        public static Response get(String url) throws IOException {
            HttpURLConnection connection = setupConnection(url);
            connection.setRequestMethod("GET");
            return getResponse(connection);
        }

        public static Response put(String url, String data) throws IOException {
            HttpURLConnection connection = setupConnection(url);
            connection.setRequestMethod("PUT");
            addRequestBody(connection, data);
            return getResponse(connection);
        }

        public Response delete(String url) throws IOException {
            HttpURLConnection connection = setupConnection(url);
            connection.setRequestMethod("DELETE");
            return getResponse(connection);
        }

        public static Response post(String url, String data) throws IOException {
            HttpURLConnection connection = setupConnection(url);
            connection.setRequestMethod("POST");
            addRequestBody(connection, data);
            return getResponse(connection);
        }

        private static HttpURLConnection setupConnection(String url) throws IOException {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("Accept", "application/json");
            return connection;
        }

        private static void addRequestBody(HttpURLConnection connection, String data) throws IOException {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();
        }

        private static Response getResponse(HttpURLConnection connection) throws IOException {
            int responseCode = connection.getResponseCode();
            InputStream is = null;
            if (responseCode >= 400) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            while (line != null) {
                stringBuilder.append(line).append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            is.close();
            String content = stringBuilder.toString().trim();
            return new Response(responseCode, content);
        }
    }
