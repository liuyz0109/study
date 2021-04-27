package com.liuyz.mapper;

import com.liuyz.entity.UserInfoEntity;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/3/1
 * @description user信息mapper接口
 */
public interface UserInfoMapper {

    UserInfoEntity selectUserInfoById(Integer id);

}
