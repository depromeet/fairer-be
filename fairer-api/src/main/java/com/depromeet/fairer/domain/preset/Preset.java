package com.depromeet.fairer.domain.preset;

import com.depromeet.fairer.global.exception.FairerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Preset {
    ORGANIZING_SHOES(Space.ENTRANCE,"신발 정리"),
    CLEANING_ENTRANCE(Space.ENTRANCE,"현관 청소"),
    SEPARATING_TRASH(Space.ENTRANCE,"분리수거하기"),
    CLEANING_BATHTUB(Space.BATHROOM,"욕조 청소"),
    CLEANING_TOILET_BOWL(Space.BATHROOM,"변기 청소"),
    CLEANING_BATHROOM(Space.BATHROOM,"화장실 청소"),
    ORGANIZING_BATHROOM_ITEMS(Space.BATHROOM,"욕실 용품 정리"),
    ORGANIZING_CLOSET(Space.ROOM,"옷장 정리"),
    ARRANGEMENT_DRESSING_TABLE(Space.ROOM,"화장대 정리"),
    CLEANING_ROOM(Space.ROOM,"방 청소"),
    ORGANIZING_ROOM_ITEMS(Space.ROOM,"물건 정리정돈"),
    MAKING_BED(Space.ROOM,"이불 정리"),
    WASHING_DISH(Space.KITCHEN,"설거지"),
    CLEANING_STOVE(Space.KITCHEN,"가스렌지 닦기"),
    CLEANING_UP_REFRIGERATOR(Space.KITCHEN,"냉장고 정리"),
    CLEANING_UP_KITCHEN(Space.KITCHEN,"부엌 정리정돈"),
    DISPOSAL_FOOD_WASTE(Space.KITCHEN,"음식물 쓰레기 버리기"),
    PREPARING_MEAL(Space.KITCHEN,"식사 준비하기"),
    PREPARING_SNACK(Space.KITCHEN,"간식 준비하기"),
    DISPOSAL_GARBAGE(Space.OUTSIDE,"쓰레기 버리기"),
    SHOPPING(Space.OUTSIDE,"장보기"),
    PET_WALKING(Space.OUTSIDE,"반려동물 산책"),
    MEETING(Space.OUTSIDE,"마중 나가기"),
    CLEANING_WINDOW(Space.LIVINGROOM,"창 청소"),
    CLEANING_LININGROOM(Space.LIVINGROOM,"거실 청소"),
    ORGANIZING_LININGROOM_ITEMS(Space.LIVINGROOM,"물건 정리정돈"),
    VENTILATION(Space.LIVINGROOM,"환기 시키기"),
    WASHING_CLOTHES(Space.LIVINGROOM,"빨래 돌리기"),
    FOLDING_LAUNDRY(Space.LIVINGROOM,"빨래 개기"),
    CLEANING_WASHING_MACHINE(Space.LIVINGROOM,"세탁기 청소"),
    ;

    public static Preset getPreset(Space space, String houseWorkName) {
        return Arrays.stream(values()).filter(p -> p.houseworkName.equals(houseWorkName) && p.space.equals(space))
                .findFirst().orElseThrow(() -> new FairerException("Preset Not Found"));
    }

    public static List<Preset> getPresetListBySpace(Space space) {
        return Arrays.stream(values()).filter(p -> p.space.equals(space)).collect(Collectors.toList());
    }

    public static List<String> getHouseworkNameListBySpace(Space space) {
        return Arrays.stream(values()).filter(p -> p.space.equals(space)).map(Preset::getHouseworkName).collect(Collectors.toList());
    }

    private Space space;
    private String houseworkName;
}
