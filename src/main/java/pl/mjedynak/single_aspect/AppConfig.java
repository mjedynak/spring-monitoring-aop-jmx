package pl.mjedynak.single_aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

import javax.management.MalformedObjectNameException;

@Configuration
@EnableAspectJAutoProxy
@EnableMBeanExport
@ComponentScan({"pl.mjedynak.single_aspect", "pl.mjedynak.service"})
public class AppConfig {

    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        return new MBeanServerFactoryBean();
    }

    // for remote connection
    @Bean
    public RmiRegistryFactoryBean registry() {
        return new RmiRegistryFactoryBean();
    }

    @Bean
    @DependsOn("registry")
    public ConnectorServerFactoryBean connectorServer() throws MalformedObjectNameException {
        ConnectorServerFactoryBean connectorServerFactoryBean = new ConnectorServerFactoryBean();
        connectorServerFactoryBean.setObjectName("connector:name=rmi");
        connectorServerFactoryBean.setServiceUrl("service:jmx:rmi://localhost/jndi/rmi://localhost:1099/connector");
        return connectorServerFactoryBean;
    }

}
