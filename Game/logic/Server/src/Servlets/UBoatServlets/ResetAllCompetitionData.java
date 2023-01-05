package Servlets.UBoatServlets;


import DataBase.Allies;
import DataBase.UBoat;
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

@WebServlet(name = "ResetAllCompetitionData", urlPatterns = "/resetContestData")
public class ResetAllCompetitionData extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {
            userManager userManager = ServletUtils.getUserManager(getServletContext());
            String UBoatName = SessionUtils.getUsername(request); // gets uboat user name
            UBoat uBoat = userManager.getUBoatAvailable().get(UBoatName);
            synchronized (this) {
                for (Allies ally : uBoat.getAlliesAssociated()) {
                        ally.setWinner("");
                        ally.setBattlefield("");
                        ally.setUboatUsername("");
                        ally.setContestDifficulty("");
                        ally.setMessageToDecrpyt("");
                        ally.setMissionSize(0);
                        ally.setAlliesNeeded(0);
                        ally.setAlliesJoined(0);
                    }
                }
            uBoat.setRunning(false);
            uBoat.getAlliesAssociated().clear();
            uBoat.setAlliesJoined(0);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) { //
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getCause().getMessage());//send back the issue caused to the problem
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

}
