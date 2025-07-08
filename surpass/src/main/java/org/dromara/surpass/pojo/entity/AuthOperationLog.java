package org.dromara.surpass.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author tomsun28
 * @date 2021/3/18 22:10
 */
@Entity
@Table(name = "auth_operation_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthOperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logName;

    private String username;

    private String api;

    private String method;

    private Boolean success;

    private String message;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtUpdate;
}
