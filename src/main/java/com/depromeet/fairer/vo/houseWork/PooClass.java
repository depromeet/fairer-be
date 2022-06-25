package com.depromeet.fairer.vo.houseWork;

import java.util.List;

public class PooClass {
    private List<HouseWorkAndAssigneeVo> houseWorkAndAssigneeVos;
    private int successCount;
    private int leftCount;

    public PooClass(List<HouseWorkAndAssigneeVo> houseWorkAndAssigneeVos, long successCount, long leftCount) {
        this.houseWorkAndAssigneeVos = houseWorkAndAssigneeVos;
        this.successCount = (int) successCount;
        this.leftCount = (int) leftCount;
    }
}
