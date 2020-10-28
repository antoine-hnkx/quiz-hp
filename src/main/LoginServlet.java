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

public class LoginServlet extends HttpServlet {

    private static Genson genson = new GensonBuilder().withConverters(new UserConverter()).create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Map<String, Object> user = genson.deserialize(req.getReader(), Map.class);

            if (verifyCredentials(user.get("email").toString(), user.get("password").toString())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", UUID.randomUUID().toString());

                String token = new JWTSigner(Utils.JWTSECRET).sign(claims);
                String respJson = "{\"success\":\"true\", \"token\":\""+ token + "\"}";
                Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
            } else {
                String respJson = "{\"success\":\"false\", \"error\":\"Wrong email or password.\"}";
                Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
            }
        } catch (Exception e) {
            String respJson = "{\"success\":\"false\", \"error\":" + e.getMessage() + "}";
            Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respJson, Utils.MIME_JSON);
        }
    }

    private boolean verifyCredentials(String email, String password) throws IOException {
        Path path = Paths.get("./data/users.json");

        if (Files.exists(path)) {
            String json = new String(Files.readAllBytes(path));
            List<User> users = genson.deserialize(json, new GenericType<List<User>>() {});

            for (User user: users)
                if (user.getEmail().equals(email))
                    return user.getPassword().equals(password);
        }

        return false;
    }
}
