package pl.mjedynak.multiple_aspects_single_metric;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Scope;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;
import pl.mjedynak.multiple_aspects_single_metric.metric.InvocationTimeMetric;

import javax.management.MalformedObjectNameException;

@Configuration
@EnableAspectJAutoProxy
@EnableMBeanExport
@ComponentScan({"pl.mjedynak.multiple_aspects_single_metric", "pl.mjedynak.service"})
public class AppConfig {

    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public InvocationTimeMetric invocationTimeMetric() {
        return new InvocationTimeMetric();
    }

    @Bean
    public AnnotationMBeanExporter mbeanExporter() {
        AnnotationMBeanExporter exporter = new AnnotationMBeanExporter();
        exporter.setNamingStrategy(new IdentityNamingStrategy());
        return exporter;
    }

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
