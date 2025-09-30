package org.dromara.surpass.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.surpass.pojo.entity.access.SessionList;
import org.dromara.surpass.pojo.entity.idm.UserInfo;


import java.util.List;

@Mapper
public interface SessionListDao extends IJpaMapper<SessionList> {
    @Select("select * from surpass_session_list  where session_id = #{sessionId}")
    public SessionList getBySessionId(@Param("sessionId") String sessionId) ;

    @Update("delete from surpass_session_list  where session_id = #{sessionId}")
    public void removeById(@Param ("sessionId") String sessionId);

    @Update("update surpass_userinfo set last_logoff_time = #{lastLogoffTime} , is_online = 0  where id = #{id}")
    public void updateLastLogoffTime(UserInfo user);

    @Select("select * from surpass_session_list")
    public List<SessionList> listAll() ;

    @Select("select * from surpass_session_list where style = #{style}")
    public List<SessionList> listByStyle(@Param ("style") String style) ;
}
