package com.edu.cqupt.diseaseassociationmining.config;

import com.edu.cqupt.diseaseassociationmining.tool.PythonRun;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class ApplicationConfig {
    @Primary
    @Scope(value = "prototype")
    @Bean(name = "pythonRun")
    @ConfigurationProperties("application.python")
    public PythonRun getPythonRunBean(){
        return new PythonRun();
    }
}
