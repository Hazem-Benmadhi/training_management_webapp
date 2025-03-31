package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.ParticipantDAO;
import com.greenbuilding.demo.entity.Participant;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/participants")
public class ParticipantServlet extends HttpServlet {
    private ParticipantDAO participantDAO;

    @Override
    public void init(){
        participantDAO = new ParticipantDAO();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(@org.jetbrains.annotations.NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "add":
                    addEditParticipant(request, response);

                    break;
                case "edit":
                    showEditForm(request, response);

                    break;
                case "delete":
                    deleteFormation(request, response);

                    break;
                default:
                    listParticipants(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void listParticipants(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("participant", participantDAO.findAll());
        RequestDispatcher dispatcher = request.getRequestDispatcher("Participant.jsp");
        dispatcher.forward(request, response);
    }

    public void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("addParticipant.jsp");
        dispatcher.forward(request,response);
    }

    public void addEditParticipant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Participant participant;
        String idP = request.getParameter("id");
        if(idP!=null && !idP.isEmpty()){
            int id = Integer.parseInt(idP);
            participant = participantDAO.findById(id);
        }
        else {
            participant = new Participant();
        }
        participant.setFirstName(request.getParameter("firstName"));
        participant.setLastName(request.getParameter("lastName")); // !!!!!!!! //
        participant.setEmail(request.getParameter("email"));
        participant.setTel(request.getParameter("tel"));

        participantDAO.saveOrUpdate(participant);
        response.sendRedirect("participants?action=list");
    }

    public void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idP = Integer.parseInt(request.getParameter("id"));
        Participant participant = participantDAO.findById(idP);
        request.setAttribute("participant",participant);
        request.getRequestDispatcher("addParticipant.jsp").forward(request,response)
        ;
    }

    public void deleteFormation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id= Integer.parseInt(request.getParameter("id"));
        participantDAO.delete(id);
        response.sendRedirect("participants?action=list");
    }
}
