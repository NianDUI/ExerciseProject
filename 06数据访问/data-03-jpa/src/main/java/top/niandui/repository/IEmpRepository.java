package top.niandui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.niandui.entity.Emp;

/**
 * @Title: IEmpRegistory.java
 * @description: IEmpRepository
 * @time: 2020/1/10 13:52
 * @author: liyongda
 * @version: 1.0
 */
@Repository
public interface IEmpRepository extends JpaRepository<Emp, Integer> {
}
