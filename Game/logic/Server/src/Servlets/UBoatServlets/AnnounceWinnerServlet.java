package Servlets.UBoatServlets;

import DataBase.Agent;
import DataBase.Allies;
import DataBase.UBoat;
import UserManager.userManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static Constans.ConstantsServer.FILENAME;
import static Constans.ConstantsServer.WINNER_ALLY;

@WebServlet(name = "AnnounceWinnerServlet", urlPatterns = "/winnerAnnounce")
public class AnnounceWinnerServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {

            userManager userManager = ServletUtils.getUserManager(getServletContext());
            String UBoatName = SessionUtils.getUsername(request); // gets uboat user name
            String allyWins  = request.getParameter(WINNER_ALLY);

            synchronized (this) {
                UBoat uBoat = userManager.getUBoatAvailable().get(UBoatName);
                uBoat.setReadyToCompete(false);
                uBoat.setRunning(false);

                for (Allies ally : uBoat.getAlliesAssociated()) {
                    if (ally.getAllyName().equals(allyWins)) {
                        ally.setWinner(allyWins);
                        ally.stopCreatingMissions();
                        ally.setInProgress(false);
                        ally.setReadyToCompete(false);
                    }
                    else {
                        ally.setWinner(allyWins);
                        ally.stopCreatingMissions();
                        ally.setInProgress(false);
                        ally.setReadyToCompete(false);
                    }
                    if(!ally.getAgentsInHold().isEmpty()) // adding agent that registered will running contest
                            ally.moveAgentsFromHoldListToAgentsList();

                   //ally.clearAgentsData();//clearing all candidates' data of this ally agents
                }
            }
            //here we will need to update all the associated allys that the competition has ended
            //-----------here-------------------------------//
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) { // invalid file !
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getCause().getMessage());//send back the issue caused to the problem
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

}
