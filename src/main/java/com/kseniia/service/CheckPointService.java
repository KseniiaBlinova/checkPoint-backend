package com.kseniia.service;

import com.kseniia.configuration.CheckPointConfiguration;
import com.kseniia.exception.IncorrectParameterException;
import com.kseniia.exception.NoEntryException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@NoArgsConstructor
public class CheckPointService {

    private CheckPointConfiguration checkPointConfiguration = CheckPointConfiguration.Holder.INSTANCE;
    private Map<Integer, Integer> enteredRoomMap = new ConcurrentHashMap<>();

    private static final Logger log = LoggerFactory.getLogger("checkPointUtil");

    public void checkPointByParameters(int roomId,
                                       int keyId,
                                       boolean entrance) {
        try {
            checkKeyId(keyId);
            checkRoomId(roomId);
            boolean enteredRoom = isEnteredRoom(roomId, keyId, entrance);
            setEnteredRoom(enteredRoom, roomId, keyId);

        } catch (IncorrectParameterException e) {
            throw new IncorrectParameterException(e.getMessage());
        }
    }

    /**
     * validation of the keyId value
     */
    private void checkKeyId(int keyId) {
        if (keyId < checkPointConfiguration.getMinKeyIdValue()
                || keyId > checkPointConfiguration.getMaxKeyIdValue()) {
            log.error("Incorrect keyId value = {}", keyId);
            throw new IncorrectParameterException("Incorrect keyId value");
        }
    }

    /**
     * validation of the roomId value
     */
    private void checkRoomId(int roomId) {
        if (roomId < checkPointConfiguration.getMinRoomIdValue()
                || roomId > checkPointConfiguration.getMaxRoomIdValue()) {
            log.error("Incorrect roomId value = {}", roomId);
            throw new IncorrectParameterException("Incorrect roomId value");
        }
    }

    /**
     * check try user go to or get out in the room
     */
    private boolean isEnteredRoom(int roomId,
                                  int keyId,
                                  boolean entrance) {

        if (keyId % roomId != 0) {
            log.error("try user = {} enter room = {} forbidden", keyId, roomId);
            throw new NoEntryException("No Entry");
        }

        Integer enteredRoomByKeyId = enteredRoomMap.get(keyId);
        if (entrance) {
            enterRoom(roomId, enteredRoomByKeyId);
        } else {
            leaveRoom(roomId, enteredRoomByKeyId);
        }

        log.info("user = {} trying to {} the room = {}",
                keyId,
                entrance ? "get in" : "get out",
                roomId);

        return entrance;
    }

    private void enterRoom(int roomId, Integer enteredRoom) {
        if (Objects.nonNull(enteredRoom)) {
            log.error("No Entry in room {}", roomId);
            throw new NoEntryException("No Entry");
        }
    }

    private void leaveRoom(int roomId, Integer enteredRoom) {
        if (Objects.isNull(enteredRoom)) {
            log.error("No Entry in room {}", roomId);
            throw new NoEntryException("No Entry");
        } else {
            if (roomId != enteredRoom) {
                log.error("No Entry in room {}", roomId);
                throw new NoEntryException("No Entry");
            }
        }
    }

    private void setEnteredRoom(boolean entrance, int roomId, int keyId) {
        if (entrance) {
            enteredRoomMap.put(keyId, roomId);
        } else {
            enteredRoomMap.remove(keyId);
        }
    }
}
