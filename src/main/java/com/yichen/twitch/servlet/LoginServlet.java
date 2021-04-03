package com.yichen.twitch.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yichen.twitch.db.MySQLConnection;
import com.yichen.twitch.db.MySQLException;
import com.yichen.twitch.entity.LoginRequestBody;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read user data from the request body
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestBody body = mapper.readValue(request.getReader(), LoginRequestBody.class);
        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String username;
        MySQLConnection conn = null;
        try {
            // Verify if the user ID and password are correct
            conn = new MySQLConnection();
            String userId = body.getUserId();
            String password = ServletUtil.encryptPassword(userId, body.getPassword());
            username = conn.verifyLogin(userId, password);
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            conn.close();
        }

        // Create a new session for the user if user ID and password are correct, otherwise return Unauthorized error
        if (!username.isEmpty()) {
            // Create a new session, put user ID as an attribute into the session object, and the expiration time to 600 seconds
            HttpSession session = request.getSession();
            session.setAttribute("user_id", body.getUserId());
            session.setMaxInactiveInterval(600);

            LoginRequestBody loginRequestBody = new LoginRequestBody(body.getUserId(), username);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(loginRequestBody));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
