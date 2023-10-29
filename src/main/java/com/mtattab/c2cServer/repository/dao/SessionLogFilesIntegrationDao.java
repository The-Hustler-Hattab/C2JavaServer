package com.mtattab.c2cServer.repository.dao;

import com.mtattab.c2cServer.model.entity.SessionFilesEntity;
import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import com.mtattab.c2cServer.repository.SessionFilesRepository;
import com.mtattab.c2cServer.repository.SessionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SessionLogFilesIntegrationDao {

    @Autowired
    SessionLogRepository sessionLogRepository;

    @Autowired
    SessionFilesRepository sessionFilesRepository;


    public void saveFilesIntoLog(List<SessionFilesEntity> sessionFilesEntity, String sessionId){
        Optional<SessionLogEntity> sessionLogOptional= sessionLogRepository.findBySessionId(sessionId);

        List<SessionFilesEntity> sessionFilesEntitySaved = sessionFilesRepository.saveAll(sessionFilesEntity);

        if (sessionLogOptional.isPresent()){

            sessionFilesEntitySaved.forEach(file-> file.setSessionLog(sessionLogOptional.get()));
            sessionFilesRepository.saveAll(sessionFilesEntity);
            sessionLogRepository.updateSessionToHaveFiles("Y",sessionId);

        }


    }



}
