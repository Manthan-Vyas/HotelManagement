package com.akm.hotelmanagement.config.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:db.properties")
public class DatabaseConfig {
}
