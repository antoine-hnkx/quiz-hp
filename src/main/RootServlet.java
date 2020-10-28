package main;

import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RootServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (req.getRequestURI().contentEquals("/")) {
			Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, Paths.get("./views/main.html"));
		} else {
			try {
				Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_OK, Paths.get("public" + req.getRequestURI()));
			} catch (Exception e) {
				Utils.sendResponse(resp, Utils.ENCODING, HttpServletResponse.SC_NOT_FOUND, "<h1>Ressource Not Found 404</h1>", Utils.MIME_HTML);
			}
		}
	}
}
