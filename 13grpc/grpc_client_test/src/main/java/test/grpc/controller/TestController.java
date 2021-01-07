package test.grpc.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.grpc.grpc.*;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**8001
 * TestController
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/18 16:29
 */
@RestController
@RequestMapping("/test")
public class TestController {
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("192.168.1.146", 50001).usePlaintext().build();

    public static void main(String[] args) throws Exception {
        TestController testController = new TestController();
        testController.send1();
//        testController.send2();
    }

    @GetMapping("/send1")
    public void send1() throws Exception {
        XRequest.Builder requestBuilder = XRequest.newBuilder();
        requestBuilder.setType(1001);
        requestBuilder.setUsername("liu123");
        requestBuilder.setPassword("8a6a19710fa4a6274eb764dc2a2f1c98");
        requestBuilder.setClient("192.168.1.78");
        requestBuilder.setServiceid(122);
        XCmd.Builder cmdBuilder = XCmd.newBuilder();
        cmdBuilder.setCommand("ls");
        requestBuilder.addCommands(cmdBuilder.build());
        cmdBuilder.setCommand("ll");
        requestBuilder.addCommands(cmdBuilder.build());
        cmdBuilder.setCommand("date");
        requestBuilder.addCommands(cmdBuilder.build());
        cmdBuilder.setCommand("echo ping 192.168.1.81 -c 5");
        requestBuilder.addCommands(cmdBuilder.build());

        XServiceGrpc.XServiceBlockingStub blockingStub = XServiceGrpc.newBlockingStub(managedChannel)
                // 返回一个新的存根，该存根的截止日期从现在开始为给定的{@code duration}之后
                .withDeadlineAfter(20, TimeUnit.SECONDS)
                ;
//        CallOptions callOptions = blockingStub.getCallOptions().withDeadlineAfter(1, TimeUnit.SECONDS);
        XReply reply = blockingStub.xCall(requestBuilder.build());
        System.out.println("reply = " + reply);
        System.out.println("reply.getType() = " + reply.getType());
        System.out.println("reply.getToken() = " + reply.getToken());
        System.out.println("reply.getResult() = " + reply.getResult());

        List<XCmdResult> commandresultsList = reply.getCommandresultsList();
        for (XCmdResult cmdResult : commandresultsList) {
            System.out.println("cmdResult = " + cmdResult);
            String result = new String(Base64.getDecoder().decode(cmdResult.getResult()));
            System.out.println("result = " + result);
        }
//        managedChannel.shutdown();
    }

    @GetMapping("/send2")
    public void send2() throws Exception {
        XRequest.Builder requestBuilder = XRequest.newBuilder();
        requestBuilder.setType(1002);
        requestBuilder.setUsername("liu123");
        requestBuilder.setPassword("8a6a19710fa4a6274eb764dc2a2f1c98");
        requestBuilder.setClient("192.168.1.78");
        requestBuilder.setServiceid(121);
        XCmd.Builder cmdBuilder = XCmd.newBuilder();
        cmdBuilder.setCommand("Dirwd");
        requestBuilder.addCommands(cmdBuilder.build());

        XServiceGrpc.XServiceBlockingStub blockingStub = XServiceGrpc.newBlockingStub(managedChannel);
        XReply reply = blockingStub.xCall(requestBuilder.build());
        System.out.println("reply = " + reply);
        System.out.println("reply.getType() = " + reply.getType());
        System.out.println("reply.getToken() = " + reply.getToken());
        System.out.println("reply.getResult() = " + reply.getResult());

        List<XCmdResult> commandresultsList = reply.getCommandresultsList();
        for (XCmdResult cmdResult : commandresultsList) {
            System.out.println("cmdResult = " + cmdResult);
            String result = new String(Base64.getDecoder().decode(cmdResult.getResult()));
            System.out.println("result = " + result);
        }
//        managedChannel.shutdown();
    }
}
