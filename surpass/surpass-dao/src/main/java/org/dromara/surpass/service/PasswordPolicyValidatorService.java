package org.dromara.surpass.service;

import org.dromara.surpass.pojo.entity.ChangePassword;

public interface PasswordPolicyValidatorService {

    public boolean validator(ChangePassword changePassword);

    public String generateRandomPassword();

}
