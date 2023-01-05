package Servlets.AlliesServlets;


import DataBase.Agent;
import DataBase.UBoat;
import ServerDTOs.Allies.AlliesDTO;
import ServerDTOs.Allies.ContestsDTO;
import UserManager.userManager;
import Utils.ServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static Constans.ConstantsServer.USER_NAME;

@WebServlet(name = "AlliesDataServlet", urlPatterns = "/alliesdata")
public class AlliesDataServlet extends HttpServlet {

    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        String usernameFromParameter = request.getParameter(USER_NAME); //get the allies team name

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            userManager userManager = ServletUtils.getUserManager(getServletContext());
            boolean uBoatAbandon=  false;
            List<Agent> emptyAgents = new ArrayList<>();
            List<ContestsDTO> contests = new ArrayList<>();
            List<Agent> agents = userManager.getAlliesAvailable().
                    get(usernameFromParameter).
                    getThisTeamsAgents().
                    isEmpty() ?
                    emptyAgents
                    : userManager.getAlliesAvailable().
                    get(usernameFromParameter).getThisTeamsAgents();


            ArrayList<UBoat> uBoats = new ArrayList<>(userManager.getUBoatAvailable().values());
            for (UBoat uboat: uBoats) {
                if(uboat.isValidFile()) {
                    ContestsDTO contestsDTO = new ContestsDTO(uboat);
                    contests.add(contestsDTO);
                }
            }

            for(String bName:userManager.getBattleFieldAbandoned())
                if(userManager.getAlliesAvailable().get(usernameFromParameter).getBattlefield().equals(bName)) {
                    uBoatAbandon = true;
                }
            //pack receives either empty lists or lists with something
            AlliesDTO pack = new AlliesDTO(agents, contests,uBoatAbandon);
            //all contests available , also checks if everyone is ready

            String json = gson.toJson(pack);
            out.println(json);
            out.flush();
        }
    }

}