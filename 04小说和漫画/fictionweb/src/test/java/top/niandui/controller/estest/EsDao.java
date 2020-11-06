//package top.niandui.controller.estest;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Repository;
//
///**
// * @Title: EsDao.java
// * @description: EsDao
// * @time: 2020/10/13 13:39
// * @author: liyongda
// * @version: 1.0
// */
//@Repository
//public interface EsDao extends ElasticsearchRepository<EsTest, String> {
//
//    Page<EsTest> findByMessage(String message, Pageable pageable);
//
//    Page<EsTest> findByMessageIn(String message, Pageable pageable);
//}
