package org.nom.mds.strategy;

import javax.sql.DataSource;
import java.util.List;

/**
 * The interface of dynamic dataSource switch strategy
 *
 * @see RandomDynamicDataSourceStrategy
 * @see LoadBalanceDynamicDataSourceStrategy
 */
public interface DynamicDataSourceStrategy {

    /**
     * determine a database from the given dataSources
     *
     * @param dataSources given dataSources
     * @return final dataSource
     */
    DataSource determineDataSource(List<DataSource> dataSources);
}
