package org.dromara.surpass.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.surpass.pojo.entity.Institutions;

@Mapper
public interface InstitutionsDao extends IJpaMapper<Institutions> {
    @Select("select * from  surpass_institutions where deleted = 'n' and id = #{value} or domain = #{value}  or console_domain = #{value}" )
    Institutions getByInstIdOrDomain(String instIdOrDomain);
}
