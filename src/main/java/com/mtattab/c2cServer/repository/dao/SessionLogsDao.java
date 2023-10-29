package com.mtattab.c2cServer.repository.dao;

import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import com.mtattab.c2cServer.util.GenericOperationsUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SessionLogsDao {

    @Autowired
    EntityManager entityManager;

    public List<SessionLogEntity> getFirstNLogs(int n) {
        String jpql = "SELECT c FROM SessionLogEntity c ORDER BY c.sessionCreatedAt DESC";
        Query query = entityManager.createQuery(jpql, SessionLogEntity.class);
        query.setMaxResults(n); // Limit the result set to the specified number of records
        List<SessionLogEntity> items = query.getResultList();

        GenericOperationsUtil.closeEntityManager(entityManager);
        return items;
    }



}
