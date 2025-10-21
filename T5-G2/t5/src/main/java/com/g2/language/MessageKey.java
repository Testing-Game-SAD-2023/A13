package com.g2.language;

import lombok.Getter;

@Getter
public enum MessageKey {
    GAMEMODE_ALREADY_EXISTS("game.exception.GameModeAlreadyExists"),
    GAMEMODE_DOESNT_EXIST("game.exception.GameModeDoesntExists"),
    SESSION_DOESNT_EXIST("game.exception.SessionDoesntExists"),
    ;

    private final String bundleKey;

    MessageKey(String bundleKey) {
        this.bundleKey = bundleKey;
    }
}
