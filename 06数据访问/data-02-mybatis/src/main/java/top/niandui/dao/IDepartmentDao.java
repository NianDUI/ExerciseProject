package top.niandui.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.niandui.model.Department;

/**
 * @Title: IDepartmentDao.java
 * @description: TODO
 * @time: 2020/1/9 11:15
 * @author: liyongda
 * @version: 1.0
 */
@Repository
public interface IDepartmentDao {

    @Select("select * from department where id=#{id}")
    Department getDeptById(Integer id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into department(department_name) values(#{departmentName})")
    Integer insertDept(Department department);

}
