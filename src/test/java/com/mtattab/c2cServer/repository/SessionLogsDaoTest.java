package com.mtattab.c2cServer.repository;

import com.mtattab.c2cServer.repository.dao.SessionLogsDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SessionLogsDaoTest {

    @Autowired
    SessionLogsDao sessionLogsDao;

    @Test
    void getFirstNItemsTest(){
        List  logs = sessionLogsDao.getFirstNLogs(5);

        System.out.printf(logs.toString());

        Assertions.assertNotNull(logs);

        Assertions.assertEquals(5 ,logs.size());


    }
}
