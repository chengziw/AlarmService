package com.miss.server.alert.sdk.enums;

/**
 * 优先级
 */
public enum PriorityLevelEnum {

    ALL("*"),

    /**
     * 马上处理
     */
    IMMEDIATE("immediate"),
    /**
     * 急需处理
     */
    URGENT("urgent"),
    /**
     * 高度重视
     */
    HIGH("high"),
    /**
     * 正常处理
     */
    NORMAL("normal"),
    /**
     * 低优先级
     */
    LOW("low");

    private String desc;

    private PriorityLevelEnum(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    public static PriorityLevelEnum getEnum(String desc) {
        for (PriorityLevelEnum c : PriorityLevelEnum.values()) {
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
