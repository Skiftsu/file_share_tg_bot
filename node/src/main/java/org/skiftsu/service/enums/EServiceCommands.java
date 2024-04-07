package org.skiftsu.service.enums;

import java.util.HashMap;
import java.util.Map;

public enum EServiceCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    START("/start"),
    NULL("null");
    private String cmd;

    EServiceCommands(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return cmd;
    }

    public static EServiceCommands textToEnum(String cmd) {
        Map<String, EServiceCommands> enumMap = new HashMap<>();
        for (EServiceCommands i : EServiceCommands.values()) {
            if(i.cmd.equals(cmd)) {
                return i;
            }
        }

        return NULL;
    }
}
