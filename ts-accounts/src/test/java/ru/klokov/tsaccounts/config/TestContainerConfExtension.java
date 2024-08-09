package ru.klokov.tsaccounts.config;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.TestWatcher;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class TestContainerConfExtension implements TestWatcher {

    //Здесь мы читаем настройки из скомпилированного application.properties
    static Properties properties = new Properties();
    static {
        try (FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\target\\test-classes\\application.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            log.error("Error with reading application.properties - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:14.1")
            .withDatabaseName(properties.getProperty("database.name"))
            .withUsername(properties.getProperty("spring.datasource.username"))
            .withPassword(properties.getProperty("spring.datasource.password"))
            .withCreateContainerCmdModifier(cmd -> {
                // Пробрасываем порт 5432 контейнера на порт 5534 хоста
                Objects.requireNonNull(cmd.getHostConfig()).withPortBindings(
                        new Ports(new PortBinding(Ports.Binding.bindPort(5534), new ExposedPort(5432)))
                );
            });

    static  {
        POSTGRESQL_CONTAINER.start();
    }
}
