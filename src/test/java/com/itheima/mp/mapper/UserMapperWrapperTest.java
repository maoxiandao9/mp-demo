package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserMapperWrapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserService userService;

    @Test
    void testQueryWrapper() {
        // 1.构建查询条件 where name like "%o%" AND balance >= 1000
        var wrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        // 2.查询数据
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateByQueryWrapper() {
        // 1.构建查询条件 where name = "Jack"
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", "Jack");
        // 2.更新数据，user中非null字段都会作为set语句
        User user = new User();
        user.setBalance(2000);
        userMapper.update(user, wrapper);
    }

    @Test
    void testUpdateWrapper() {
        List<Long> ids = List.of(1L, 2L, 4L);
        // 1.生成SQL UPDATE user SET balance = balance - 200 WHERE id in (1, 2, 4)
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200") // SET balance = balance - 200
                .in("id", ids); // WHERE id in (1, 2, 4)
        // 2.更新，注意第一个参数可以给null，也就是不填更新字段和数据，
        // 而是基于UpdateWrapper中的setSQL来更新
        userMapper.update(null, wrapper);
    }

    @Test
    void testLambdaQueryWrapper() {
        // 1.构建条件 WHERE username LIKE "%o%" AND balance >= 1000
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        // 2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testCustomWrapper() {
        // 1.准备自定义查询条件
        List<Long> ids = List.of(1L, 2L, 4L);
        QueryWrapper<User> wrapper = new QueryWrapper<User>().in("id", ids);

        // 2.调用mapper的自定义方法，直接传递Wrapper
        userMapper.deductBalanceByIds(200, wrapper);
    }

    @Test
    void testCustomJoinWrapper() {
        // 1.准备自定义查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .in("u.id", List.of(1L, 2L, 4L))
                .eq("a.city", "北京");

        // 2.调用mapper的自定义方法
        List<User> users = userMapper.queryUserByWrapper(wrapper);

        users.forEach(System.out::println);
    }

//    @Test
//    void testSaveOneByOne() {
//        long b = System.currentTimeMillis();
//        for (int i = 1; i <= 100000; i++) {
//            userService.save(buildUser(i));
//        }
//        long e = System.currentTimeMillis();
//        System.out.println("耗时：" + (e - b));
//    }

//    private User buildUser(int i) {
//        User user = new User();
//        user.setUsername("user_" + i);
//        user.setPassword("123");
//        user.setPhone("" + (18688190000L + i));
//        user.setBalance(2000);
//        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
//        user.setCreateTime(LocalDateTime.now());
//        user.setUpdateTime(user.getCreateTime());
//        return user;
//    }

//    @Test
//    void testSaveBatch() {
//        // 准备10万条数据
//        List<User> list = new ArrayList<>(1000);
//        long b = System.currentTimeMillis();
//        for (int i = 1; i <= 100000; i++) {
//            list.add(buildUser(i));
//            // 每1000条批量插入一次
//            if (i % 1000 == 0) {
//                userService.saveBatch(list);
//                list.clear();
//            }
//        }
//        long e = System.currentTimeMillis();
//        System.out.println("耗时：" + (e - b));
//    }
}
