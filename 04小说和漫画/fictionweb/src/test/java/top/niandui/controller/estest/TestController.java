package top.niandui.controller.estest;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: TestController.java
 * @description: TestController
 * @time: 2020/10/13 13:37
 * @author: liyongda
 * @version: 1.0
 */
@RestController
@Api(tags = "测试")
@RequestMapping("/test")
public class TestController {

    @Autowired
    private EsDao esDao;

    @GetMapping("/test1")
    public void test1() {
//        EsTest esTest = new EsTest(1, "name", "author");
//        esDao.save(esTest);
    }

    @GetMapping("/get/{id}")
    public Object get(@PathVariable Integer id){
        // 排序方式，这里是以“recordNo”为标准进行降序
        // 这里的"recordNo"是实体类的主键，记住一定要是实体类的属性，而不能是数据库的字段
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp", "log.offset");
        // （当前页， 每页记录数， 排序方式）
        Pageable pageable = PageRequest.of(0, id, sort);
        Page<EsTest> page = esDao.findAll(pageable);
//        Page<EsTest> page = esDao.findByMessage("*] INFO  *", pageable); //.sorted((o1, o2) -> -1)
        List list = page.stream().map(EsTest::getMessage).collect(Collectors.toList());
        list.addAll(page.getContent());
        test2(pageable);
        return list;
    }

    public void test2(Pageable pageable) {
        long count = esDao.count();
        System.out.println("count = " + count);
        esDao.findByMessageIn("] ERROR ", pageable)
                .stream().map(EsTest::getMessage).forEach(System.out::println);
    }
}
