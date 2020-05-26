package com.kseniia.service;

import com.kseniia.exception.IncorrectParameterException;
import com.kseniia.exception.NoEntryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckPointService {

    private static final Logger log = LoggerFactory.getLogger("checkPointUtil");

    public boolean checkPointByParameters(int roomId,
                                          int keyId,
                                          boolean entrance,
                                          boolean entered) {
        try {
            checkKeyId(keyId);
            checkRoomId(roomId);
            return isEnteredRoom(roomId, keyId, entrance, entered);
        } catch (IncorrectParameterException e) {
            throw new IncorrectParameterException(e.getMessage());
        }
    }

    /**
     * validation of the keyId value
     */
    private void checkKeyId(int keyId) {
        if (keyId < 1 || keyId > 10000) {
            log.error("Incorrect keyId value");
            throw new IncorrectParameterException("Incorrect keyId value");
        }
    }

    /**
     * validation of the roomId value
     */
    private void checkRoomId(int roomId) {
        if (roomId < 1 || roomId > 5) {
            log.error("Incorrect roomId value");
            throw new IncorrectParameterException("Incorrect roomId value");
        }
    }

    /**
     * check try user go to or get out in the room
     */
    private boolean isEnteredRoom(int roomId,
                                  int keyId,
                                  boolean entrance,
                                  boolean entered) {

        if (keyId % roomId != 0) {
            log.error("try user = {} enter room = {} forbidden", keyId, roomId);
            throw new NoEntryException("No Entry");
        }
        if (entrance == entered) {
            if (entrance) {
                log.error("user = {} enter room = {} already in room", keyId, roomId);
                throw new NoEntryException("user already go to the room");
            } else {
                log.error("user = {} already get out the room = {}", keyId, roomId);
                throw new NoEntryException("user already get out the room");
            }
        }
        log.info("user = {} trying to {} the room = {}",
                keyId,
                entrance ? "get in" : "get out",
                roomId);

        return entrance;
    }
}
