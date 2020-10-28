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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScoreServlet extends HttpServlet {

    private static Genson genson = new Genson();
    private static Genson gensonUser = new GensonBuilder().withConverters(new UserConverter()).create();

    private Map<String, Map<String, Integer>> scores = new HashMap(); // userEmail -> houseName -> points

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String token = req.getHeader("Authorization");
            if (token != null) {
                Object userID = null;
                Map<String, Object> decodedPayload = new JWTVerifier(Utils.JWTSECRET).verify(token);
                userID = decodedPayload.get("id");
                if (userID != null) {
                    Map<String, Object> params = genson.deserialize(req.getReader(), Map.class);
                    String userEmail = params.get("email").toString();
                    String houseName = getHouseNameFromURL(req.getRequestURI());

                    if (houseName.equals("reset")) {
                        resetUserScore(userEmail);
                        String respJson = "{\"success\":\"true\"}";
                        Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
                    } else {
                        giveOnePoint(userEmail, houseName);
                        int totalPoints = getNbQuestionsAnswered(userEmail);
                        if (totalPoints < 11) { // number of questions in questions.json
                            String respJson = "{\"success\":\"true\"}";
                            Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
                        } else {
                            String winningHouse = getHouseMaxPoints(userEmail);
                            setUserHouse(userEmail, winningHouse); // set the user's house in users.json
                            String respJson = "{\"success\":\"true\", \"house\":\"" + winningHouse + "\"}";
                            Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
                        }
                    }
                }
            } else {
                String respJson = "{\"success\":\"false\", \"error\":\"Unauthorized: this ressource can only be accessed with a valid token.\"";
                Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respJson, Utils.MIME_JSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String respJson = "{\"success\":\"false\", \"error\":" + e.getMessage() + "}";
            Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respJson, Utils.MIME_JSON);
        }
    }

    private void resetUserScore(String userEmail) {
        if (scores.containsKey(userEmail)) {
            scores.replace(userEmail, new HashMap<String, Integer>());
        } else {
            scores.put(userEmail, new HashMap<String, Integer>());
        }

    }

    private void setUserHouse(String userEmail, String winningHouse) throws IOException {

        Path path = Paths.get("./data/users.json");
        if (Files.exists(path)) {
            String json = new String(Files.readAllBytes(path));
            List<User> users = gensonUser.deserialize(json, new GenericType<List<User>>() {
            });

            for (User user : users) {
                if (user.getEmail().equals(userEmail)) {
                    user.setHouse(winningHouse);
                    break;
                }
            }

            json = gensonUser.serialize(users);
            Files.write(path, json.getBytes());
        }
    }

    private String getHouseMaxPoints(String userEmail) {
        String[] houses = {"gryffindor", "ravenclaw", "slytherin", "hufflepuff"};
        Map<String, Integer> userPoints = scores.get(userEmail);
        int gPoints = 0, rPoints = 0, sPoints = 0, hPoints = 0;
        if (userPoints.containsKey(houses[0])) gPoints = userPoints.get(houses[0]);
        if (userPoints.containsKey(houses[1])) rPoints = userPoints.get(houses[1]);
        if (userPoints.containsKey(houses[2])) sPoints = userPoints.get(houses[2]);
        if (userPoints.containsKey(houses[3])) hPoints = userPoints.get(houses[3]);

        if (gPoints >= rPoints && gPoints >= sPoints && gPoints >= hPoints) return houses[0];
        if (rPoints >= gPoints && rPoints >= sPoints && rPoints >= hPoints) return houses[1];
        if (sPoints >= gPoints && sPoints >= rPoints && sPoints >= hPoints) return houses[2];
        return houses[3];
    }

    private int getNbQuestionsAnswered(String userEmail) {
        Map<String, Integer> userPoints = scores.get(userEmail);
        int nbQuestionsAnswered = 0;
        if (userPoints.containsKey("gryffindor")) nbQuestionsAnswered += userPoints.get("gryffindor");
        if (userPoints.containsKey("ravenclaw")) nbQuestionsAnswered += userPoints.get("ravenclaw");
        if (userPoints.containsKey("slytherin")) nbQuestionsAnswered += userPoints.get("slytherin");
        if (userPoints.containsKey("hufflepuff")) nbQuestionsAnswered += userPoints.get("hufflepuff");
        return nbQuestionsAnswered;
    }

    private void giveOnePoint(String userEmail, String houseName) {
        if (!scores.containsKey(userEmail)) {
            scores.put(userEmail, new HashMap<String, Integer>());
        }
        Map<String, Integer> userPoints = scores.get(userEmail);
        if (userPoints.containsKey(houseName)) {
            userPoints.replace(houseName, userPoints.get(houseName) + 1);
        } else {
            userPoints.put(houseName, 1);
        }
    }

    private String getHouseNameFromURL(String url) {
        String[] pathParts = url.split("/");
        String houseName = "";
        if (url.length() > 2) houseName = pathParts[2];
        return houseName;
    }
}
