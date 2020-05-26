package com.kseniia;

import com.kseniia.controller.CheckPointServlet;
import com.kseniia.service.CheckPointService;
import com.kseniia.stub.StubServletOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckPointServletTest {
    @Mock
    private CheckPointService checkPointService;
    @InjectMocks
    private CheckPointServlet checkPointServlet = new CheckPointServlet();

    @Test
    public void testSuccessful() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        StubServletOutputStream servletOutputStream = new StubServletOutputStream();
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        doReturn(session).when(request).getSession(true);
        doReturn("10").when(request).getParameter("keyId");
        doReturn("1").when(request).getParameter("roomId");
        doReturn("true").when(request).getParameter("entrance");

        checkPointServlet.doGet(request, response);

        ArgumentCaptor<Integer> roomIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> keyIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> entranceCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Boolean> enteredCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(checkPointService, times(1)).checkPointByParameters(
                roomIdCaptor.capture(),
                keyIdCaptor.capture(),
                entranceCaptor.capture(),
                enteredCaptor.capture());
        assertEquals(1, roomIdCaptor.getValue().intValue());
        assertEquals(10, keyIdCaptor.getValue().intValue());
        assertTrue(entranceCaptor.getValue());
        assertFalse(enteredCaptor.getValue());

        verify(response).setStatus(200);
        Assert.assertEquals(servletOutputStream.outputStream.toString(), "Successful");
    }

    @Test
    public void testIncorrectKeyIdValue() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        StubServletOutputStream servletOutputStream = new StubServletOutputStream();
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        doReturn(session).when(request).getSession(true);
        doReturn("abc").when(request).getParameter("keyId");

        checkPointServlet.doGet(request, response);

        verify(response).setStatus(500);
        assertEquals(servletOutputStream.outputStream.toString(), "Incorrect keyId value");
    }

    @Test
    public void testIncorrectRoomIdValue() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);


        StubServletOutputStream servletOutputStream = new StubServletOutputStream();
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        doReturn(session).when(request).getSession(true);
        doReturn("10").when(request).getParameter("keyId");
        doReturn("abc").when(request).getParameter("roomId");

        checkPointServlet.doGet(request, response);

        verify(response).setStatus(500);
        assertEquals(servletOutputStream.outputStream.toString(), "Incorrect roomId value");
    }

    @Test
    public void testIncorrectEntranceValue() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);


        StubServletOutputStream servletOutputStream = new StubServletOutputStream();
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        doReturn(session).when(request).getSession(true);
        doReturn("10").when(request).getParameter("keyId");
        doReturn("10").when(request).getParameter("roomId");
        doReturn("abc").when(request).getParameter("entrance");

        checkPointServlet.doGet(request, response);

        verify(response).setStatus(500);
        assertEquals(servletOutputStream.outputStream.toString(), "Incorrect entrance value");
    }
}

