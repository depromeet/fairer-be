package com.depromeet.fairer.domain.preset;

import com.depromeet.fairer.global.exception.FairerException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Preset {
    PRESET_1(1, Space.ENTRANCE,"신발 정리"),
    PRESET_2(2, Space.ENTRANCE,"현관 청소"),
    PRESET_3(3, Space.ENTRANCE,"분리수거하기"),
    PRESET_4(4, Space.BATHROOM,"욕조 청소"),
    PRESET_5(5, Space.BATHROOM,"변기 청소"),
    PRESET_6(6, Space.BATHROOM,"화장실 청소"),
    PRESET_7(7, Space.BATHROOM,"욕실 용품 정리"),
    PRESET_8(8, Space.ROOM,"옷장 정리"),
    PRESET_9(9, Space.ROOM,"화장대 정리"),
    PRESET_10(10, Space.ROOM,"방청소"),
    PRESET_11(11, Space.ROOM,"물건 정리정돈"),
    PRESET_12(12, Space.ROOM,"이불 정리"),
    PRESET_13(13, Space.KITCHEN,"설거지"),
    PRESET_14(14, Space.KITCHEN,"가스렌지 닦기"),
    PRESET_15(15, Space.KITCHEN,"냉장고 정리"),
    PRESET_16(16, Space.KITCHEN,"부엌 정리정돈"),
    PRESET_17(17, Space.KITCHEN,"음식물 쓰레기 버리기"),
    PRESET_18(18, Space.KITCHEN,"식사 준비하기"),
    PRESET_19(19, Space.KITCHEN,"간식 준비하기"),
    PRESET_20(20, Space.OUTSIDE,"쓰레기 버리기"),
    PRESET_21(21, Space.OUTSIDE,"장보기"),
    PRESET_22(22, Space.OUTSIDE,"반려동물 산책"),
    PRESET_23(23, Space.OUTSIDE,"마중 나가기"),
    PRESET_24(24, Space.LIVINGROOM,"창 청소"),
    PRESET_25(25, Space.LIVINGROOM,"거실 청소"),
    PRESET_26(26, Space.LIVINGROOM,"물건 정리정돈"),
    PRESET_27(27, Space.LIVINGROOM,"환기 시키기"),
    PRESET_28(28, Space.LIVINGROOM,"빨래 돌리기"),
    PRESET_29(29, Space.LIVINGROOM,"빨래 개기"),
    PRESET_30(30, Space.LIVINGROOM,"세탁기 청소"),
    ;

    public static Preset getPreset(Space space, String houseWorkName) {
        return Arrays.stream(values()).filter(p -> p.houseworkName.equals(houseWorkName) && p.space.equals(space))
                .findFirst().orElseThrow(() -> new FairerException("Preset Not Found"));
    }

    public static Preset getPreset(int presetId) {
        return Arrays.stream(values()).filter(p -> p.presetId == presetId)
                .findFirst().orElseThrow(() -> new FairerException("Preset Not Found"));
    }

    public static List<Preset> getPresetListBySpace(Space space) {
        return Arrays.stream(values()).filter(p -> p.space.equals(space)).collect(Collectors.toList());
    }

    public static List<String> getHouseworkNameListBySpace(Space space) {
        return Arrays.stream(values()).filter(p -> p.space.equals(space)).map(Preset::getHouseworkName).collect(Collectors.toList());
    }

    private int presetId;
    private Space space;
    private String houseworkName;
}
