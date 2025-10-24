package org.dromara.surpass.service.impl;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.dao.FileStorageDao;
import org.dromara.surpass.pojo.entity.FileStorage;
import org.dromara.surpass.service.FileStorageService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/22 16:05
 */
@Service
public class FileStorageServiceImpl extends JpaServiceImpl<FileStorageDao, FileStorage> implements FileStorageService {

}
