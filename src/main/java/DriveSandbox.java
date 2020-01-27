import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DriveSandbox {

    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final NetHttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JacksonFactory.getDefaultInstance(), AuthorizationHelper.authorize())
            .setApplicationName(APPLICATION_NAME)
            .build();

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
            .setPageSize(10)
            .setFields("nextPageToken, files(id, name)")
            .execute();
        var files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }
}
