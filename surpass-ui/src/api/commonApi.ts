/* import { i18n } from 'vue-i18n'
const { t } = useI18n() */

import i18n from '@/languages'
const { t } = i18n.global;

//校验字段方法
export function validateFields(component : any, fieldsToValidate : any, form : any): any {
    return new Promise((resolve: any, reject: any) => {
        Promise.all(
            fieldsToValidate.map((field: any) => {
                return new Promise((resolve: any, reject: any) => {
                    component.$refs[form].validateField(field, (isValid: any) => {
                        resolve(isValid);
                    });
                });
            })
        ).then((validationResults: any) => {
            let valid: any = validationResults.every((isValid: any) => isValid);
            resolve(valid);
        }).catch((error: any) => {
            reject(error);
        });
    });
}

export const commonValidateNotEmptyAndMaxLength32: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 32, message: t('common.rules.NotEmptyAndMaxLength32'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}

export const commonValidateNotEmptyAndMaxLength64: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 64, message: t('common.rules.NotEmptyAndMaxLength64'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}


export const commonValidateNotEmptyAndMaxLength128: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 128, message: t('common.rules.NotEmptyAndMaxLength128'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}

export const commonValidateNotEmptyAndMaxLength200: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 200, message: t('common.rules.NotEmptyAndMaxLength200'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}

export const commonValidateNotEmptyAndMaxLength255: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 255, message: t('common.rules.NotEmptyAndMaxLength255'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}

export const commonValidateNotEmptyAndMaxLength8192: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 8192, message: t('common.rules.NotEmptyAndMaxLength8192'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}

export const commonValidateNotEmptyAndMaxLength4096: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 4096, message: t('common.rules.NotEmptyAndMaxLength4096'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}

export const commonValidateEmptyAndMaxLength255AndUri: any = () => {
  return [
    {min: 0, max: 255, message: t('common.rules.NotEmptyAndMaxLength255'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        const urlregex: any = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
        if(value == '' || value === undefined || value.trim()  === '') {
          callback();
        } else if(!urlregex.test(value)) {
          callback(new Error(t('common.rules.uri')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}

export const commonValidateNotEmptyAndMaxLength255AndUri: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 255, message: t('common.rules.NotEmptyAndMaxLength255'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        const urlregex: any = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else if(!urlregex.test(value)) {
          callback(new Error(t('common.rules.uri')));
        }else {
          callback();
        }
      }, trigger: "blur"}
  ]
}


export const commonValidateNotEmptyAndMaxLength255AndLDAP: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 255, message: t('common.rules.NotEmptyAndMaxLength255'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        const ldapRegex: any = /^(ldap?|ldaps):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else if(!ldapRegex.test(value)) {
          callback(new Error(t('common.rules.ldap')));
        }else {
          callback();
        }
      }, trigger: "blur"}
  ]
}


export const commonValidateNotEmptyAndMaxLength64AndNumber: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 64, message: t('common.rules.NotEmptyAndMaxLength64'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        const regex: any = /^[0-9]+$/; // 定义只包含字母和数字的正则表达式
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else if (!regex.test(value)) {
          callback(new Error(t('common.rules.number')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}

export const commonValidateNotEmptyAndMaxLength64AndNumberAndChar: any = () => {
  return [
    {required: true, message: t('common.rules.NotEmpty'), trigger: "blur"},
    {min: 1, max: 64, message: t('common.rules.NotEmptyAndMaxLength64'), trigger: 'blur'},
    { validator: (rule: any, value: any, callback: any) => {
        const regex: any = /^[a-zA-Z0-9]+$/;
        if(value == '' || value === undefined || value.trim()  === '') {
          callback(new Error(t('common.rules.NotEmpty')));
        } else if (!regex.test(value)) {
          callback(new Error(t('common.rules.numberChar')));
        } else {
          callback();
        }
      }, trigger: "blur"}
  ]
}

export const validatePass: any = (rule: any, value: any, callback: any, policy: any) => {
    console.log(policy)

    if (value === '') {
        callback(new Error(t('user.rule.pwd')));
        return;
    }

    let data: any = policy;
    let errors: any = [];

    if (data.minLength !== 0 && data.maxLength !== 0) {
        let inputLength: any = value.length;
        if (inputLength < data.minLength || inputLength > data.maxLength) {
            errors.push(`${t('user.rule.pwd1')}${data.minLength}-${data.maxLength}${t('user.rule.pwd2')}`);
        }
    }
    if (data.isLowerCase === 1) {
        // 必须包含小写字母
        if (!/[a-z]/.test(value)) {
            errors.push(t('user.rule.pwd3'));
        }
    }

    if (data.isDigit === 1) {
        // 必须包含阿拉伯数字
        if (!/\d/.test(value)) {
            errors.push(t('user.rule.pwd4'));
        }
    }

    if (data.isSpecial === 1) {
        // 必须包含标点符号
        if (!/[!@#$%^&*()_+={}\[\]:;<>,.?~\\-]/.test(value)) {
            errors.push(t('user.rule.pwd5'));
        }
    }

    if (/[\u4e00-\u9fa5]/.test(value)) {
        // 包含中文字符
        errors.push(t('user.rule.pwd6'));
    }

    if (/\s/.test(value)) {
        errors.push(t('user.rule.pwd7'));
    }

    if (errors.length > 0) {
        callback(new Error(errors.join(', ')));
    } else {
        callback();
    }
}
