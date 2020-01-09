package top.niandui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.niandui.dao.IDepartmentDao;
import top.niandui.model.Department;

/**
 * @Title: DepartmentController.java
 * @description: DepartmentController
 * @time: 2020/1/9 11:20
 * @author: liyongda
 * @version: 1.0
 */
@RestController
public class DepartmentController {
    @Autowired
    private IDepartmentDao iDepartmentDao;

    @GetMapping("/dept/{id}")
    public Department getDepartmentById(@PathVariable Integer id) {
        return iDepartmentDao.getDeptById(id);
    }

    @GetMapping("/dept")
    public Department getDepartmentById(Department department) {
        iDepartmentDao.insertDept(department);
        return department;
    }
}
