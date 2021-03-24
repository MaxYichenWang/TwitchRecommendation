package com.yichen.twitch.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yichen.twitch.db.MySQLConnection;
import com.yichen.twitch.db.MySQLException;
import com.yichen.twitch.entity.FavoriteRequestBody;
import com.yichen.twitch.entity.Item;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FavoriteServlet", urlPatterns = "/favorite")
public class FavoriteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user ID from request URL, this is a temporary solution since we don’t support session now
        String userId = request.getParameter("user_id");
        Map<String, List<Item>> itemMap;

        MySQLConnection connection = null;
        try {
            // Read the favorite items from database
            connection = new MySQLConnection();
            itemMap = connection.getFavoriteItems(userId);
            ServletUtil.writeItemMap(response, itemMap);
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user ID from request URL, this is a temporary solution since we don’t support session now
        String userId = request.getParameter("user_id");

        // Get favorite item information from request body
        ObjectMapper mapper = new ObjectMapper();
        FavoriteRequestBody body = mapper.readValue(request.getReader(), FavoriteRequestBody.class);
        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MySQLConnection connection = null;
        try {
            // Save the favorite item to database
            connection = new MySQLConnection();
            connection.setFavoriteItem(userId, body.getFavoriteItem());
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user ID from request URL, this is a temporary solution since we don’t support session now
        String userId = request.getParameter("user_id");

        // Get favorite item information from request body
        ObjectMapper mapper = new ObjectMapper();
        FavoriteRequestBody body = mapper.readValue(request.getReader(), FavoriteRequestBody.class);
        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MySQLConnection connection = null;
        try {
            // Remove the favorite item from database
            connection = new MySQLConnection();
            connection.unsetFavoriteItem(userId, body.getFavoriteItem().getId());
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
