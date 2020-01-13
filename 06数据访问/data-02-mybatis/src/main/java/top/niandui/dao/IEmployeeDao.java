package top.niandui.dao;

import org.springframework.stereotype.Repository;
import top.niandui.model.Employee;

/**
 * @Title: IEmployeeDao.java
 * @description: IEmployeeDao
 * @time: 2020/1/10 11:35
 * @author: liyongda
 * @version: 1.0
 */
@Repository
public interface IEmployeeDao {
    Employee getEmpById(Integer id);

    Integer insertEmp(Employee employee);
}
