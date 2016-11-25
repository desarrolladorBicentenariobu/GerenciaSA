package com.arquetipo;

import com.arquetipo.cifrado.Encr;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.bbu.middleware.java.esb.ReceptorEmisorService;
import com.bbu.middleware.java.esb.ReceptorEmisor;

@SpringBootApplication
public class ArquetipoApplication extends SpringBootServletInitializer {
    
        @Autowired
        private Environment env;
        
        @Autowired
        private Encr encr;
        
        /* Inicio
        ********************** Configuración para el JDBC ******************************************/
        @Bean
        RoutingDataSource dataSource () {
            
            RoutingDataSource routingDataSource = new RoutingDataSource();

            routingDataSource.setDefaultTargetDataSource(dataSourceSQLServer());
            
            Map<Object, Object> dbType = new HashMap<Object, Object>();
            
            dbType.put(DbType.SQLServer, dataSourceSQLServer());
            dbType.put(DbType.Oracle, dataSourceOracle());
            dbType.put(DbType.DB2, dataSourceSQLServer());
            dbType.put(DbType.PostgreSQL, dataSourceSQLServer());
            
            routingDataSource.setTargetDataSources(dbType);
            routingDataSource.afterPropertiesSet();
            
            return routingDataSource;
        }
        
        //@Bean
        public BasicDataSource dataSourceSQLServer() {
            final BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("sqlserver.jdbc.driver")));
            dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("sqlserver.jdbc.url")));
            dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("sqlserver.jdbc.usuario")));
            
            String clave = null;
            try {
                clave = encr.dencr(env.getProperty("sqlserver.jdbc.clave"));
                //System.out.println("======== Clave: " + clave + "=========");
            } catch (Exception ex) {
                Logger.getLogger(ArquetipoApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
            dataSource.setPassword(Preconditions.checkNotNull(clave));

            return dataSource;
        }
        
        public BasicDataSource dataSourceOracle() {
            final BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("oracle.jdbc.driver")));
            dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("oracle.jdbc.url")));
            dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("oracle.jdbc.usuario")));
            
            String clave = null;
            try {
                clave = encr.dencr(env.getProperty("oracle.jdbc.clave"));
                //System.out.println("======== Clave: " + clave + "=========");
            } catch (Exception ex) {
                Logger.getLogger(ArquetipoApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
            dataSource.setPassword(Preconditions.checkNotNull(clave));

            return dataSource;
        }
        /********************** Configuración para el JDBC ******************************************
        * Fin */
                
        /* Inicio
        ********************** Configuración para el LDAP ******************************************/
        public LdapContextSource contextSource () {
            LdapContextSource contextSource= new LdapContextSource();
            contextSource.setUrl(env.getRequiredProperty("ldap.url"));
            contextSource.setUserDn(env.getRequiredProperty("ldap.user"));
            try {
                contextSource.setPassword(encr.dencr(env.getRequiredProperty("ldap.password")));
            } catch (Exception ex) {
                Logger.getLogger(ArquetipoApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
            contextSource.afterPropertiesSet();
            return contextSource;
        }
        
        @Bean
        public LdapTemplate ldapTemplate() {
            return new LdapTemplate(contextSource());        
        }
        /********************** Configuración para el LDAP ******************************************
        * Fin */        
        
        /* Inicio
        ********************** Configuración para Hibernate ******************************************/
        @Bean
        public LocalSessionFactoryBean sessionFactory() {
            final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
            sessionFactory.setDataSource(dataSource());
            sessionFactory.setPackagesToScan(new String[] { "paquete.base" });
            sessionFactory.setHibernateProperties(hibernateProperties());

            return sessionFactory;
        }
        
        @Bean
        @Autowired
        public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory) {
            final HibernateTransactionManager txManager = new HibernateTransactionManager();
            txManager.setSessionFactory(sessionFactory);

            return txManager;
        }
        
        final Properties hibernateProperties() {
            final Properties hibernateProperties = new Properties();
            hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
            hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
            hibernateProperties.setProperty("hibernate.current_session_context_class", env.getProperty("hibernate.current_session_context_class"));

            hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
            hibernateProperties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
            // hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");

            return hibernateProperties;
        }
        /********************** Configuración para Hibernate ******************************************
        * Fin */
        
        @Bean
        public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
            return new PersistenceExceptionTranslationPostProcessor();
        }
        
        /* Inicio
        ********************** Configuración de Seguridad ******************************************
	@Bean
	public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
            return new ApplicationSecurity();
	}
        /********************** Configuración de Seguridad ******************************************
        * Fin */
            
        /* Inicio
        ********************** Configuración de Ejecución ******************************************/
        // Hace que la aplicación .WAR, se pueda desplegar en un servidor de aplicaciones (Ej. Tomcat)
        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(ArquetipoApplication.class);
        }
        
        // Hace que la aplicación se pueda ejecutar sin necesidad de servidor de aplicaciones (Ej. .JAR)
	public static void main(String[] args) {
		SpringApplication.run(ArquetipoApplication.class, args);
	}
        /********************** Configuración de Ejecución ******************************************
        * Fin */       

}