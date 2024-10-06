package com.josefy.nnpda;

import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test")
public class ApplicationTest {
    @Test
    public void contextLoads() {
        System.out.println("Test context loaded successfully, good job me!");
    }
}
