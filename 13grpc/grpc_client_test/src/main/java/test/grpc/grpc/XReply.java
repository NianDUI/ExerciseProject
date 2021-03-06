// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: XAccessGrpc.proto

package test.grpc.grpc;

/**
 * Protobuf type {@code rpc_package.XReply}
 */
public final class XReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:rpc_package.XReply)
    XReplyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use XReply.newBuilder() to construct.
  private XReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private XReply() {
    token_ = "";
    commandresults_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new XReply();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private XReply(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {

            type_ = input.readInt32();
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            token_ = s;
            break;
          }
          case 26: {
            test.grpc.grpc.XResult.Builder subBuilder = null;
            if (result_ != null) {
              subBuilder = result_.toBuilder();
            }
            result_ = input.readMessage(test.grpc.grpc.XResult.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(result_);
              result_ = subBuilder.buildPartial();
            }

            break;
          }
          case 810: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              commandresults_ = new java.util.ArrayList<test.grpc.grpc.XCmdResult>();
              mutable_bitField0_ |= 0x00000001;
            }
            commandresults_.add(
                input.readMessage(test.grpc.grpc.XCmdResult.parser(), extensionRegistry));
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        commandresults_ = java.util.Collections.unmodifiableList(commandresults_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XReply_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            test.grpc.grpc.XReply.class, test.grpc.grpc.XReply.Builder.class);
  }

  public static final int TYPE_FIELD_NUMBER = 1;
  private int type_;
  /**
   * <pre>
   * 1-1000 for control
   * </pre>
   *
   * <code>int32 type = 1;</code>
   * @return The type.
   */
  @java.lang.Override
  public int getType() {
    return type_;
  }

  public static final int TOKEN_FIELD_NUMBER = 2;
  private volatile java.lang.Object token_;
  /**
   * <code>string token = 2;</code>
   * @return The token.
   */
  @java.lang.Override
  public java.lang.String getToken() {
    java.lang.Object ref = token_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      token_ = s;
      return s;
    }
  }
  /**
   * <code>string token = 2;</code>
   * @return The bytes for token.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getTokenBytes() {
    java.lang.Object ref = token_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      token_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int RESULT_FIELD_NUMBER = 3;
  private test.grpc.grpc.XResult result_;
  /**
   * <code>.rpc_package.XResult result = 3;</code>
   * @return Whether the result field is set.
   */
  @java.lang.Override
  public boolean hasResult() {
    return result_ != null;
  }
  /**
   * <code>.rpc_package.XResult result = 3;</code>
   * @return The result.
   */
  @java.lang.Override
  public test.grpc.grpc.XResult getResult() {
    return result_ == null ? test.grpc.grpc.XResult.getDefaultInstance() : result_;
  }
  /**
   * <code>.rpc_package.XResult result = 3;</code>
   */
  @java.lang.Override
  public test.grpc.grpc.XResultOrBuilder getResultOrBuilder() {
    return getResult();
  }

  public static final int COMMANDRESULTS_FIELD_NUMBER = 101;
  private java.util.List<test.grpc.grpc.XCmdResult> commandresults_;
  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  @java.lang.Override
  public java.util.List<test.grpc.grpc.XCmdResult> getCommandresultsList() {
    return commandresults_;
  }
  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  @java.lang.Override
  public java.util.List<? extends test.grpc.grpc.XCmdResultOrBuilder> 
      getCommandresultsOrBuilderList() {
    return commandresults_;
  }
  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  @java.lang.Override
  public int getCommandresultsCount() {
    return commandresults_.size();
  }
  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  @java.lang.Override
  public test.grpc.grpc.XCmdResult getCommandresults(int index) {
    return commandresults_.get(index);
  }
  /**
   * <pre>
   * command result
   * </pre>
   *
   * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
   */
  @java.lang.Override
  public test.grpc.grpc.XCmdResultOrBuilder getCommandresultsOrBuilder(
      int index) {
    return commandresults_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (type_ != 0) {
      output.writeInt32(1, type_);
    }
    if (!getTokenBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, token_);
    }
    if (result_ != null) {
      output.writeMessage(3, getResult());
    }
    for (int i = 0; i < commandresults_.size(); i++) {
      output.writeMessage(101, commandresults_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (type_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, type_);
    }
    if (!getTokenBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, token_);
    }
    if (result_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getResult());
    }
    for (int i = 0; i < commandresults_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(101, commandresults_.get(i));
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof test.grpc.grpc.XReply)) {
      return super.equals(obj);
    }
    test.grpc.grpc.XReply other = (test.grpc.grpc.XReply) obj;

    if (getType()
        != other.getType()) return false;
    if (!getToken()
        .equals(other.getToken())) return false;
    if (hasResult() != other.hasResult()) return false;
    if (hasResult()) {
      if (!getResult()
          .equals(other.getResult())) return false;
    }
    if (!getCommandresultsList()
        .equals(other.getCommandresultsList())) return false;
    return unknownFields.equals(other.unknownFields);
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + TYPE_FIELD_NUMBER;
    hash = (53 * hash) + getType();
    hash = (37 * hash) + TOKEN_FIELD_NUMBER;
    hash = (53 * hash) + getToken().hashCode();
    if (hasResult()) {
      hash = (37 * hash) + RESULT_FIELD_NUMBER;
      hash = (53 * hash) + getResult().hashCode();
    }
    if (getCommandresultsCount() > 0) {
      hash = (37 * hash) + COMMANDRESULTS_FIELD_NUMBER;
      hash = (53 * hash) + getCommandresultsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static test.grpc.grpc.XReply parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static test.grpc.grpc.XReply parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static test.grpc.grpc.XReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static test.grpc.grpc.XReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static test.grpc.grpc.XReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static test.grpc.grpc.XReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static test.grpc.grpc.XReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static test.grpc.grpc.XReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static test.grpc.grpc.XReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static test.grpc.grpc.XReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static test.grpc.grpc.XReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static test.grpc.grpc.XReply parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(test.grpc.grpc.XReply prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code rpc_package.XReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:rpc_package.XReply)
      test.grpc.grpc.XReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XReply_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              test.grpc.grpc.XReply.class, test.grpc.grpc.XReply.Builder.class);
    }

    // Construct using test.grpc.grpc.XReply.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getCommandresultsFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      type_ = 0;

      token_ = "";

      if (resultBuilder_ == null) {
        result_ = null;
      } else {
        result_ = null;
        resultBuilder_ = null;
      }
      if (commandresultsBuilder_ == null) {
        commandresults_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        commandresultsBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XReply_descriptor;
    }

    @java.lang.Override
    public test.grpc.grpc.XReply getDefaultInstanceForType() {
      return test.grpc.grpc.XReply.getDefaultInstance();
    }

    @java.lang.Override
    public test.grpc.grpc.XReply build() {
      test.grpc.grpc.XReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public test.grpc.grpc.XReply buildPartial() {
      test.grpc.grpc.XReply result = new test.grpc.grpc.XReply(this);
      int from_bitField0_ = bitField0_;
      result.type_ = type_;
      result.token_ = token_;
      if (resultBuilder_ == null) {
        result.result_ = result_;
      } else {
        result.result_ = resultBuilder_.build();
      }
      if (commandresultsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          commandresults_ = java.util.Collections.unmodifiableList(commandresults_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.commandresults_ = commandresults_;
      } else {
        result.commandresults_ = commandresultsBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof test.grpc.grpc.XReply) {
        return mergeFrom((test.grpc.grpc.XReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(test.grpc.grpc.XReply other) {
      if (other == test.grpc.grpc.XReply.getDefaultInstance()) return this;
      if (other.getType() != 0) {
        setType(other.getType());
      }
      if (!other.getToken().isEmpty()) {
        token_ = other.token_;
        onChanged();
      }
      if (other.hasResult()) {
        mergeResult(other.getResult());
      }
      if (commandresultsBuilder_ == null) {
        if (!other.commandresults_.isEmpty()) {
          if (commandresults_.isEmpty()) {
            commandresults_ = other.commandresults_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureCommandresultsIsMutable();
            commandresults_.addAll(other.commandresults_);
          }
          onChanged();
        }
      } else {
        if (!other.commandresults_.isEmpty()) {
          if (commandresultsBuilder_.isEmpty()) {
            commandresultsBuilder_.dispose();
            commandresultsBuilder_ = null;
            commandresults_ = other.commandresults_;
            bitField0_ = (bitField0_ & ~0x00000001);
            commandresultsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getCommandresultsFieldBuilder() : null;
          } else {
            commandresultsBuilder_.addAllMessages(other.commandresults_);
          }
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      test.grpc.grpc.XReply parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (test.grpc.grpc.XReply) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int type_ ;
    /**
     * <pre>
     * 1-1000 for control
     * </pre>
     *
     * <code>int32 type = 1;</code>
     * @return The type.
     */
    @java.lang.Override
    public int getType() {
      return type_;
    }
    /**
     * <pre>
     * 1-1000 for control
     * </pre>
     *
     * <code>int32 type = 1;</code>
     * @param value The type to set.
     * @return This builder for chaining.
     */
    public Builder setType(int value) {
      
      type_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 1-1000 for control
     * </pre>
     *
     * <code>int32 type = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearType() {
      
      type_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object token_ = "";
    /**
     * <code>string token = 2;</code>
     * @return The token.
     */
    public java.lang.String getToken() {
      java.lang.Object ref = token_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        token_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string token = 2;</code>
     * @return The bytes for token.
     */
    public com.google.protobuf.ByteString
        getTokenBytes() {
      java.lang.Object ref = token_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        token_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string token = 2;</code>
     * @param value The token to set.
     * @return This builder for chaining.
     */
    public Builder setToken(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      token_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string token = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearToken() {
      
      token_ = getDefaultInstance().getToken();
      onChanged();
      return this;
    }
    /**
     * <code>string token = 2;</code>
     * @param value The bytes for token to set.
     * @return This builder for chaining.
     */
    public Builder setTokenBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      token_ = value;
      onChanged();
      return this;
    }

    private test.grpc.grpc.XResult result_;
    private com.google.protobuf.SingleFieldBuilderV3<
        test.grpc.grpc.XResult, test.grpc.grpc.XResult.Builder, test.grpc.grpc.XResultOrBuilder> resultBuilder_;
    /**
     * <code>.rpc_package.XResult result = 3;</code>
     * @return Whether the result field is set.
     */
    public boolean hasResult() {
      return resultBuilder_ != null || result_ != null;
    }
    /**
     * <code>.rpc_package.XResult result = 3;</code>
     * @return The result.
     */
    public test.grpc.grpc.XResult getResult() {
      if (resultBuilder_ == null) {
        return result_ == null ? test.grpc.grpc.XResult.getDefaultInstance() : result_;
      } else {
        return resultBuilder_.getMessage();
      }
    }
    /**
     * <code>.rpc_package.XResult result = 3;</code>
     */
    public Builder setResult(test.grpc.grpc.XResult value) {
      if (resultBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        result_ = value;
        onChanged();
      } else {
        resultBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.rpc_package.XResult result = 3;</code>
     */
    public Builder setResult(
        test.grpc.grpc.XResult.Builder builderForValue) {
      if (resultBuilder_ == null) {
        result_ = builderForValue.build();
        onChanged();
      } else {
        resultBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.rpc_package.XResult result = 3;</code>
     */
    public Builder mergeResult(test.grpc.grpc.XResult value) {
      if (resultBuilder_ == null) {
        if (result_ != null) {
          result_ =
            test.grpc.grpc.XResult.newBuilder(result_).mergeFrom(value).buildPartial();
        } else {
          result_ = value;
        }
        onChanged();
      } else {
        resultBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.rpc_package.XResult result = 3;</code>
     */
    public Builder clearResult() {
      if (resultBuilder_ == null) {
        result_ = null;
        onChanged();
      } else {
        result_ = null;
        resultBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.rpc_package.XResult result = 3;</code>
     */
    public test.grpc.grpc.XResult.Builder getResultBuilder() {
      
      onChanged();
      return getResultFieldBuilder().getBuilder();
    }
    /**
     * <code>.rpc_package.XResult result = 3;</code>
     */
    public test.grpc.grpc.XResultOrBuilder getResultOrBuilder() {
      if (resultBuilder_ != null) {
        return resultBuilder_.getMessageOrBuilder();
      } else {
        return result_ == null ?
            test.grpc.grpc.XResult.getDefaultInstance() : result_;
      }
    }
    /**
     * <code>.rpc_package.XResult result = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        test.grpc.grpc.XResult, test.grpc.grpc.XResult.Builder, test.grpc.grpc.XResultOrBuilder> 
        getResultFieldBuilder() {
      if (resultBuilder_ == null) {
        resultBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            test.grpc.grpc.XResult, test.grpc.grpc.XResult.Builder, test.grpc.grpc.XResultOrBuilder>(
                getResult(),
                getParentForChildren(),
                isClean());
        result_ = null;
      }
      return resultBuilder_;
    }

    private java.util.List<test.grpc.grpc.XCmdResult> commandresults_ =
      java.util.Collections.emptyList();
    private void ensureCommandresultsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        commandresults_ = new java.util.ArrayList<test.grpc.grpc.XCmdResult>(commandresults_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        test.grpc.grpc.XCmdResult, test.grpc.grpc.XCmdResult.Builder, test.grpc.grpc.XCmdResultOrBuilder> commandresultsBuilder_;

    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public java.util.List<test.grpc.grpc.XCmdResult> getCommandresultsList() {
      if (commandresultsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(commandresults_);
      } else {
        return commandresultsBuilder_.getMessageList();
      }
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public int getCommandresultsCount() {
      if (commandresultsBuilder_ == null) {
        return commandresults_.size();
      } else {
        return commandresultsBuilder_.getCount();
      }
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public test.grpc.grpc.XCmdResult getCommandresults(int index) {
      if (commandresultsBuilder_ == null) {
        return commandresults_.get(index);
      } else {
        return commandresultsBuilder_.getMessage(index);
      }
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public Builder setCommandresults(
        int index, test.grpc.grpc.XCmdResult value) {
      if (commandresultsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCommandresultsIsMutable();
        commandresults_.set(index, value);
        onChanged();
      } else {
        commandresultsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public Builder setCommandresults(
        int index, test.grpc.grpc.XCmdResult.Builder builderForValue) {
      if (commandresultsBuilder_ == null) {
        ensureCommandresultsIsMutable();
        commandresults_.set(index, builderForValue.build());
        onChanged();
      } else {
        commandresultsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public Builder addCommandresults(test.grpc.grpc.XCmdResult value) {
      if (commandresultsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCommandresultsIsMutable();
        commandresults_.add(value);
        onChanged();
      } else {
        commandresultsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public Builder addCommandresults(
        int index, test.grpc.grpc.XCmdResult value) {
      if (commandresultsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCommandresultsIsMutable();
        commandresults_.add(index, value);
        onChanged();
      } else {
        commandresultsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public Builder addCommandresults(
        test.grpc.grpc.XCmdResult.Builder builderForValue) {
      if (commandresultsBuilder_ == null) {
        ensureCommandresultsIsMutable();
        commandresults_.add(builderForValue.build());
        onChanged();
      } else {
        commandresultsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public Builder addCommandresults(
        int index, test.grpc.grpc.XCmdResult.Builder builderForValue) {
      if (commandresultsBuilder_ == null) {
        ensureCommandresultsIsMutable();
        commandresults_.add(index, builderForValue.build());
        onChanged();
      } else {
        commandresultsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public Builder addAllCommandresults(
        java.lang.Iterable<? extends test.grpc.grpc.XCmdResult> values) {
      if (commandresultsBuilder_ == null) {
        ensureCommandresultsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, commandresults_);
        onChanged();
      } else {
        commandresultsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public Builder clearCommandresults() {
      if (commandresultsBuilder_ == null) {
        commandresults_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        commandresultsBuilder_.clear();
      }
      return this;
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public Builder removeCommandresults(int index) {
      if (commandresultsBuilder_ == null) {
        ensureCommandresultsIsMutable();
        commandresults_.remove(index);
        onChanged();
      } else {
        commandresultsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public test.grpc.grpc.XCmdResult.Builder getCommandresultsBuilder(
        int index) {
      return getCommandresultsFieldBuilder().getBuilder(index);
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public test.grpc.grpc.XCmdResultOrBuilder getCommandresultsOrBuilder(
        int index) {
      if (commandresultsBuilder_ == null) {
        return commandresults_.get(index);  } else {
        return commandresultsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public java.util.List<? extends test.grpc.grpc.XCmdResultOrBuilder> 
         getCommandresultsOrBuilderList() {
      if (commandresultsBuilder_ != null) {
        return commandresultsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(commandresults_);
      }
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public test.grpc.grpc.XCmdResult.Builder addCommandresultsBuilder() {
      return getCommandresultsFieldBuilder().addBuilder(
          test.grpc.grpc.XCmdResult.getDefaultInstance());
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public test.grpc.grpc.XCmdResult.Builder addCommandresultsBuilder(
        int index) {
      return getCommandresultsFieldBuilder().addBuilder(
          index, test.grpc.grpc.XCmdResult.getDefaultInstance());
    }
    /**
     * <pre>
     * command result
     * </pre>
     *
     * <code>repeated .rpc_package.XCmdResult commandresults = 101;</code>
     */
    public java.util.List<test.grpc.grpc.XCmdResult.Builder> 
         getCommandresultsBuilderList() {
      return getCommandresultsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        test.grpc.grpc.XCmdResult, test.grpc.grpc.XCmdResult.Builder, test.grpc.grpc.XCmdResultOrBuilder> 
        getCommandresultsFieldBuilder() {
      if (commandresultsBuilder_ == null) {
        commandresultsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            test.grpc.grpc.XCmdResult, test.grpc.grpc.XCmdResult.Builder, test.grpc.grpc.XCmdResultOrBuilder>(
                commandresults_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        commandresults_ = null;
      }
      return commandresultsBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:rpc_package.XReply)
  }

  // @@protoc_insertion_point(class_scope:rpc_package.XReply)
  private static final test.grpc.grpc.XReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new test.grpc.grpc.XReply();
  }

  public static test.grpc.grpc.XReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<XReply>
      PARSER = new com.google.protobuf.AbstractParser<XReply>() {
    @java.lang.Override
    public XReply parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new XReply(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<XReply> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<XReply> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public test.grpc.grpc.XReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

