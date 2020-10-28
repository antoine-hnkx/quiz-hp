package main;

import com.auth0.jwt.JWTVerifier;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class QuestionsServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String token = req.getHeader("Authorization");
            if (token != null) {
                Object userID = null;
                Map<String, Object> decodedPayload = new JWTVerifier(Utils.JWTSECRET).verify(token);
                userID = decodedPayload.get("id");

                if (userID != null) {
                    Path path = Paths.get("./data/questions.json");
                    String questions = new String(Files.readAllBytes(path));
                    String respJson = "{\"success\":\"true\", \"data\":" + questions + "}";
                    Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, respJson, Utils.MIME_JSON);
//                    resp.setContentType("application/json");
//                    resp.setCharacterEncoding("UTF-8");
//                    resp.setStatus(200);
//                    resp.getWriter().write(respJson);
                }
            } else {
                String respJson = "{\"success\":\"false\", \"error\":\"Unauthorized: this ressource can only be accessed with a valid token.\"";
                Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respJson, Utils.MIME_JSON);
            }
        } catch(Exception e) {
            String respJson = "{\"success\":\"false\", \"error\":" + e.getMessage() + "}";
            Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respJson, Utils.MIME_JSON);
        }
    }
}
