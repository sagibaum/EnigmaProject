package Servlets.AlliesServlets;

import DataBase.Allies;
import DataBase.UBoat;
import UserManager.userManager;
import Utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static Constans.ConstantsServer.USER_NAME;

@WebServlet(name = "ChooseBattleFieldServlet", urlPatterns = "/chooseBattleField")
public class ChooseBattleFieldServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            userManager userManager = ServletUtils.getUserManager(getServletContext());

            String allyTeam = request.getParameter(USER_NAME);//gets the ally team that sent the message
            String selectedBattleField = request.getParameter("BattleField");//gets the battlefield chosen

            HashMap<String, UBoat> uBoatAvailable = userManager.getUBoatAvailable();
            HashMap<String, Allies> alliesAvailable = userManager.getAlliesAvailable();

            synchronized (this) {
                Allies currAlly = null;
                UBoat currUBoat = null;
                for (Allies ally : alliesAvailable.values()) {
                    if (ally.getAllyName().equals(allyTeam)) {
                        currAlly = ally;
                    }
                }
                for (UBoat uboat : uBoatAvailable.values()) {
                    if (uboat.getBattlefieldName().equals(selectedBattleField)) {
                        currUBoat = uboat;
                    }
                }

                if(currUBoat!=null && currAlly!= null) {
                    currUBoat.addAlly(currAlly);
                    currAlly.setBattlefield(currUBoat.getBattlefieldName());
                    currAlly.setUboatUsername(currUBoat.getUsername());
                    currAlly.setUnDecryptedSentence(currUBoat.getMessageBeforeUnencrypted());
                    currAlly.setContestDifficulty(currUBoat.getDifficulty());
                    currAlly.setAlliesNeeded(currUBoat.getAlliesNeededAmountInteger());
                    for (Allies ally: currUBoat.getAlliesAssociated()) {
                        currAlly.setAlliesJoined(currUBoat.getAlliesJoinedInteger());
                    }
                    currAlly.setMessageToDecrpyt(currUBoat.getEncryptMessage());
                    currAlly.clearAgentsData();//clearing all candidates' data of this ally agents
                    currAlly.setWinner("");
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else{
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    out.print("Could not add the ally to the uboat app");//send back the issue caused to the problem
                }

            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getCause().getMessage());//send back the issue caused to the problem
        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException { processRequest(request,response);}

}
