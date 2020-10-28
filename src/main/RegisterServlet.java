package main;

import com.auth0.jwt.JWTSigner;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class RegisterServlet extends HttpServlet {

    private static Genson genson = new GensonBuilder().withConverters(new UserConverter()).create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Map<String, Object> user = genson.deserialize(req.getReader(), Map.class);

            if (!registerUser(user.get("email").toString(), user.get("password").toString(),user.get("firstName").toString(),user.get("lastName").toString())) {
                String respJson = "{\"success\":\"false\", \"error\":\"Email already exists\"}";
                Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
            } else {
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", UUID.randomUUID().toString());

                String token = new JWTSigner(Utils.JWTSECRET).sign(claims);
                String respJson = "{\"success\":\"true\", \"token\":\""+ token + "\"}";
                Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
            }

        } catch (Exception e) {
            String respJson = "{\"success\":\"false\", \"error\":" + e.getMessage() + "}";
            Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respJson, Utils.MIME_JSON);
        }
    }

    private boolean registerUser(String email, String password,String firstName,String lastName) throws IOException {
        Path path = Paths.get("./data/users.json");
        List<User> users;
        int id;

        if (Files.exists(path)) {
            String json = new String(Files.readAllBytes(path));
            users = genson.deserialize(json, new GenericType<List<User>>() {});
            id = users.get(users.size() - 1).getId() + 1;
        } else {
            users = new ArrayList<>();
            id = 1;
        }

        // if user (email) already exists
        for (User user : users)
            if(user.getEmail().equals(email))
                return false;
        users.add(new User(id, email, password,firstName,lastName));
        String json = genson.serialize(users);
        Files.write(path, json.getBytes());
        return true;
    }
}
