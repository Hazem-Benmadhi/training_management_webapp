package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.DomainDAO;
import com.greenbuilding.demo.dao.FormationDAO;
import com.greenbuilding.demo.dao.TrainerDAO;
import com.greenbuilding.demo.entity.Domain;
import com.greenbuilding.demo.entity.Formation;
import com.greenbuilding.demo.entity.Trainer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@WebServlet("/formations")
public class FormationServlet extends HttpServlet {

    private FormationDAO formationDAO;
    private DomainDAO domainDAO;
    private TrainerDAO trainerDAO;

    @Override
    public void init(){
        formationDAO = new FormationDAO();
        domainDAO = new DomainDAO();
        trainerDAO = new TrainerDAO();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "add":
                    addEditFormation(request, response);

                    break;
                case "edit":
                    showEditForm(request, response);

                    break;
                case "delete":
                    deleteFormation(request, response);

                    break;
                default:
                    listFormations(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void listFormations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("formations", formationDAO.findAll());
        request.getRequestDispatcher("formations.jsp").forward(request, response);
    }

    public void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DomainDAO domainDAO = new DomainDAO();
        List<Domain> domains = domainDAO.findAll();

        request.setAttribute("domains",  domains);
        System.out.println("Domains found: " + domains);

        request.setAttribute("trainers",  trainerDAO.findAll());
        request.getRequestDispatcher("addFormation.jsp").forward(request,response);
    }

    public void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idF = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("formation",formationDAO.findById(idF));
        request.setAttribute("domains",domainDAO.findAll());
        request.setAttribute("trainers",trainerDAO.findAll());
        request.getRequestDispatcher("addFormation.jsp").forward(request,response);
    }

    public void addEditFormation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Formation formation;
        String idFormation = request.getParameter("id");
        if(idFormation!=null && !idFormation.isEmpty()){
            int id = Integer.parseInt(idFormation);
            formation = formationDAO.findById(id);
        }
        else {
            formation= new Formation();
        }
        Domain domain = domainDAO.findById(Integer.parseInt(request.getParameter("idDomain")));
        Trainer trainer = trainerDAO.findById(Integer.parseInt(request.getParameter("idTrainer")));

        formation.setTitle(request.getParameter("title"));
        formation.setDate(LocalDate.parse(request.getParameter("date")));
        formation.setDuration(Integer.parseInt(request.getParameter("duration")));
        formation.setBudget(Double.parseDouble(request.getParameter("budget")));
        formation.setIddomain(domain);
        formation.setIdtrainer(trainer);

        formationDAO.saveOrUpdate(formation);
        response.sendRedirect("formations?action=list");
    }

    public void deleteFormation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id= Integer.parseInt(request.getParameter("id"));
        formationDAO.delete(id);
        response.sendRedirect("formations?action=list");
    }

}
