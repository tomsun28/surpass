package org.dromara.surpass.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/22 16:01
 */

@Data
@NoArgsConstructor
@Table(name = "JBX_FILE_STORAGE")
@Entity
public class FileStorage extends JpaEntity {
    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    @JsonIgnore
    byte[] dataStored ;

    @JsonIgnore
    MultipartFile uploadFile;

    @Column
    String fileName;

    @Column
    String contentType;

    @Column
    long contentSize;

    @Column
    String category;

    String imageBase64;

    /**
     * 创建者
     */
    @Column
    @Schema(name = "createdBy",description = "创建人")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected String createdBy;

    /**
     * 创建时间
     */
    @Column
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Schema(name = "createdDate",description = "创建时间")
    protected Date createdDate;
}
