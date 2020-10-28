package main;

import com.auth0.jwt.JWTVerifier;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HouseServlet extends HttpServlet {

    private static Genson genson = new GensonBuilder().withConverters(new UserConverter()).create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String token = req.getHeader("Authorization");
            if (token != null) {
                Object userID = null;
                Map<String, Object> decodedPayload = new JWTVerifier(Utils.JWTSECRET).verify(token);
                userID = decodedPayload.get("id");
                if (userID != null) {
                    String houseName = getHouseNameFromURL(req.getRequestURI());
                    Path path = Paths.get("./data/users.json");

                    if (Files.exists(path)) {

                        String json = new String(Files.readAllBytes(path));
                        List<User> users = genson.deserialize(json, new GenericType<List<User>>() {});
                        List<User> usersFromHouse = new ArrayList<>();

                        for (User user: users) {
                            if(user.getHouse().equals(houseName)) {
                                usersFromHouse.add(user);
                            }
                        }

                        String respJson = "{\"success\":\"true\", \"data\":" + genson.serialize(usersFromHouse) + "}";
                        Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
                    } else {
                        String respJson = "{\"success\":\"false\", \"error\":\"invalid house name\"}";
                        Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
                    }
                }
            } else {
                String respJson = "{\"success\":\"false\", \"error\":\"Unauthorized: this resource can only be accessed with a valid token.\"";
                Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respJson, Utils.MIME_JSON);
            }
        } catch(Exception e) {
            String respJson = "{\"success\":\"false\", \"error\":" + e.getMessage() + "}";
            Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respJson, Utils.MIME_JSON);
        }
    }

    private String getHouseNameFromURL(String url) {
        String[] pathParts = url.split("/");
        String houseName = "";
        if (url.length() > 2) houseName = pathParts[2];
        return houseName;
    }
}
