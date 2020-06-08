package com.kseniia;

import com.kseniia.configuration.CheckPointConfiguration;
import com.kseniia.exception.IncorrectParameterException;
import com.kseniia.exception.NoEntryException;
import com.kseniia.service.CheckPointService;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(MockitoJUnitRunner.class)
public class CheckPointServiceTest {

    @InjectMocks
    private CheckPointService checkPointService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp(){
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        map.put(2,2);
        map.put(3,3);
        checkPointService = new CheckPointService(CheckPointConfiguration.Holder.INSTANCE, map);
    }

    @Test
    public void testSuccessfulGoToRoom() {
        checkPointService.checkPointByParameters(1, 1, true);
    }

    @Test
    public void testSuccessfulGetOutRoom(){
        this.checkPointService.checkPointByParameters(2, 2, false);
    }

    @Test
    public void testIncorrectKeyIdValue() {
        expectedEx.expect(IncorrectParameterException.class);
        expectedEx.expectMessage("Incorrect keyId value");

        checkPointService.checkPointByParameters(1, 1000000, true);
    }

    @Test
    public void testIncorrectRoomIdValue() {
        expectedEx.expect(IncorrectParameterException.class);
        expectedEx.expectMessage("Incorrect roomId value");

        checkPointService.checkPointByParameters(10, 1, true);
    }

    @Test
    public void testKeyIdNotMultipleOfRoomId() {
        expectedEx.expect(NoEntryException.class);
        expectedEx.expectMessage("No Entry");

        checkPointService.checkPointByParameters(2, 5, true);
    }

    @Test
    public void testUserAlreadyGoToRoom() {
        expectedEx.expect(NoEntryException.class);
        expectedEx.expectMessage("No Entry");

        checkPointService.checkPointByParameters(3, 3, true);
    }

    @Test
    public void testUserAlreadyGetOutInRoom() {
        expectedEx.expect(NoEntryException.class);
        expectedEx.expectMessage("No Entry");

        checkPointService.checkPointByParameters(1, 1, false);
    }
}
