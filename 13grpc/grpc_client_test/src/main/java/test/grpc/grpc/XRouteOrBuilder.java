// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: XAccessGrpc.proto

package test.grpc.grpc;

public interface XRouteOrBuilder extends
    // @@protoc_insertion_point(interface_extends:rpc_package.XRoute)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated int32 instchain = 1;</code>
   * @return A list containing the instchain.
   */
  java.util.List<java.lang.Integer> getInstchainList();
  /**
   * <code>repeated int32 instchain = 1;</code>
   * @return The count of instchain.
   */
  int getInstchainCount();
  /**
   * <code>repeated int32 instchain = 1;</code>
   * @param index The index of the element to return.
   * @return The instchain at the given index.
   */
  int getInstchain(int index);

  /**
   * <code>int32 timeout = 2;</code>
   * @return The timeout.
   */
  int getTimeout();
}
