package top.niandui.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Title: Emp.java
 * @description: Emp
 * @time: 2020/1/10 13:43
 * @author: liyongda
 * @version: 1.0
 */
@Data
@Entity // 指定该实体类和数据表相对应
@Table(name = "tbl_emp") // 指定和数据表对应的名称，默认为emp
public class Emp {
    @Id // 指定该属性为主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 指定主键增长策略
    private Integer id;
    @Column(name = "last_name", length = 50) // 指定该属性数据表的last_name相对应，长度为50
    private String lastName;
    @Column
    private String email;
}
