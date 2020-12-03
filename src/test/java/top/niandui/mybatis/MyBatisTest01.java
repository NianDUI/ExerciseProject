package top.niandui.mybatis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import top.niandui.dao.IBookDao;
import top.niandui.model.Book;

/**
 * @Title: MyBatisTest01.java
 * @description: MyBatisTest01
 * @time: 2020/11/2 13:56
 * @author: liyongda
 * @version: 1.0
 */
@SpringBootTest
public class MyBatisTest01 {
    @Autowired
    IBookDao<Book> iBookDao;

    @Test
    @Transactional // 开启事务
    public void cache1() {
        Book book1 = iBookDao.model(7L);
        System.out.println("book1 = " + book1);
        Book book2 = iBookDao.model(7L);
        System.out.println("book1 = " + book1);
    }
}
