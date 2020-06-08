package com.kseniia;

import com.kseniia.controller.CheckPointServlet;
import com.kseniia.service.CheckPointService;
import com.kseniia.stub.StubServletOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckPointServletTest {

    @Mock
    private CheckPointService checkPointService;

    @InjectMocks
    private CheckPointServlet checkPointServlet = new CheckPointServlet();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StubServletOutputStream servletOutputStream;

    @Before
    public void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        servletOutputStream = new StubServletOutputStream();
        when(response.getOutputStream()).thenReturn(servletOutputStream);
    }

    @Test
    public void testSuccessful() {
        doReturn("10").when(request).getParameter("keyId");
        doReturn("1").when(request).getParameter("roomId");
        doReturn("true").when(request).getParameter("entrance");

        checkPointServlet.doGet(request, response);

        ArgumentCaptor<Integer> roomIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> keyIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> entranceCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(checkPointService, times(1)).checkPointByParameters(
                roomIdCaptor.capture(),
                keyIdCaptor.capture(),
                entranceCaptor.capture());
        assertEquals(1, roomIdCaptor.getValue().intValue());
        assertEquals(10, keyIdCaptor.getValue().intValue());
        assertTrue(entranceCaptor.getValue());

        verify(response).setStatus(200);
        Assert.assertEquals(servletOutputStream.outputStream.toString(), "Successful");
    }

    @Test
    public void testIncorrectKeyIdValue() throws IOException {
        StubServletOutputStream servletOutputStream = new StubServletOutputStream();
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        doReturn("abc").when(request).getParameter("keyId");

        checkPointServlet.doGet(request, response);

        verify(response).setStatus(500);
        assertEquals(servletOutputStream.outputStream.toString(), "Incorrect keyId value");
    }

    @Test
    public void testIncorrectRoomIdValue() throws IOException {
        StubServletOutputStream servletOutputStream = new StubServletOutputStream();
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        doReturn("10").when(request).getParameter("keyId");
        doReturn("abc").when(request).getParameter("roomId");

        checkPointServlet.doGet(request, response);

        verify(response).setStatus(500);
        assertEquals(servletOutputStream.outputStream.toString(), "Incorrect roomId value");
    }

    @Test
    public void testIncorrectEntranceValue(){
        doReturn("10").when(request).getParameter("keyId");
        doReturn("10").when(request).getParameter("roomId");
        doReturn("abc").when(request).getParameter("entrance");

        checkPointServlet.doGet(request, response);

        verify(response).setStatus(500);
        assertEquals(servletOutputStream.outputStream.toString(), "Incorrect entrance value");
    }

    @Test
    public void testNullEntranceValue(){
        doReturn("10").when(request).getParameter("keyId");
        doReturn("10").when(request).getParameter("roomId");
        doReturn(null).when(request).getParameter("entrance");

        checkPointServlet.doGet(request, response);

        verify(response).setStatus(500);
        assertEquals(servletOutputStream.outputStream.toString(), "Incorrect entrance value");
    }
}

