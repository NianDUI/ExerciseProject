// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: XAccessGrpc.proto

package test.grpc.grpc;

public interface XFileCmdOrBuilder extends
    // @@protoc_insertion_point(interface_extends:rpc_package.XFileCmd)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
   * @return Whether the filecontent field is set.
   */
  boolean hasFilecontent();
  /**
   * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
   * @return The filecontent.
   */
  test.grpc.grpc.XFileCmdsByContent getFilecontent();
  /**
   * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
   */
  test.grpc.grpc.XFileCmdsByContentOrBuilder getFilecontentOrBuilder();

  /**
   * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
   * @return Whether the filepath field is set.
   */
  boolean hasFilepath();
  /**
   * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
   * @return The filepath.
   */
  test.grpc.grpc.XFileCmdsByPath getFilepath();
  /**
   * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
   */
  test.grpc.grpc.XFileCmdsByPathOrBuilder getFilepathOrBuilder();

  /**
   * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
   * @return Whether the fileid field is set.
   */
  boolean hasFileid();
  /**
   * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
   * @return The fileid.
   */
  test.grpc.grpc.XFileCmdsByID getFileid();
  /**
   * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
   */
  test.grpc.grpc.XFileCmdsByIDOrBuilder getFileidOrBuilder();

  test.grpc.grpc.XFileCmd.FilecmdCase getFilecmdCase();
}
