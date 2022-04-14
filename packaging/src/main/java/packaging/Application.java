package packaging;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Application {
    private static final String SHA_256 = "SHA-256";
    private static final String JAR_LOCATION = "app/target/app-1.0.jar";
    private static final String REPO_ROOT = "./sfp.jar";
    private static final String README_LOCATION = "./README.md";

    private static final String VIRUS_TOTAL_API_KEY_VAR = "VIRUS_TOTAL_API_KEY";
    private static final String VIRUS_TOTAL_SCAN_API = "https://www.virustotal.com/api/v3/files";

    private static final String VIRUS_TOTAL_URL = "https://www.virustotal.com/gui/file/";
    private static final String VIRUS_TOTAL_DETECTION = "/detect";
    private static final String VIRUS_TOTAL_DETAILS = "/details";

    public static void main(String[] args) {
        try {
            String jarHash = computeJarHash();

            moveJar();

            boolean scanResult = scanJar(jarHash);

            updateReadme(jarHash, scanResult);
        } catch (IOException | NoSuchAlgorithmException | InterruptedException e) {
            System.out.println(createMessage(e));
            e.printStackTrace();
        }
    }

    private static String computeJarHash() throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
        Path originalPath = Path.of(JAR_LOCATION);
        return hexToString(messageDigest.digest(Files.readAllBytes(originalPath)));
    }

    private static void moveJar() throws IOException {
        Files.copy(Path.of(JAR_LOCATION), Path.of(REPO_ROOT), StandardCopyOption.REPLACE_EXISTING);
    }

    private static boolean scanJar(String jarHash) throws IOException, InterruptedException {
        System.out.println("Started file scanning");

        String apiKey = System.getenv(VIRUS_TOTAL_API_KEY_VAR);
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException(VIRUS_TOTAL_API_KEY_VAR + " not set");
        }

        HttpClient httpClient = HttpClient.newHttpClient();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("file", Path.of(REPO_ROOT));

        String boundary = new BigInteger(256, new Random()).toString();

        HttpRequest scanRequest = HttpRequest.newBuilder(URI.create(VIRUS_TOTAL_SCAN_API))
                .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                .header("X-APIKEY", apiKey)
                .POST(MultipartAdapter.ofMimeMultipartData(data, boundary))
                .build();

        HttpResponse<String> scanResponse = httpClient.send(scanRequest, HttpResponse.BodyHandlers.ofString());

        if (scanResponse.statusCode() != 200) {
            System.out.println("File scanning failed: " + scanResponse.body());
            return false;
        }

        System.out.println("Finished file scanning");

        // trigger GUI analysis page in order to have it loaded for further checks
        httpClient.send(HttpRequest.newBuilder(URI.create(VIRUS_TOTAL_URL + jarHash + VIRUS_TOTAL_DETECTION))
                .GET().build(), HttpResponse.BodyHandlers.ofString());

        return true;
    }

    private static void updateReadme(String jarHash, boolean scanResult) {
        try (RandomAccessFile raf = new RandomAccessFile(Path.of(README_LOCATION).toFile(), "rw")) {
            byte b;
            long length = raf.length() - 1;

            do {
                length -= 1;
                raf.seek(length);
                b = raf.readByte();
            } while (b != 10);

            String jarInfo = jarHash;

            if (scanResult) {
                jarInfo = String.format("[%s](%s%s%s)", jarHash, VIRUS_TOTAL_URL, jarHash, VIRUS_TOTAL_DETAILS);
            }

            raf.write(jarInfo.getBytes());
            System.out.println("Moved jar and updated README");
        } catch (IOException e) {
            System.out.println(createMessage(e));
            e.printStackTrace();
        }
    }

    private static String createMessage(Exception ex) {
        if (ex instanceof IOException) {
            return "Error occurred while copying jar into repository root";
        } else if (ex instanceof NoSuchAlgorithmException) {
            return SHA_256 + " hashing algorithm was not found";
        }

        return "An unknown exception occurred";
    }

    private static String hexToString(byte[] content) {
        StringBuilder builder = new StringBuilder();

        for (byte b : content) {
            builder.append(String.format("%02X", b));
        }

        return builder.toString();
    }
}