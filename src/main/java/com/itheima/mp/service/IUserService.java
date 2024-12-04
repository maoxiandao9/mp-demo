package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;

public interface IUserService extends IService<User> {
    // 拓展自定义方法
    void deductBalance(Long id, Integer money);

    UserVO queryUserAndAddressById(Long userId);
}