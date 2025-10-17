package org.dromara.surpass.pojo.entity.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/16 16:34
 */
public class QueryGroupMembersDto {

    List<String> members;

    public QueryGroupMembersDto() {
        members = new ArrayList<>();
    }

    public QueryGroupMembersDto(List<String> members) {
        this.members = members;
    }

    public void add(String memberId) {
        this.members.add(memberId);
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
