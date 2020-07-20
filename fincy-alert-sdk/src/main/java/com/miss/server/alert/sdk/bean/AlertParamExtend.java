package com.miss.server.alert.sdk.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertParamExtend extends AlertParam {
    private String system;
    private String serialNumber;
    private String contentKey;

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj || this.hashCode() == obj.hashCode()) {
            return true;
        }

        if (obj instanceof AlertParamExtend) {
            AlertParamExtend paramExtend = (AlertParamExtend) obj;
            if (this.getAppName().equals(paramExtend.getAppName())
                    && this.getModuleName().equals(paramExtend.getModuleName())
                    && this.getEventName().equals(paramExtend.getEventName())
                    && this.getAlertLevel().equals(paramExtend.getAlertLevel())
                    && this.getPriorityLevel().equals(paramExtend.getPriorityLevel())
                    && this.getContentKey().equals(paramExtend.getContentKey())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int n = 6;
        return this.getAppName().hashCode() * 31 ^ (n - 1)
                + this.getModuleName().hashCode() * 31 ^ (n - 2)
                + this.getEventName().hashCode() * 31 ^ (n - 3)
                + this.getAlertLevel().hashCode() * 31 ^ (n - 4)
                + this.getPriorityLevel().hashCode() * 31 ^ (n - 5)
                + this.getContentKey().hashCode() * 31 ^ (n - 6);
    }
}
