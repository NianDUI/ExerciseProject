//package top.niandui.controller.estest;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.DateFormat;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * @Title: EsTest.java
// * @description: EsTest
// * @time: 2020/10/13 14:06
// * @author: liyongda
// * @version: 1.0
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Document(indexName = "logstash-*")
//public class EsTest {
//    /**
//     * tags : ["beats_input_codec_plain_applied"]
//     * ecs : {"version":"1.5.0"}
//     * agent : {"hostname":"localhost.localdomain","id":"cc86bc8d-c656-405e-ab03-011d7eb4fad8","type":"filebeat","name":"localhost.localdomain","ephemeral_id":"b3f2dceb-1a38-4707-b991-405419aa6579","version":"7.9.2"}
//     * message : 2020-10-13 12:16:52.666 [task-40] DEBUG t.n.dao.IBookDao.queryBookTaskstatus- ==>  Preparing: SELECT "taskstatus" FROM "book" WHERE "bookid" = ?
//     *
//     * @timestamp : 2020-10-13T04:16:54.433Z
//     * host : {"hostname":"localhost.localdomain","id":"c2e215802b414b44aef5fe9d568b6911","containerized":false,"os":{"codename":"Core","name":"CentOS Linux","kernel":"4.18.0-193.19.1.el8_2.x86_64","version":"8 (Core)","family":"redhat","platform":"centos"},"name":"localhost.localdomain","ip":["192.168.3.10","2408:8207:7896:9641:ce4:a019:4542:2","2408:8207:7896:9641:d56:9098:d64d:8493","fe80::e421:125f:196:ba0f","10.144.0.21","fe80::84b1:e3ff:fe2d:499f"],"mac":["40:8d:5c:45:f6:d8","86:b1:e3:2d:49:9f"],"architecture":"x86_64"}
//     * log : {"offset":717442,"file":{"path":"/soft/fiction/fictionweb-log/info/[192.168.3.10]-[fictionweb]-[info.2020-10-13].log"}}
//     * input : {"type":"log"}
//     * @version : 1
//     */
//
//    @Id
//    private String id;
//    private EcsBean ecs;
//    private AgentBean agent;
//    private String message;
//    @Field(value = "@timestamp", format = DateFormat.date_optional_time, type = FieldType.Date)
//    private Date timestamp;
//    private HostBean host;
//    private LogBean log;
//    private InputBean input;
//    @Field(value = "@version")
//    private String version;
//    private List<String> tags;
//
//    @Data
//    public static class EcsBean {
//        /**
//         * version : 1.5.0
//         */
//
//        private String version;
//    }
//
//    @Data
//    public static class AgentBean {
//        /**
//         * hostname : localhost.localdomain
//         * id : cc86bc8d-c656-405e-ab03-011d7eb4fad8
//         * type : filebeat
//         * name : localhost.localdomain
//         * ephemeral_id : b3f2dceb-1a38-4707-b991-405419aa6579
//         * version : 7.9.2
//         */
//
//        private String hostname;
//        private String id;
//        private String type;
//        private String name;
//        private String ephemeral_id;
//        private String version;
//    }
//
//    @Data
//    public static class HostBean {
//        /**
//         * hostname : localhost.localdomain
//         * id : c2e215802b414b44aef5fe9d568b6911
//         * containerized : false
//         * os : {"codename":"Core","name":"CentOS Linux","kernel":"4.18.0-193.19.1.el8_2.x86_64","version":"8 (Core)","family":"redhat","platform":"centos"}
//         * name : localhost.localdomain
//         * ip : ["192.168.3.10","2408:8207:7896:9641:ce4:a019:4542:2","2408:8207:7896:9641:d56:9098:d64d:8493","fe80::e421:125f:196:ba0f","10.144.0.21","fe80::84b1:e3ff:fe2d:499f"]
//         * mac : ["40:8d:5c:45:f6:d8","86:b1:e3:2d:49:9f"]
//         * architecture : x86_64
//         */
//
//        private String hostname;
//        private String id;
//        private boolean containerized;
//        private OsBean os;
//        private String name;
//        private String architecture;
//        private List<String> ip;
//        private List<String> mac;
//
//        @Data
//        public static class OsBean {
//            /**
//             * codename : Core
//             * name : CentOS Linux
//             * kernel : 4.18.0-193.19.1.el8_2.x86_64
//             * version : 8 (Core)
//             * family : redhat
//             * platform : centos
//             */
//
//            private String codename;
//            private String name;
//            private String kernel;
//            private String version;
//            private String family;
//            private String platform;
//        }
//    }
//
//    @Data
//    public static class LogBean {
//        /**
//         * offset : 717442
//         * file : {"path":"/soft/fiction/fictionweb-log/info/[192.168.3.10]-[fictionweb]-[info.2020-10-13].log"}
//         */
//
//        private int offset;
//        private FileBean file;
//
//        @Data
//        public static class FileBean {
//            /**
//             * path : /soft/fiction/fictionweb-log/info/[192.168.3.10]-[fictionweb]-[info.2020-10-13].log
//             */
//
//            private String path;
//        }
//    }
//
//    @Data
//    public static class InputBean {
//        /**
//         * type : log
//         */
//
//        private String type;
//    }
//}
