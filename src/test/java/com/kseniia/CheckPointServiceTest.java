package com.kseniia;

import com.kseniia.exception.IncorrectParameterException;
import com.kseniia.exception.NoEntryException;
import com.kseniia.service.CheckPointService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckPointServiceTest {

    @InjectMocks
    private CheckPointService checkPointService = new CheckPointService();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testSuccessfulGoToRoom() {
        assertTrue(checkPointService.checkPointByParameters(1, 1, true, false));
    }

    @Test
    public void testSuccessfulGetOutRoom() {
        assertFalse(checkPointService.checkPointByParameters(1, 1, false, true));
    }

    @Test
    public void testIncorrectKeyIdValue() {
        expectedEx.expect(IncorrectParameterException.class);
        expectedEx.expectMessage("Incorrect keyId value");

        checkPointService.checkPointByParameters(1, 1000000, true, false);
    }

    @Test
    public void testIncorrectRoomIdValue() {
        expectedEx.expect(IncorrectParameterException.class);
        expectedEx.expectMessage("Incorrect roomId value");

        checkPointService.checkPointByParameters(10, 1, true, false);
    }

    @Test
    public void testKeyIdNotMultipleOfRoomId() {
        expectedEx.expect(NoEntryException.class);
        expectedEx.expectMessage("No Entry");

        checkPointService.checkPointByParameters(2, 5, true, false);
    }

    @Test
    public void testUserAlreadyGoToRoom() {
        expectedEx.expect(NoEntryException.class);
        expectedEx.expectMessage("user already go to the room");

        checkPointService.checkPointByParameters(1, 1, true, true);
    }

    @Test
    public void testUserAlreadyGetOutInRoom() {
        expectedEx.expect(NoEntryException.class);
        expectedEx.expectMessage("user already get out the room");

        checkPointService.checkPointByParameters(1, 1, false, false);
    }
}
