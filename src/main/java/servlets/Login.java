/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import database.tables.EditLibrarianTable;
import database.tables.EditStudentsTable;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import mainClasses.JSON_Converter;

import mainClasses.Student;
import mainClasses.Librarian;

/**
 *
 * @author root
 */
public class Login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try ( PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet Login</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("loggedIn")!=null){
            response.setStatus(200);
            EditStudentsTable eut = new EditStudentsTable();
            EditLibrarianTable lib_table = new EditLibrarianTable();
            String username = (String) session.getAttribute("loggedIn");
            try{
                Student s=eut.StudentUserNameExists(username);
                Librarian libr = lib_table.LibrarianUserNameExists(username);
                if(s!=null){
                    response.getWriter().write(s.getUsername());
                } else if( libr !=null){
                    response.getWriter().write(libr.getUsername());
                }
            }catch(Exception e){
                System.err.println("Got an exception while getting loggedIn user.");
                System.err.println(e.getMessage());
                response.setStatus(500);
            }
        }else{
            response.setStatus(403);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession(true);
        
        JSON_Converter jc = new JSON_Converter();
        
        EditStudentsTable eut = new EditStudentsTable();
        EditLibrarianTable lib_table = new EditLibrarianTable();
        
        
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try{
            Student s=eut.databaseToStudent(username, password);
            Librarian libr = lib_table.databaseToLibrarian(username,password);
            if(libr == null && s == null){
                response.setStatus(403);
            }else{
                session.setAttribute("loggedIn",username);
                response.setStatus(200);
            }
        }catch(Exception e){
            System.err.println("Got an exception while Logining a user ");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
