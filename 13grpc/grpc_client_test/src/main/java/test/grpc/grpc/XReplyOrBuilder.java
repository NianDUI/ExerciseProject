// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: XAccessGrpc.proto

package test.grpc.grpc;

public interface XReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:rpc_package.XReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * 1-1000 for control
   * </pre>
   *
   * <code>int32 type = 1;</code>
   * @return The type.
   */
  int getType();

  /**
   * <code>string token = 2;</code>
   * @return The token.
   */
  java.lang.String getToken();
  /**
   * <code>string token = 2;</code>
   * @return The bytes for token.
   */
  com.google.protobuf.ByteString
      getTokenBytes();

  /**
   * <code>.rpc_package.XResult result = 3;</code>
   * @return Whether the result field is set.
   */
  boolean hasResult();
  /**
   * <code>.rpc_package.XResult result = 3;</code>
   * @return The result.
   */
  test.grpc.grpc.XResult getResult();
  /**
   * <code>.rpc_package.XResult result = 3;</code>
   */
  test.grpc.grpc.XResultOrBuilder getResultOrBuilder();

  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  java.util.List<test.grpc.grpc.XCmdResult> 
      getCommandresultsList();
  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  test.grpc.grpc.XCmdResult getCommandresults(int index);
  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  int getCommandresultsCount();
  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  java.util.List<? extends test.grpc.grpc.XCmdResultOrBuilder> 
      getCommandresultsOrBuilderList();
  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  test.grpc.grpc.XCmdResultOrBuilder getCommandresultsOrBuilder(
      int index);
}