//package top.niandui.controller.estest;
//
//import io.swagger.annotations.Api;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.elasticsearch.search.aggregations.BucketOrder;
//import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
//import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
//import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
//import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
//import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
//import org.elasticsearch.search.aggregations.metrics.MinAggregationBuilder;
//import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @Title: TestController.java
// * @description: TestController
// * @time: 2020/10/13 13:37
// * @author: liyongda
// * @version: 1.0
// */
//@RestController
//@Api(tags = "测试")
//@RequestMapping("/test")
//public class TestController {
//
//    @Autowired
//    private EsDao esDao;
//    // 相对于 ElasticsearchRepository 偏向更底层的操作
//    @Autowired
//    private ElasticsearchRestTemplate restTemplate;
//
//    @GetMapping("/test1")
//    public void test1() {
////        EsTest esTest = new EsTest(1, "name", "author");
////        esDao.save(esTest);
//    }
//
//    @GetMapping("/get/{id}")
//    public Object get(@PathVariable Integer id){
//        // 排序方式，这里是以“recordNo”为标准进行降序
//        // 这里的"recordNo"是实体类的主键，记住一定要是实体类的属性，而不能是数据库的字段
//        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp", "log.offset");
//        // （当前页， 每页记录数， 排序方式）
//        Pageable pageable = PageRequest.of(0, id, sort);
//        Page<EsTest> page = esDao.findAll(pageable);
////        Page<EsTest> page = esDao.findByMessage("*] INFO  *", pageable); //.sorted((o1, o2) -> -1)
//        List list = page.stream().map(EsTest::getMessage).collect(Collectors.toList());
//        list.addAll(page.getContent());
//        test2(pageable);
//        return list;
//    }
//
//    public void test2(Pageable pageable) {
//        long count = esDao.count();
//        System.out.println("count = " + count);
//        esDao.findByMessageIn("] ERROR ", pageable)
//                .stream().map(EsTest::getMessage).forEach(System.out::println);
//    }
//
//    // 分组 terms 不支持分页
//    // 分组 terms 下 topHits 返回每个分组里面的数据条数
//
//    // 分组查询
//    public Object termsQuery() {
//        // 创建一个查询条件对象
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        // 拼接查询条件
//        queryBuilder.should(QueryBuilders.termQuery("f_accountname", "admin"));
//
//        MaxAggregationBuilder maxAgg = AggregationBuilders.max("max").field("f_actiontime");
//        MinAggregationBuilder minAgg = AggregationBuilders.min("min").field("f_actiontime");
//        AvgAggregationBuilder avgAgg = AggregationBuilders.avg("avg").field("f_actiontime");
//
//        // 创建分组查询条件,size是查询聚合出来的条数,不支持分页
//        TermsAggregationBuilder termsAgg = AggregationBuilders.terms("terms").field("f_result");
//        termsAgg.subAggregation(maxAgg);
//        termsAgg.subAggregation(minAgg);
//        termsAgg.subAggregation(avgAgg);
//        termsAgg.order(BucketOrder.key(true));
////        termsAgg.size(Integer.MAX_VALUE);
//        TopHitsAggregationBuilder topHits = AggregationBuilders.topHits("topHits")
//                .sort(SortBuilders.fieldSort("@timestamp").order(SortOrder.ASC)).from(0).size(1);
//        termsAgg.subAggregation(topHits);
//        FilterAggregationBuilder successfilter = AggregationBuilders.filter("successfilter", QueryBuilders.termQuery("f_result", 1));
//        FilterAggregationBuilder failfilter = AggregationBuilders.filter("failfilter", QueryBuilders.termQuery("f_result", 0));
//
//        // 创建查询对象
//        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder) // 对聚合产生了影响
////                .withFilter(queryBuilder) // 过滤没有对聚合产生影响
//                .withPageable(PageRequest.of(0, 2))
//                .withSort(SortBuilders.fieldSort("@timestamp").order(SortOrder.ASC))
//                .addAggregation(termsAgg)
//                .addAggregation(successfilter)
//                .addAggregation(failfilter)
//                .build();
//        SearchHits<Map> search = restTemplate.search(query, Map.class, IndexCoordinates.of("java-system-log-instruction*"));
//        Aggregations aggregations = search.getAggregations();
//        ParsedLongTerms terms = aggregations.get("terms");
//        Terms.Bucket bucket1 = terms.getBucketByKey("1");
////        ParsedMax max = bucket1.getAggregations().get("max");
////        ParsedMin min = bucket1.getAggregations().get("min");
////        ParsedAvg avg = bucket1.getAggregations().get("avg");
//        Terms.Bucket bucket0 = terms.getBucketByKey("0");
//        ParsedFilter fail = aggregations.get("failfilter");
//        ParsedFilter success = aggregations.get("successfilter");
//        return search;
////        return search.getAggregations();
//    }
//
//    public Object maxQuery() {
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        queryBuilder.should(QueryBuilders.termQuery("f_accountname", "admin"));
//        MaxAggregationBuilder maxAgg = AggregationBuilders.max("f_actiontimeMax").field("f_actiontime");
//        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withFilter(queryBuilder)
//                .addAggregation(maxAgg)
//                .withCollapseField("f_accountname")
//                .withPageable(PageRequest.of(0, 1))
//                .build();
//        SearchHits<Map> search = restTemplate.search(query, Map.class, IndexCoordinates.of("java-system-log-instruction*"));
//        return search;
//    }
//}
