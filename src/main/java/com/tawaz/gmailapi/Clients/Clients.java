package com.tawaz.gmailapi.Clients;
import com.google.api.client.util.ArrayMap;
import com.google.api.services.admin.directory.model.UserName;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;




public class Clients {


    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main1(String[] args) throws Exception {

        Clients obj = new Clients();

        try {
//            System.out.println("Testing 1 - Send Http GET request");
//            obj.sendGet();

//            System.out.println("Testing 2 - Send Http POST request... Returned: "+obj.sendPost("N0125698X"));
            obj.createGmail("n0125698x","Tawanda","Muyambo","p@ssW0rd","male");

        } finally {
            obj.close();
        }
    }

    private void close() throws IOException {
        httpClient.close();
    }

    private void sendGet() throws Exception {

        HttpGet request = new HttpGet("https://www.google.com/search?q=mkyong");

        // add request headers
        request.addHeader("custom-key", "mkyong");
        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        }

    }

    private String sendPost(String studentNumber) throws Exception {

        HttpPost post = new HttpPost("https://apply.nust.ac.zw/credentials/tutorials/"+studentNumber);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            String output=EntityUtils.toString(response.getEntity());
            JSONObject jsonObj = new JSONObject(output);
            String password = jsonObj.getString("password");
            return password;
        }

    }

    private int createGmail(String studentNumber,String name, String surname, String password, String gender) throws Exception{

        String payload = "{\n" +
                "  \"kind\": \"admin#directory#user\",\n" +
                "  \"primaryEmail\": \""+studentNumber+"@students.nust.ac.zw\",\n" +
                "  \"name\": {\n" +
                "    \"givenName\": \""+name+"\",\n" +
                "    \"familyName\": \""+surname+"\",\n" +
                "    \"fullName\": \""+name+" "+surname+"\",\n" +
                "  },\n" +
                "  \"isAdmin\": false,\n" +
                "  \"isDelegatedAdmin\": false,\n" +
                "  \"agreedToTerms\": false,\n" +
                "  \"suspended\": false,\n" +
                "  \"password\": \""+password+"\",\n" +
                "  \"archived\": false,\n" +
                "  \"changePasswordAtNextLogin\": false,\n" +
                "  \"ipWhitelisted\": false,\n" +
                "  \"emails\": [\n" +
                "    {\n" +
                "      \"address\": \""+studentNumber+"@students.nust.ac.zw\",\n" +
                "      \"primary\": true\n" +
                "    }\n" +
                "  ],\n" +
                "  \"gender\": {\n" +
                "    \"type\": \""+gender+"\"\n" +
                "  },\n" +
                "  \"orgUnitPath\": \"/zw/ac/nust/STUDENTS OU\",\n" +
                "  \"isMailboxSetup\": true,\n" +
                "  \"isEnrolledIn2Sv\": false,\n" +
                "  \"isEnforcedIn2Sv\": false,\n" +
                "  \"includeInGlobalAddressList\": true\n" +
                "}";

        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:8080/register");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
        return response.getStatusLine().getStatusCode();
    }


    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens_dir";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DirectoryScopes.ADMIN_DIRECTORY_USER);
    // private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_LABELS);
    //private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\Workstation\\Documents\\nuststudentsportal\\com.mycompany_studentsportalmaven_war_1.0-SNAPSHOT\\src\\main\\resources\\credentials.json";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Clients.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("tawanda.muyambo@nust.ac.zw");
    }

    public static void main2(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Print the labels in the user's account.
        String user = "me";
        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
    }



    public static void main3(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Directory service = new Directory.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Print the first 10 users in the domain.
        Users result = service.users().list()
                .setCustomer("my_customer")
                .setMaxResults(10)
                .setOrderBy("email")
                .execute();
        List<User> users = result.getUsers();
        if (users == null || users.size() == 0) {
            System.out.println("No users found.");
        } else {
            System.out.println("Users:");
            for (User user : users) {
                System.out.println(user.getName().getFullName());
            }
        }
    }


    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Directory service = new Directory.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        String email= "testStudent@students.nust.ac.zw";
        String firstName="New";
        String lastName="User";
        String password="Abcd@1234";
        String kind= "admin#directory#user";


        User user=new User();

        //create object of Type Username
        UserName userName=new UserName();
        userName.setGivenName(firstName);
        userName.setFamilyName(lastName);
        userName.setFullName(userName.getGivenName()+" "+userName.getFamilyName());

        //Set user attributes
        user.setKind(kind);
        user.setPrimaryEmail(email);
        user.setName(userName);
        user.setIsAdmin(false);
        user.setIsDelegatedAdmin(false);
        user.setAgreedToTerms(false);
        user.setSuspended(false);
        user.setPassword(password);
        user.setChangePasswordAtNextLogin(false);
        user.setIpWhitelisted(false);

        //set Emails
        List<ArrayMap<String, Object>> myList=new ArrayList<>();
        ArrayMap<String, Object> arrayMap=new ArrayMap<>();
        arrayMap.put("address",email);
        arrayMap.put("primary", true);
        myList.add(arrayMap);
        user.setEmails(myList);


        //insert user to gmail
        User newuser= service.users().insert(user).execute();

        if (newuser == null || newuser.size() == 0) {
            System.out.println("Failed to create user");
        } else {
            System.out.println(" Successfully created Gmail user: "+newuser.getPrimaryEmail());
        }
    }





}
