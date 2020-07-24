package org.nom.mds;

import org.nom.mds.strategy.DynamicDataSourceStrategy;
import lombok.Data;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;

/**
 * 分组数据源
 */
@Data
public class DynamicGroupDataSource {

    private String groupName;

    private DynamicDataSourceStrategy dynamicDataSourceStrategy;

    private List<DataSource> dataSources = new LinkedList<>();

    public DynamicGroupDataSource(String groupName, DynamicDataSourceStrategy dynamicDataSourceStrategy) {
        this.groupName = groupName;
        this.dynamicDataSourceStrategy = dynamicDataSourceStrategy;
    }

    public void addDataSource(DataSource dataSource) {
        dataSources.add(dataSource);
    }

    public void removeDataSource(DataSource dataSource) {
        dataSources.remove(dataSource);
    }

    public DataSource determineDataSource() {
        return dynamicDataSourceStrategy.determineDataSource(dataSources);
    }

    public int size() {
        return dataSources.size();
    }
}
