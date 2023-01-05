package Servlets.AgentServlets;

import DataBase.Agent;
import DataBase.Allies;
import Mission.Mission;
import ServerDTOs.Agent.MissionDTO;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MissionDataServlet", urlPatterns = "/pullMissions")
public class MissionDataServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML

        String agentUserName = SessionUtils.getUsername(request); // gets allies user name

        response.setContentType("application/json"); //returning type is JSON

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();

            userManager userManager = ServletUtils.getUserManager(getServletContext());

            Agent thisAgent = userManager.getRelevantAgentFromUserName(agentUserName); //get this agent saved in server
            Allies relevantAlly = userManager.getRelevantAllyFromAllyName(thisAgent.getAlliesTeam()); //get ally of this agent

            List<Mission> missionsForAgent = new ArrayList<>();

            if(!relevantAlly.isQueueEmpty()) {
                for (int i = 0; i < thisAgent.getMissionSize(); i++) { //missions as specified in login
                    try {
                        Mission m = relevantAlly.getOneMission();
                        missionsForAgent.add(m);
                        if (m == null)
                            break; //in this case the mission queue is empty
                    } catch (InterruptedException e) {
                        //in this case the queue is empty
                        break; //no more missions leave the loop
                    }
                }
            }

            MissionDTO pack = new MissionDTO(relevantAlly.getMissionsProgress().getMissionsTotal(),
                    missionsForAgent.size(),
                    relevantAlly.getMissionsInQueueNumber(),
                    missionsForAgent,
                    relevantAlly.getMessageToDecrpyt());

            //System.out.println(pack);

            String json = gson.toJson(pack);
            out.println(json);
            out.flush();
        }
    }

}
