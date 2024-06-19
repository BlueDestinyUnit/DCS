package com.scd.dcs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DcsApplicationTests {



    @Test
    void contextLoads() {
        String date = "2024-06-30";


        System.out.println(String.format(date+"%d",1));



        System.out.println(date.substring(0,7));

    }

}
