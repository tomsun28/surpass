package org.dromara.surpass.support;


import lombok.extern.slf4j.Slf4j;

import org.dromara.surpass.pojo.Message;
import org.dromara.surpass.service.impl.DataConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * controller exception handler
 * @author tomsun28
 * @date 22:45 2019-08-01
 */
@RestControllerAdvice
@Slf4j
public class GlobalDataConflictExceptionHandler {

    /**
     * handler the exception thrown for data conflict
     * @param exception data conflict
     * @return response
     */
    @ExceptionHandler(DataConflictException.class)
    @ResponseBody
    ResponseEntity<Message> handleDataConflictException(DataConflictException exception) {
        String errorMessage = "data status conflict warning";
        if (exception != null && exception.getMessage() != null) {
            errorMessage = exception.getMessage();
        }
        log.info("[sample-tom]-[data status conflict warning]-{}", errorMessage, exception);
        Message message = Message.builder().msg(errorMessage).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

}
