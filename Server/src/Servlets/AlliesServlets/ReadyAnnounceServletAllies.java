package Servlets.AlliesServlets;

import UserManager.userManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static Constans.ConstantsServer.*;

@WebServlet(name = "ReadyAnnounceServletAllies", urlPatterns = "/ReadyAnnounceServletAllies")
public class ReadyAnnounceServletAllies extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        boolean running = false;
        try {
            userManager userManager = ServletUtils.getUserManager(getServletContext());

            String userName = SessionUtils.getUsername(request); // gets username
            boolean readyStatus = Boolean.parseBoolean(request.getParameter(READY_STATUS));//gets the user ready status
            String userType = request.getParameter(USER_TYPE);//gets the type of the user
            if(userType.equals(ALLIES)) {
                userName = userManager.getAlliesAvailable().get(request.getParameter(USER_NAME)).getAllyName();
            }
            String missionSize = request.getParameter(MISSION_SIZE);// S added
            userManager.setReadyStatus(userType,userName,readyStatus ,missionSize);
            userManager.startCompetitionIfAllReady(userType,userName);//checks if all the contest allies and u boat announce ready
            response.setStatus(HttpServletResponse.SC_OK); //if 200 is sent back  Ally status changed successfully
            out.println("Ready status of " + userType + " " + userName + " sets successfully to: " + readyStatus);
            //here if everyone is ready the server must create a new THREAD to create MISSION
            //and push them to RELEVANT ALLY's mission QUEUE (using mission creator , and consumer )
            //SERVER MUST BE AVAILABLE TO HTTP REQUESTS FROM CLIENT!!!!!!!!!!!!!!!!!
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            //System.out.println(e);
            out.print(e.getCause().getMessage());//send back the issue caused to the problem
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);}
}