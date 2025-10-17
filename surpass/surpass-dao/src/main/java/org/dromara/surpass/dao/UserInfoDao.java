package org.dromara.surpass.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.surpass.constants.ConstsStatus;
import org.dromara.surpass.pojo.entity.ChangePassword;
import org.dromara.surpass.pojo.entity.idm.UserInfo;

import java.util.List;

@Mapper
public interface UserInfoDao extends IJpaMapper<UserInfo> {
    @Select("select * from  jbx_userinfo where deleted = 'n' and username = #{value} and status = " + ConstsStatus.ACTIVE)
    public UserInfo findByUsername(String username);

    @Select("select * from  jbx_userinfo where deleted = 'n' and ( email = #{value} or mobile= #{value} ) and status = " + ConstsStatus.ACTIVE)
    public UserInfo findByEmailMobile(String emailMobile);

    public void updateLocked(UserInfo userInfo);

    public void updateLockout(UserInfo userInfo);

    @Update("update jbx_userinfo set status =  #{status} where id = #{id}")
    public int updateStatus(UserInfo userInfo) ;

    @Update("update jbx_userinfo set book_id =  #{bookId} where id = #{id}")
    public int switchBook(UserInfo userInfo) ;

    public int changePassword(ChangePassword changePassword);
}
