// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: XAccessGrpc.proto

package test.grpc.grpc;

public final class XAccessGrpc {
  private XAccessGrpc() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XRoute_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XRoute_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XFileCmdsByContent_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XFileCmdsByContent_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XFileCmdsByPath_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XFileCmdsByPath_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XFileCmdsByID_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XFileCmdsByID_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XFileCmd_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XFileCmd_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XCmd_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XCmd_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XResult_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XResult_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XCmdResult_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XCmdResult_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_package_XReply_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_package_XReply_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static final com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021XAccessGrpc.proto\022\013rpc_package\",\n\006XRou" +
      "te\022\021\n\tinstchain\030\001 \003(\005\022\017\n\007timeout\030\002 \001(\005\"M" +
      "\n\022XFileCmdsByContent\022\020\n\010filename\030\001 \001(\t\022\023" +
      "\n\013filecontent\030\002 \001(\t\022\020\n\010checksum\030\003 \001(\t\"#\n" +
      "\017XFileCmdsByPath\022\020\n\010filepath\030\001 \001(\t\"\037\n\rXF" +
      "ileCmdsByID\022\016\n\006fileid\030\001 \001(\t\"\256\001\n\010XFileCmd" +
      "\0226\n\013filecontent\030\001 \001(\0132\037.rpc_package.XFil" +
      "eCmdsByContentH\000\0220\n\010filepath\030\002 \001(\0132\034.rpc" +
      "_package.XFileCmdsByPathH\000\022,\n\006fileid\030\003 \001" +
      "(\0132\032.rpc_package.XFileCmdsByIDH\000B\n\n\010_fil" +
      "ecmd\"(\n\004XCmd\022\017\n\007command\030\001 \001(\t\022\017\n\007timeout" +
      "\030\002 \001(\005\"\270\002\n\010XRequest\022\"\n\005route\030\001 \001(\0132\023.rpc" +
      "_package.XRoute\022\020\n\010username\030\002 \001(\t\022\020\n\010use" +
      "rmima\030\003 \001(\t\022\021\n\tserviceid\030\004 \001(\005\022\014\n\004type\030\005" +
      " \001(\005\022\r\n\005token\030\006 \001(\t\022\016\n\006client\030\010 \001(\t\022\020\n\010w" +
      "arnflag\030\t \001(\005\022\021\n\tappcoding\030\n \001(\t\022#\n\010comm" +
              "ands\030e \003(\0132\021.rpc_package.XCmd\022'\n\014extdcom" +
      "mands\030f \003(\0132\021.rpc_package.XCmd\022+\n\014fileco" +
      "mmands\030g \003(\0132\025.rpc_package.XFileCmdJ\004\010\013\020" +
      "e\"*\n\007XResult\022\017\n\007errorno\030\001 \001(\t\022\016\n\006errmsg\030" +
      "\002 \001(\t\"T\n\nXCmdResult\022\017\n\007command\030\001 \001(\t\022\016\n\006" +
      "result\030\002 \001(\t\022\016\n\006encode\030\003 \001(\t\022\025\n\rfilter_r" +
      "esult\030\004 \001(\t\"\202\001\n\006XReply\022\014\n\004type\030\001 \001(\005\022\r\n\005" +
      "token\030\002 \001(\t\022$\n\006result\030\003 \001(\0132\024.rpc_packag" +
      "e.XResult\022/\n\016commandresults\030e \003(\0132\027.rpc_" +
      "package.XCmdResultJ\004\010\004\020e2{\n\010XService\0225\n\005" +
      "XCall\022\025.rpc_package.XRequest\032\023.rpc_packa" +
      "ge.XReply\"\000\0228\n\010XCallIPC\022\025.rpc_package.XR" +
      "equest\032\023.rpc_package.XReply\"\000B\030\n\024com.syn" +
      "qnc.util.grpcP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_rpc_package_XRoute_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_rpc_package_XRoute_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XRoute_descriptor,
        new java.lang.String[] { "Instchain", "Timeout", });
    internal_static_rpc_package_XFileCmdsByContent_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_rpc_package_XFileCmdsByContent_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XFileCmdsByContent_descriptor,
        new java.lang.String[] { "Filename", "Filecontent", "Checksum", });
    internal_static_rpc_package_XFileCmdsByPath_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_rpc_package_XFileCmdsByPath_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XFileCmdsByPath_descriptor,
        new java.lang.String[] { "Filepath", });
    internal_static_rpc_package_XFileCmdsByID_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_rpc_package_XFileCmdsByID_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XFileCmdsByID_descriptor,
        new java.lang.String[] { "Fileid", });
    internal_static_rpc_package_XFileCmd_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_rpc_package_XFileCmd_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XFileCmd_descriptor,
        new java.lang.String[] { "Filecontent", "Filepath", "Fileid", "Filecmd", });
    internal_static_rpc_package_XCmd_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_rpc_package_XCmd_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XCmd_descriptor,
        new java.lang.String[] { "Command", "Timeout", });
    internal_static_rpc_package_XRequest_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_rpc_package_XRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XRequest_descriptor,
        new java.lang.String[] { "Route", "Username", "Usermima", "Serviceid", "Type", "Token", "Client", "Warnflag", "Appcoding", "Commands", "Extdcommands", "Filecommands", });
    internal_static_rpc_package_XResult_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_rpc_package_XResult_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XResult_descriptor,
        new java.lang.String[] { "Errorno", "Errmsg", });
    internal_static_rpc_package_XCmdResult_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_rpc_package_XCmdResult_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XCmdResult_descriptor,
        new java.lang.String[] { "Command", "Result", "Encode", "FilterResult", });
    internal_static_rpc_package_XReply_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_rpc_package_XReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_package_XReply_descriptor,
        new java.lang.String[] { "Type", "Token", "Result", "Commandresults", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
