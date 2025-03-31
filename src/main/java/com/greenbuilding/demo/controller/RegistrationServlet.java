package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.RegistrationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private RegistrationDAO registrationDAO;
    public void init(){
        registrationDAO = new RegistrationDAO();
    }
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
//        try {
//            switch (action) {
//                case "new":
//                    showNewForm(request, response);
//
//                    break;
//                case "add":
//                    addEditCategory(request, response);
//
//                    break;
//                case "edit":
//                    showEditForm(request, response);
//
//                    break;
//                case "delete":
//                    deleteCategory(request, response);
//
//                    break;
//                default:
//                    listCategories(request, response);
//            }
//        } catch (Exception ex) {
//            throw new ServletException(ex);
//        }
//    }
}
