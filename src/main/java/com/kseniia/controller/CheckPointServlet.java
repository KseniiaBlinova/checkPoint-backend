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
            int keyId = getKeyId(req);
            int roomId = getRoomId(req);
            boolean entrance = getEntrance(req);
            checkPointService.checkPointByParameters(roomId, keyId, entrance);

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

    private void write(ServletOutputStream outputStream, String message) {
        try {
            if (outputStream != null) {
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            log("Unable to return response to user", e);
        }
    }

    //get parameters value

    private int getKeyId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("keyId"));
        } catch (Exception e) {
            log.error("Incorrect keyId value", e);
            throw new IncorrectParameterException("Incorrect keyId value");
        }
    }

    private int getRoomId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("roomId"));
        } catch (Exception e) {
            log.error("Incorrect roomId value", e);
            throw new IncorrectParameterException("Incorrect roomId value");
        }
    }

    private boolean getEntrance(HttpServletRequest req) {
        if ("true".equals(req.getParameter("entrance")) || "false".equals(req.getParameter("entrance"))) {
            return Boolean.parseBoolean(req.getParameter("entrance"));
        } else {
            log.error("Incorrect entrance value");
            throw new IncorrectParameterException("Incorrect entrance value");
        }
    }
}
