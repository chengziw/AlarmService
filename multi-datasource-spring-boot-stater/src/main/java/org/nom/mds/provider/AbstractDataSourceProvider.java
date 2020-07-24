package org.nom.mds.provider;

import org.nom.mds.creator.DataSourceCreator;
import org.nom.mds.spring.boot.autoconfigure.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractDataSourceProvider implements DynamicDataSourceProvider {

    @Autowired
    private DataSourceCreator dataSourceCreator;

    protected Map<String, DataSource> createDataSourceMap(
            Map<String, DataSourceProperty> dataSourcePropertiesMap) {
        Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size() * 2);
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            DataSourceProperty dataSourceProperty = item.getValue();
            String pollName = dataSourceProperty.getPoolName();
            if (pollName == null || "".equals(pollName)) {
                pollName = item.getKey();
            }
            dataSourceProperty.setPoolName(pollName);
            dataSourceMap.put(pollName, dataSourceCreator.createDataSource(dataSourceProperty));
        }
        return dataSourceMap;
    }
}
