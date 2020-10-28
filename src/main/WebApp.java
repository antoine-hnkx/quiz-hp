package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.http.HttpServlet;

public class WebApp {
    public static void main(String[] args) throws Exception {
        //1
        Server server = new Server(8080);
        WebAppContext context = new WebAppContext();

        //2
        context.setContextPath("/");
        context.setResourceBase("public");
        context.setInitParameter("cacheControl", "no-store,no-cache,must-revalidate"); // to enable dynamic changes to js files without restarting

        //3
        HttpServlet rootServlet = new RootServlet();
        context.addServlet(new ServletHolder(rootServlet), "/");

        HttpServlet loginServlet = new LoginServlet();
        context.addServlet(new ServletHolder(loginServlet), "/login");

        HttpServlet registerServlet = new RegisterServlet();
        context.addServlet(new ServletHolder(registerServlet), "/register");

        HttpServlet houseServlet = new HouseServlet();
        context.addServlet(new ServletHolder(houseServlet), "/house/*");

        HttpServlet questionsServlet = new QuestionsServlet();
        context.addServlet(new ServletHolder(questionsServlet), "/questions");

        HttpServlet scoreServlet = new ScoreServlet();
        context.addServlet(new ServletHolder(scoreServlet), "/score/*");


        //4
        server.setHandler(context);
        server.start();
    }
}