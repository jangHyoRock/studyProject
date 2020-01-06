package dhi.optimizer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "commonEntityManagerFactory", basePackages = {"dhi.optimizer.common.repository"})
public class CommonDBConfig {

  @Bean(name = "commonDataSource")
  @ConfigurationProperties(prefix = "common.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "commonEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      EntityManagerFactoryBuilder builder, @Qualifier("commonDataSource") DataSource dataSource) {

    return builder.dataSource(dataSource).packages("dhi.optimizer.model.db").persistenceUnit("common").build();
  }

  @Bean(name = "commonTransactionManager")
  public PlatformTransactionManager transactionManager(
      @Qualifier("commonEntityManagerFactory") EntityManagerFactory commonEntityManagerFactory) {
    return new JpaTransactionManager(commonEntityManagerFactory);
  }
}
