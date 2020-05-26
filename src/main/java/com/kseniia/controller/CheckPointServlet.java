package com.kseniia.controller;

import com.kseniia.exception.IncorrectParameterException;
import com.kseniia.exception.NoEntryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kseniia.service.CheckPointService;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/check")
public class CheckPointServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger("checkPointServlet");
    private CheckPointService checkPointService = new CheckPointService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ServletOutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
            HttpSession session = req.getSession(true);
            int keyId = getKeyId(req);
            int roomId = getRoomId(req);
            boolean entrance = getEntrance(req);
            boolean entered = isEntered(session);
            boolean enteredRoom = checkPointService.checkPointByParameters(roomId, keyId, entrance, entered);
            setEntered(enteredRoom, session);

            resp.setStatus(200);
            write(outputStream, "Successful");

        } catch (IOException e) {
            resp.setStatus(500);
        } catch (IncorrectParameterException e) {
            resp.setStatus(500);
            write(outputStream, e.getMessage());
        } catch (NoEntryException e) {
            resp.setStatus(403);
            write(outputStream, e.getMessage());
        }
    }

    /**
     * set a flag for the room for this session
     * true - user go to the room
     * false - user get out the room
     */
    private void setEntered(boolean entrance,
                            HttpSession session) {
        session.setAttribute("entered", entrance);
    }

    private void write(ServletOutputStream outputStream, String message){
        try {
            if (outputStream != null) {
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get parameters value

    private int getKeyId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("keyId"));
        } catch (Exception e) {
            log.error("Incorrect keyId value");
            throw new IncorrectParameterException("Incorrect keyId value");
        }
    }

    private int getRoomId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("roomId"));
        } catch (Exception e) {
            log.error("Incorrect roomId value");
            throw new IncorrectParameterException("Incorrect roomId value");
        }
    }

    private boolean getEntrance(HttpServletRequest req) {
        if (req.getParameter("entrance").equals("true") || req.getParameter("entrance").equals("false")) {
            return Boolean.parseBoolean(req.getParameter("entrance"));
        } else throw new IncorrectParameterException("Incorrect entrance value");
    }

    private boolean isEntered(HttpSession session) {
        try {
            return (boolean) session.getAttribute("entered");
        } catch (Exception e) {
            return false;
        }
    }
}
