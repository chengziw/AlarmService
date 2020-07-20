package com.miss.server.alert.sdk.enums;

/**
 * 报警等级
 */
public enum AlertLevelEnum {

    ALL("*"),

    DEBUG("debug"),
    INFO("info"),
    WARN("warn"),
    ERROR("error");

    private String desc;

    private AlertLevelEnum(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    public static AlertLevelEnum getEnum(String desc) {
        for (AlertLevelEnum c : AlertLevelEnum.values()) {
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
