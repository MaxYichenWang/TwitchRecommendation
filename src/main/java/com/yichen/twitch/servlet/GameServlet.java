package com.yichen.twitch.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yichen.twitch.entity.Game;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "GameServlet", urlPatterns = "/game")
public class GameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        Game game = new Game.Builder()
                .id("12924")
                .name("World of Warcraft")
                .boxArtUrl("[San Francisco](https://static-cdn.jtvnw.net/ttv-boxart/Warcraft%20III-{width}x{height}.jpg)")
                .build();

        response.getWriter().print(mapper.writeValueAsString(game));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
