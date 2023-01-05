package Servlets.UBoatServlets;

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

@WebServlet(name = "ReadyAnnounceServletUBoat", urlPatterns = "/ReadyAnnounceUBoat")
public class ReadyAnnounceServletUBoat extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        boolean running = false;
        try {
            userManager userManager = ServletUtils.getUserManager(getServletContext());
            String userName = SessionUtils.getUsername(request); // gets username
            boolean readyStatus = Boolean.parseBoolean(request.getParameter(READY_STATUS));//gets the user ready status
            String userType = request.getParameter(USER_TYPE);//gets the type of the user
            userManager.setReadyStatus(userType,userName,readyStatus ,"0");
            userManager.startCompetitionIfAllReady(userType,userName); // checks if all the contest allies and u boat announce ready
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("Ready Announce Succeeded!! ");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getCause().getMessage());//send back the issue caused to the problem

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException { processRequest(request, response);}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {processRequest(request, response);}

}


/*        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        boolean running = false;
        try {
            userManager userManager = ServletUtils.getUserManager(getServletContext());
            String userName = SessionUtils.getUsername(request); // gets username
            boolean readyStatus = Boolean.parseBoolean(request.getParameter(READY_STATUS));//gets the user ready status
            String userType = request.getParameter(USER_TYPE);//gets the type of the user
            String missionSize = request.getParameter(MISSION_SIZE);// S added
             userManager.setReadyStatus(userType,userName,readyStatus ,missionSize);// S added
             running =userManager.checkIfAllReadyToCompete(userType,userName);// S added
            if(running) {
                response.setStatus(HttpServletResponse.SC_OK); //if 200 is sent back EVERYONE is READY!
                out.println("Ready status of " + userType + " " + userName + " sets successfully to: " + readyStatus);
            }
            else{
                response.setStatus(HttpServletResponse.SC_CONTINUE);
                out.println("Successfully set to " + readyStatus + " waiting for other contestants...");
                System.out.println(" \n100 code printed");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getCause().getMessage());//send back the issue caused to the problem
            System.out.println(" \n"+e.getCause());
        }
    }*/