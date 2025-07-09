package org.dromara.surpass.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * role entity
 * @author tomsun28
 * @date 00:27 2019-07-27
 */
@Entity
@Table(name = "auth_role")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name can not null")
    @Length(min = 3, max = 100, message = "name length in 3-100")
    private String name;

    @NotBlank(message = "code can not null")
    @Length(min = 3, max = 100, message = "code length in 3-100")
    private String code;

    @Range(min = 0, max = 9, message = "1 enable, 9 disable")
    private Integer status;

    private String description;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtUpdate;
}
