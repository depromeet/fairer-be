package com.depromeet.fairer.domain.preset.constant;

import lombok.Getter;

@Getter
public enum Space {
    ENTRANCE, LIVINGROOM, BATHROOM, OUTSIDE, ROOM, KITCHEN, ETC;

    public boolean equals(Space space) {
        return this.name().equals(space.name());
    }
}
