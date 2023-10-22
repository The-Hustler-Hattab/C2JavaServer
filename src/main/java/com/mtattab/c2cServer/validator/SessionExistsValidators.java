package com.mtattab.c2cServer.validator;

import com.mtattab.c2cServer.annotations.SessionExistsValidator;
import com.mtattab.c2cServer.util.ConnectionManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SessionExistsValidators implements ConstraintValidator<SessionExistsValidator, String> {



    @Override
    public boolean isValid(String sessionId, ConstraintValidatorContext constraintValidatorContext) {

        if (ConnectionManager.getBySessionId(sessionId, ConnectionManager.connectedReverseToManagerSessions) != null){
            return true;
        }



        return false;
    }
}
