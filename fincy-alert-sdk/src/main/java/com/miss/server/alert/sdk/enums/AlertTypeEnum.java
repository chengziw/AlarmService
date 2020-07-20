package com.miss.server.alert.sdk.enums;

public enum AlertTypeEnum {

    TELEGRAM("telegram");

    private String desc;

    private AlertTypeEnum(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    public static AlertTypeEnum getEnum(String desc) {
        for (AlertTypeEnum c : AlertTypeEnum.values()) {
            if (c.desc().equalsIgnoreCase(desc)) {
                return c;
            }
        }
        return null;
    }

    public static boolean isValid(String desc) {
        return null != getEnum(desc);
    }

}
