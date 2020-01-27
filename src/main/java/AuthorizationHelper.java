import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

public class AuthorizationHelper {

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

    public static Credential authorize() throws IOException, GeneralSecurityException {
        var in = ofNullable(SheetsSandbox.class.getResourceAsStream(CREDENTIALS_FILE_PATH))
            .map(InputStreamReader::new)
            .orElseThrow(() -> new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH));
        var clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), in);
        var flow = new GoogleAuthorizationCodeFlow.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(),
            clientSecrets,
            SCOPES
        ).setDataStoreFactory(new MemoryDataStoreFactory()).setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
}
