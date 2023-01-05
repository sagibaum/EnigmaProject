package Servlets.AgentServlets;

import DataBase.Agent;
import DataBase.Allies;
import ServerDTOs.Agent.AgentDTO;
import UserManager.userManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AgentDataServlet", urlPatterns = "/agentData")
public class AgentDataServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML

        String agentUserName = SessionUtils.getUsername(request); // gets uboat user name

        response.setContentType("application/json"); //returning type is JSON

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            AgentDTO pack;
            userManager userManager = ServletUtils.getUserManager(getServletContext());

            Agent thisAgent = userManager.getRelevantAgentFromUserName(agentUserName); //get this agent saved in server
            if (thisAgent!=null) {
                Allies relevantAlly = userManager.getRelevantAllyFromAllyName(thisAgent.getAlliesTeam()); //get ally of this agent

                pack = new AgentDTO(thisAgent.getMissionSize(), relevantAlly.getAllyName(),
                        relevantAlly.getBattlefield(),
                        relevantAlly.getUboatUsername(),
                        relevantAlly.isInProgress(),
                        relevantAlly.getContestDifficulty(),
                        relevantAlly.getAlliesNeeded().toString(),
                        relevantAlly.getAlliesJoined().toString(),
                        thisAgent.getThreadNumber(),
                        thisAgent.getUserName(), thisAgent.isBelongToContest()
                        , thisAgent.getAllyDisconnected());
            }
            else pack=null;
            String json = gson.toJson(pack);
            out.println(json);
            out.flush();
        }
    }

}

