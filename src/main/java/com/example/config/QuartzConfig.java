package com.example.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            @Qualifier("dataSource") DataSource dataSource) {

        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        // 🔥 这一行是生死线
        factory.setDataSource(dataSource);

        factory.setOverwriteExistingJobs(true);
        factory.setStartupDelay(5);
        factory.setAutoStartup(true);

        Properties props = new Properties();
        props.setProperty("org.quartz.jobStore.class",
                "org.quartz.impl.jdbcjobstore.JobStoreTX");
        props.setProperty("org.quartz.jobStore.driverDelegateClass",
                "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        props.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");

        factory.setQuartzProperties(props);
        return factory;
    }
}

