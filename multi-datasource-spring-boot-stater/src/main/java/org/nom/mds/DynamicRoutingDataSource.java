package org.nom.mds;

import org.nom.mds.exception.ErrorCreateDataSourceException;
import org.nom.mds.provider.DynamicDataSourceProvider;
import org.nom.mds.strategy.DynamicDataSourceStrategy;
import org.nom.mds.toolkit.DynamicDataSourceContextHolder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 核心动态数据源组件
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource implements InitializingBean, DisposableBean {

    private static final String UNDERLINE = "_";
    /**
     * 所有数据库
     */
    private final Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();
    /**
     * 分组数据库
     */
    private final Map<String, DynamicGroupDataSource> groupDataSources = new ConcurrentHashMap<>();
    @Setter
    private DynamicDataSourceProvider provider;
    @Setter
    private String primary;
    @Setter
    private boolean strict;
    @Setter
    private Class<? extends DynamicDataSourceStrategy> strategy;

    @Override
    public DataSource determineDataSource() {
        return getDataSource(DynamicDataSourceContextHolder.peek());
    }

    private DataSource determinePrimaryDataSource() {
        log.debug("dynamic-dataSource switch to the primary dataSource");
        return groupDataSources.containsKey(primary) ? groupDataSources.get(primary).determineDataSource() : dataSourceMap.get(primary);
    }

    /**
     * 获取当前所有的数据源
     *
     * @return 当前所有数据源
     */
    public Map<String, DataSource> getCurrentDataSources() {
        return dataSourceMap;
    }

    /**
     * 获取的当前所有的分组数据源
     *
     * @return 当前所有的分组数据源
     */
    public Map<String, DynamicGroupDataSource> getCurrentGroupDataSources() {
        return groupDataSources;
    }

    /**
     * 获取数据源
     *
     * @param ds 数据源名称
     * @return 数据源
     */
    public DataSource getDataSource(String ds) {
        if (StringUtils.isEmpty(ds)) {
            return determinePrimaryDataSource();
        } else if (!groupDataSources.isEmpty() && groupDataSources.containsKey(ds)) {
            log.debug("dynamic-dataSource switch to the dataSource named [{}]", ds);
            return groupDataSources.get(ds).determineDataSource();
        } else if (dataSourceMap.containsKey(ds)) {
            log.debug("dynamic-dataSource switch to the dataSource named [{}]", ds);
            return dataSourceMap.get(ds);
        }
        if (strict) {
            throw new ErrorCreateDataSourceException("dynamic-dataSource could not find a dataSource named" + ds);
        }
        return determinePrimaryDataSource();
    }

    /**
     * 添加数据源
     *
     * @param ds         数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(String ds, DataSource dataSource) {
        if (!dataSourceMap.containsKey(ds)) {
            dataSourceMap.put(ds, dataSource);
            this.addGroupDataSource(ds, dataSource);
            log.info("dynamic-dataSource - load a dataSource named [{}] success", ds);
        } else {
            log.warn("dynamic-dataSource - load a dataSource named [{}] failed, because it already exist", ds);
        }
    }


    private void addGroupDataSource(String ds, DataSource dataSource) {
        if (ds.contains(UNDERLINE)) {
            String group = ds.split(UNDERLINE)[0];
            if (groupDataSources.containsKey(group)) {
                groupDataSources.get(group).addDataSource(dataSource);
            } else {
                try {
                    DynamicGroupDataSource groupDataSource = new DynamicGroupDataSource(group, strategy.getDeclaredConstructor().newInstance());
                    groupDataSource.addDataSource(dataSource);
                    groupDataSources.put(group, groupDataSource);
                } catch (Exception e) {
                    log.error("dynamic-dataSource - add the dataSource named [{}] error", ds, e);
                    dataSourceMap.remove(ds);
                }
            }
        }
    }

    /**
     * 删除数据源
     *
     * @param ds 数据源名称
     */
    public synchronized void removeDataSource(String ds) {
        if (!StringUtils.hasText(ds)) {
            throw new ErrorCreateDataSourceException("remove parameter could not be empty");
        }
        if (primary.equals(ds)) {
            throw new ErrorCreateDataSourceException("could not remove primary dataSource");
        }
        if (dataSourceMap.containsKey(ds)) {
            DataSource dataSource = dataSourceMap.get(ds);
            try {
                closeDataSource(ds, dataSource);
            } catch (Exception e) {
                throw new ErrorCreateDataSourceException("dynamic-dataSource - remove the database named " + ds + " failed", e);
            }
            dataSourceMap.remove(ds);
            if (ds.contains(UNDERLINE)) {
                String group = ds.split(UNDERLINE)[0];
                if (groupDataSources.containsKey(group)) {
                    groupDataSources.get(group).removeDataSource(dataSource);
                }
            }
            log.info("dynamic-dataSource - remove the database named [{}] success", ds);
        } else {
            log.warn("dynamic-dataSource - could not find a database named [{}]", ds);
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("dynamic-dataSource start closing ....");
        for (Map.Entry<String, DataSource> item : dataSourceMap.entrySet()) {
            closeDataSource(item.getKey(), item.getValue());
        }
        log.info("dynamic-dataSource all closed success,bye");
    }

    private void closeDataSource(String name, DataSource dataSource)
            throws IllegalAccessException, InvocationTargetException {
        Class<? extends DataSource> clazz = dataSource.getClass();
        try {
            Method closeMethod = clazz.getDeclaredMethod("close");
            closeMethod.invoke(dataSource);
        } catch (NoSuchMethodException e) {
            log.warn("dynamic-dataSource close the dataSource named [{}] failed,", name);
        }
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, DataSource> dataSources = provider.loadDataSources();
        // 添加并分组数据源
        for (Map.Entry<String, DataSource> dsItem : dataSources.entrySet()) {
            addDataSource(dsItem.getKey(), dsItem.getValue());
        }
        // 检测默认数据源设置
        if (groupDataSources.containsKey(primary)) {
            log.info("dynamic-dataSource initial loaded [{}] dataSource,primary group dataSource named [{}]", dataSources.size(), primary);
        } else if (dataSourceMap.containsKey(primary)) {
            log.info("dynamic-dataSource initial loaded [{}] dataSource,primary dataSource named [{}]", dataSources.size(), primary);
        } else {
            throw new ErrorCreateDataSourceException("dynamic-dataSource Please check the setting of primary");
        }
    }

}
