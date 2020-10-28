package main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    public static final String JWTSECRET = "secret_key";
    public static final String ENCODING = "UTF-8";

    public static final String MIME_JS = "application/javascript";
    public static final String MIME_JSON = "application/json";
    public static final String MIME_HTML = "text/html";
    public static final String MIME_CSS = "text/css";
    public static final String MIME_ICO = "image/x-icon";
    public static final String MIME_PNG = "image/png";
    public static final String MIME_JPEG = "image/jpeg";

    public static String getMimeType(Path path) {
        if(path.toString().endsWith(".js")) return MIME_JS;
        else if(path.toString().contains(".json")) return MIME_JSON;
        else if(path.toString().contains(".html")) return MIME_HTML;
        else if(path.toString().contains(".css")) return MIME_CSS;
        else if(path.toString().contains(".ico")) return MIME_ICO;
        else if(path.toString().contains(".png")) return MIME_PNG;
        else if(path.toString().contains(".jpg") || path.toString().contains(".jpeg")) return MIME_JPEG;
        else return "";
    }

    public static void sendResponse(HttpServletResponse resp, String encoding, int status, Path path) throws IOException {
        try {
            String mimeType = Utils.getMimeType(path);
            if(mimeType == Utils.MIME_PNG || mimeType == Utils.MIME_JPEG) {
                sendImage(resp, new File(path.toString()), mimeType);
            } else {
                resp.setContentType(mimeType);
                resp.setCharacterEncoding(encoding);
                resp.setStatus(status);
                resp.getWriter().write(new String(Files.readAllBytes(path)));
            }
        } catch (IOException e) {
            byte[] msgBytes = e.getMessage().getBytes(Utils.ENCODING);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType(Utils.MIME_HTML);
            resp.setContentLength(msgBytes.length);
            resp.setCharacterEncoding(Utils.ENCODING);
            resp.getOutputStream().write(msgBytes);
        }
    }

    private static void sendImage(HttpServletResponse resp, File image, String mimeType) {
        resp.setContentType(mimeType);
        resp.setHeader("Content-Length", String.valueOf(image.length()));
        try {
            Files.copy(image.toPath(), resp.getOutputStream());
        } catch(IOException e) {}
    }

    public static void sendResponse(HttpServletResponse resp, String encoding, int status, String content, String mimeType) throws IOException {
        try {
            resp.setContentType(mimeType);
            resp.setCharacterEncoding(encoding);
            resp.setStatus(status);
            //resp.setHeader("Content-Length", String.valueOf(content.length()));
            resp.getWriter().write(content);
        } catch (IOException e) {
            byte[] msgBytes = e.getMessage().getBytes(Utils.ENCODING);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType(Utils.MIME_HTML);
            resp.setContentLength(msgBytes.length);
            resp.setCharacterEncoding(Utils.ENCODING);
            resp.getOutputStream().write(msgBytes);
        }
    }
}
