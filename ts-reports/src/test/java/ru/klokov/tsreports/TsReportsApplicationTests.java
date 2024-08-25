package ru.klokov.tsreports;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import ru.klokov.tsreports.config.TestContainerConfExtension;

@SpringBootTest
@ExtendWith(TestContainerConfExtension.class)
class TsReportsApplicationTests {

    @Test
    void contextLoads() {
    }

}
