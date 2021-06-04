// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: XAccessGrpc.proto

package test.grpc.grpc;

/**
 * Protobuf type {@code rpc_package.XFileCmd}
 */
public final class XFileCmd extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:rpc_package.XFileCmd)
    XFileCmdOrBuilder {
private static final long serialVersionUID = 0L;
  // Use XFileCmd.newBuilder() to construct.
  private XFileCmd(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private XFileCmd() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new XFileCmd();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private XFileCmd(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
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
          case 10: {
            test.grpc.grpc.XFileCmdsByContent.Builder subBuilder = null;
            if (FilecmdCase_ == 1) {
              subBuilder = ((test.grpc.grpc.XFileCmdsByContent) Filecmd_).toBuilder();
            }
            Filecmd_ =
                input.readMessage(test.grpc.grpc.XFileCmdsByContent.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((test.grpc.grpc.XFileCmdsByContent) Filecmd_);
              Filecmd_ = subBuilder.buildPartial();
            }
            FilecmdCase_ = 1;
            break;
          }
          case 18: {
            test.grpc.grpc.XFileCmdsByPath.Builder subBuilder = null;
            if (FilecmdCase_ == 2) {
              subBuilder = ((test.grpc.grpc.XFileCmdsByPath) Filecmd_).toBuilder();
            }
            Filecmd_ =
                input.readMessage(test.grpc.grpc.XFileCmdsByPath.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((test.grpc.grpc.XFileCmdsByPath) Filecmd_);
              Filecmd_ = subBuilder.buildPartial();
            }
            FilecmdCase_ = 2;
            break;
          }
          case 26: {
            test.grpc.grpc.XFileCmdsByID.Builder subBuilder = null;
            if (FilecmdCase_ == 3) {
              subBuilder = ((test.grpc.grpc.XFileCmdsByID) Filecmd_).toBuilder();
            }
            Filecmd_ =
                input.readMessage(test.grpc.grpc.XFileCmdsByID.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((test.grpc.grpc.XFileCmdsByID) Filecmd_);
              Filecmd_ = subBuilder.buildPartial();
            }
            FilecmdCase_ = 3;
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
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XFileCmd_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XFileCmd_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            test.grpc.grpc.XFileCmd.class, test.grpc.grpc.XFileCmd.Builder.class);
  }

  private int FilecmdCase_ = 0;
  private java.lang.Object Filecmd_;
  public enum FilecmdCase
      implements com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    FILECONTENT(1),
    FILEPATH(2),
    FILEID(3),
    FILECMD_NOT_SET(0);
    private final int value;
    FilecmdCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static FilecmdCase valueOf(int value) {
      return forNumber(value);
    }

    public static FilecmdCase forNumber(int value) {
      switch (value) {
        case 1: return FILECONTENT;
        case 2: return FILEPATH;
        case 3: return FILEID;
        case 0: return FILECMD_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  }

  public FilecmdCase
  getFilecmdCase() {
    return FilecmdCase.forNumber(
        FilecmdCase_);
  }

  public static final int FILECONTENT_FIELD_NUMBER = 1;
  /**
   * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
   * @return Whether the filecontent field is set.
   */
  @java.lang.Override
  public boolean hasFilecontent() {
    return FilecmdCase_ == 1;
  }
  /**
   * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
   * @return The filecontent.
   */
  @java.lang.Override
  public test.grpc.grpc.XFileCmdsByContent getFilecontent() {
    if (FilecmdCase_ == 1) {
       return (test.grpc.grpc.XFileCmdsByContent) Filecmd_;
    }
    return test.grpc.grpc.XFileCmdsByContent.getDefaultInstance();
  }
  /**
   * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
   */
  @java.lang.Override
  public test.grpc.grpc.XFileCmdsByContentOrBuilder getFilecontentOrBuilder() {
    if (FilecmdCase_ == 1) {
       return (test.grpc.grpc.XFileCmdsByContent) Filecmd_;
    }
    return test.grpc.grpc.XFileCmdsByContent.getDefaultInstance();
  }

  public static final int FILEPATH_FIELD_NUMBER = 2;
  /**
   * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
   * @return Whether the filepath field is set.
   */
  @java.lang.Override
  public boolean hasFilepath() {
    return FilecmdCase_ == 2;
  }
  /**
   * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
   * @return The filepath.
   */
  @java.lang.Override
  public test.grpc.grpc.XFileCmdsByPath getFilepath() {
    if (FilecmdCase_ == 2) {
       return (test.grpc.grpc.XFileCmdsByPath) Filecmd_;
    }
    return test.grpc.grpc.XFileCmdsByPath.getDefaultInstance();
  }
  /**
   * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
   */
  @java.lang.Override
  public test.grpc.grpc.XFileCmdsByPathOrBuilder getFilepathOrBuilder() {
    if (FilecmdCase_ == 2) {
       return (test.grpc.grpc.XFileCmdsByPath) Filecmd_;
    }
    return test.grpc.grpc.XFileCmdsByPath.getDefaultInstance();
  }

  public static final int FILEID_FIELD_NUMBER = 3;
  /**
   * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
   * @return Whether the fileid field is set.
   */
  @java.lang.Override
  public boolean hasFileid() {
    return FilecmdCase_ == 3;
  }
  /**
   * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
   * @return The fileid.
   */
  @java.lang.Override
  public test.grpc.grpc.XFileCmdsByID getFileid() {
    if (FilecmdCase_ == 3) {
       return (test.grpc.grpc.XFileCmdsByID) Filecmd_;
    }
    return test.grpc.grpc.XFileCmdsByID.getDefaultInstance();
  }
  /**
   * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
   */
  @java.lang.Override
  public test.grpc.grpc.XFileCmdsByIDOrBuilder getFileidOrBuilder() {
    if (FilecmdCase_ == 3) {
       return (test.grpc.grpc.XFileCmdsByID) Filecmd_;
    }
    return test.grpc.grpc.XFileCmdsByID.getDefaultInstance();
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
    if (FilecmdCase_ == 1) {
      output.writeMessage(1, (test.grpc.grpc.XFileCmdsByContent) Filecmd_);
    }
    if (FilecmdCase_ == 2) {
      output.writeMessage(2, (test.grpc.grpc.XFileCmdsByPath) Filecmd_);
    }
    if (FilecmdCase_ == 3) {
      output.writeMessage(3, (test.grpc.grpc.XFileCmdsByID) Filecmd_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (FilecmdCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (test.grpc.grpc.XFileCmdsByContent) Filecmd_);
    }
    if (FilecmdCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (test.grpc.grpc.XFileCmdsByPath) Filecmd_);
    }
    if (FilecmdCase_ == 3) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, (test.grpc.grpc.XFileCmdsByID) Filecmd_);
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
    if (!(obj instanceof test.grpc.grpc.XFileCmd)) {
      return super.equals(obj);
    }
    test.grpc.grpc.XFileCmd other = (test.grpc.grpc.XFileCmd) obj;

    if (!getFilecmdCase().equals(other.getFilecmdCase())) return false;
    switch (FilecmdCase_) {
      case 1:
        if (!getFilecontent()
            .equals(other.getFilecontent())) return false;
        break;
      case 2:
        if (!getFilepath()
            .equals(other.getFilepath())) return false;
        break;
      case 3:
        if (!getFileid()
            .equals(other.getFileid())) return false;
        break;
      case 0:
      default:
    }
    return unknownFields.equals(other.unknownFields);
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    switch (FilecmdCase_) {
      case 1:
        hash = (37 * hash) + FILECONTENT_FIELD_NUMBER;
        hash = (53 * hash) + getFilecontent().hashCode();
        break;
      case 2:
        hash = (37 * hash) + FILEPATH_FIELD_NUMBER;
        hash = (53 * hash) + getFilepath().hashCode();
        break;
      case 3:
        hash = (37 * hash) + FILEID_FIELD_NUMBER;
        hash = (53 * hash) + getFileid().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static test.grpc.grpc.XFileCmd parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static test.grpc.grpc.XFileCmd parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static test.grpc.grpc.XFileCmd parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static test.grpc.grpc.XFileCmd parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static test.grpc.grpc.XFileCmd parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static test.grpc.grpc.XFileCmd parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static test.grpc.grpc.XFileCmd parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static test.grpc.grpc.XFileCmd parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static test.grpc.grpc.XFileCmd parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static test.grpc.grpc.XFileCmd parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static test.grpc.grpc.XFileCmd parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static test.grpc.grpc.XFileCmd parseFrom(
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
  public static Builder newBuilder(test.grpc.grpc.XFileCmd prototype) {
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
   * Protobuf type {@code rpc_package.XFileCmd}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:rpc_package.XFileCmd)
      test.grpc.grpc.XFileCmdOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XFileCmd_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XFileCmd_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              test.grpc.grpc.XFileCmd.class, test.grpc.grpc.XFileCmd.Builder.class);
    }

    // Construct using test.grpc.grpc.XFileCmd.newBuilder()
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
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      FilecmdCase_ = 0;
      Filecmd_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return test.grpc.grpc.XAccessGrpc.internal_static_rpc_package_XFileCmd_descriptor;
    }

    @java.lang.Override
    public test.grpc.grpc.XFileCmd getDefaultInstanceForType() {
      return test.grpc.grpc.XFileCmd.getDefaultInstance();
    }

    @java.lang.Override
    public test.grpc.grpc.XFileCmd build() {
      test.grpc.grpc.XFileCmd result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public test.grpc.grpc.XFileCmd buildPartial() {
      test.grpc.grpc.XFileCmd result = new test.grpc.grpc.XFileCmd(this);
      if (FilecmdCase_ == 1) {
        if (filecontentBuilder_ == null) {
          result.Filecmd_ = Filecmd_;
        } else {
          result.Filecmd_ = filecontentBuilder_.build();
        }
      }
      if (FilecmdCase_ == 2) {
        if (filepathBuilder_ == null) {
          result.Filecmd_ = Filecmd_;
        } else {
          result.Filecmd_ = filepathBuilder_.build();
        }
      }
      if (FilecmdCase_ == 3) {
        if (fileidBuilder_ == null) {
          result.Filecmd_ = Filecmd_;
        } else {
          result.Filecmd_ = fileidBuilder_.build();
        }
      }
      result.FilecmdCase_ = FilecmdCase_;
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
      if (other instanceof test.grpc.grpc.XFileCmd) {
        return mergeFrom((test.grpc.grpc.XFileCmd)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(test.grpc.grpc.XFileCmd other) {
      if (other == test.grpc.grpc.XFileCmd.getDefaultInstance()) return this;
      switch (other.getFilecmdCase()) {
        case FILECONTENT: {
          mergeFilecontent(other.getFilecontent());
          break;
        }
        case FILEPATH: {
          mergeFilepath(other.getFilepath());
          break;
        }
        case FILEID: {
          mergeFileid(other.getFileid());
          break;
        }
        case FILECMD_NOT_SET: {
          break;
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
      test.grpc.grpc.XFileCmd parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (test.grpc.grpc.XFileCmd) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int FilecmdCase_ = 0;
    private java.lang.Object Filecmd_;
    public FilecmdCase
        getFilecmdCase() {
      return FilecmdCase.forNumber(
          FilecmdCase_);
    }

    public Builder clearFilecmd() {
      FilecmdCase_ = 0;
      Filecmd_ = null;
      onChanged();
      return this;
    }


    private com.google.protobuf.SingleFieldBuilderV3<
        test.grpc.grpc.XFileCmdsByContent, test.grpc.grpc.XFileCmdsByContent.Builder, test.grpc.grpc.XFileCmdsByContentOrBuilder> filecontentBuilder_;
    /**
     * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
     * @return Whether the filecontent field is set.
     */
    @java.lang.Override
    public boolean hasFilecontent() {
      return FilecmdCase_ == 1;
    }
    /**
     * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
     * @return The filecontent.
     */
    @java.lang.Override
    public test.grpc.grpc.XFileCmdsByContent getFilecontent() {
      if (filecontentBuilder_ == null) {
        if (FilecmdCase_ == 1) {
          return (test.grpc.grpc.XFileCmdsByContent) Filecmd_;
        }
        return test.grpc.grpc.XFileCmdsByContent.getDefaultInstance();
      } else {
        if (FilecmdCase_ == 1) {
          return filecontentBuilder_.getMessage();
        }
        return test.grpc.grpc.XFileCmdsByContent.getDefaultInstance();
      }
    }
    /**
     * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
     */
    public Builder setFilecontent(test.grpc.grpc.XFileCmdsByContent value) {
      if (filecontentBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        Filecmd_ = value;
        onChanged();
      } else {
        filecontentBuilder_.setMessage(value);
      }
      FilecmdCase_ = 1;
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
     */
    public Builder setFilecontent(
        test.grpc.grpc.XFileCmdsByContent.Builder builderForValue) {
      if (filecontentBuilder_ == null) {
        Filecmd_ = builderForValue.build();
        onChanged();
      } else {
        filecontentBuilder_.setMessage(builderForValue.build());
      }
      FilecmdCase_ = 1;
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
     */
    public Builder mergeFilecontent(test.grpc.grpc.XFileCmdsByContent value) {
      if (filecontentBuilder_ == null) {
        if (FilecmdCase_ == 1 &&
            Filecmd_ != test.grpc.grpc.XFileCmdsByContent.getDefaultInstance()) {
          Filecmd_ = test.grpc.grpc.XFileCmdsByContent.newBuilder((test.grpc.grpc.XFileCmdsByContent) Filecmd_)
              .mergeFrom(value).buildPartial();
        } else {
          Filecmd_ = value;
        }
        onChanged();
      } else {
        if (FilecmdCase_ == 1) {
          filecontentBuilder_.mergeFrom(value);
        }
        filecontentBuilder_.setMessage(value);
      }
      FilecmdCase_ = 1;
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
     */
    public Builder clearFilecontent() {
      if (filecontentBuilder_ == null) {
        if (FilecmdCase_ == 1) {
          FilecmdCase_ = 0;
          Filecmd_ = null;
          onChanged();
        }
      } else {
        if (FilecmdCase_ == 1) {
          FilecmdCase_ = 0;
          Filecmd_ = null;
        }
        filecontentBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
     */
    public test.grpc.grpc.XFileCmdsByContent.Builder getFilecontentBuilder() {
      return getFilecontentFieldBuilder().getBuilder();
    }
    /**
     * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
     */
    @java.lang.Override
    public test.grpc.grpc.XFileCmdsByContentOrBuilder getFilecontentOrBuilder() {
      if ((FilecmdCase_ == 1) && (filecontentBuilder_ != null)) {
        return filecontentBuilder_.getMessageOrBuilder();
      } else {
        if (FilecmdCase_ == 1) {
          return (test.grpc.grpc.XFileCmdsByContent) Filecmd_;
        }
        return test.grpc.grpc.XFileCmdsByContent.getDefaultInstance();
      }
    }
    /**
     * <code>.rpc_package.XFileCmdsByContent filecontent = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        test.grpc.grpc.XFileCmdsByContent, test.grpc.grpc.XFileCmdsByContent.Builder, test.grpc.grpc.XFileCmdsByContentOrBuilder> 
        getFilecontentFieldBuilder() {
      if (filecontentBuilder_ == null) {
        if (!(FilecmdCase_ == 1)) {
          Filecmd_ = test.grpc.grpc.XFileCmdsByContent.getDefaultInstance();
        }
        filecontentBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            test.grpc.grpc.XFileCmdsByContent, test.grpc.grpc.XFileCmdsByContent.Builder, test.grpc.grpc.XFileCmdsByContentOrBuilder>(
                (test.grpc.grpc.XFileCmdsByContent) Filecmd_,
                getParentForChildren(),
                isClean());
        Filecmd_ = null;
      }
      FilecmdCase_ = 1;
      onChanged();
      return filecontentBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        test.grpc.grpc.XFileCmdsByPath, test.grpc.grpc.XFileCmdsByPath.Builder, test.grpc.grpc.XFileCmdsByPathOrBuilder> filepathBuilder_;
    /**
     * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
     * @return Whether the filepath field is set.
     */
    @java.lang.Override
    public boolean hasFilepath() {
      return FilecmdCase_ == 2;
    }
    /**
     * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
     * @return The filepath.
     */
    @java.lang.Override
    public test.grpc.grpc.XFileCmdsByPath getFilepath() {
      if (filepathBuilder_ == null) {
        if (FilecmdCase_ == 2) {
          return (test.grpc.grpc.XFileCmdsByPath) Filecmd_;
        }
        return test.grpc.grpc.XFileCmdsByPath.getDefaultInstance();
      } else {
        if (FilecmdCase_ == 2) {
          return filepathBuilder_.getMessage();
        }
        return test.grpc.grpc.XFileCmdsByPath.getDefaultInstance();
      }
    }
    /**
     * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
     */
    public Builder setFilepath(test.grpc.grpc.XFileCmdsByPath value) {
      if (filepathBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        Filecmd_ = value;
        onChanged();
      } else {
        filepathBuilder_.setMessage(value);
      }
      FilecmdCase_ = 2;
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
     */
    public Builder setFilepath(
        test.grpc.grpc.XFileCmdsByPath.Builder builderForValue) {
      if (filepathBuilder_ == null) {
        Filecmd_ = builderForValue.build();
        onChanged();
      } else {
        filepathBuilder_.setMessage(builderForValue.build());
      }
      FilecmdCase_ = 2;
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
     */
    public Builder mergeFilepath(test.grpc.grpc.XFileCmdsByPath value) {
      if (filepathBuilder_ == null) {
        if (FilecmdCase_ == 2 &&
            Filecmd_ != test.grpc.grpc.XFileCmdsByPath.getDefaultInstance()) {
          Filecmd_ = test.grpc.grpc.XFileCmdsByPath.newBuilder((test.grpc.grpc.XFileCmdsByPath) Filecmd_)
              .mergeFrom(value).buildPartial();
        } else {
          Filecmd_ = value;
        }
        onChanged();
      } else {
        if (FilecmdCase_ == 2) {
          filepathBuilder_.mergeFrom(value);
        }
        filepathBuilder_.setMessage(value);
      }
      FilecmdCase_ = 2;
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
     */
    public Builder clearFilepath() {
      if (filepathBuilder_ == null) {
        if (FilecmdCase_ == 2) {
          FilecmdCase_ = 0;
          Filecmd_ = null;
          onChanged();
        }
      } else {
        if (FilecmdCase_ == 2) {
          FilecmdCase_ = 0;
          Filecmd_ = null;
        }
        filepathBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
     */
    public test.grpc.grpc.XFileCmdsByPath.Builder getFilepathBuilder() {
      return getFilepathFieldBuilder().getBuilder();
    }
    /**
     * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
     */
    @java.lang.Override
    public test.grpc.grpc.XFileCmdsByPathOrBuilder getFilepathOrBuilder() {
      if ((FilecmdCase_ == 2) && (filepathBuilder_ != null)) {
        return filepathBuilder_.getMessageOrBuilder();
      } else {
        if (FilecmdCase_ == 2) {
          return (test.grpc.grpc.XFileCmdsByPath) Filecmd_;
        }
        return test.grpc.grpc.XFileCmdsByPath.getDefaultInstance();
      }
    }
    /**
     * <code>.rpc_package.XFileCmdsByPath filepath = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        test.grpc.grpc.XFileCmdsByPath, test.grpc.grpc.XFileCmdsByPath.Builder, test.grpc.grpc.XFileCmdsByPathOrBuilder> 
        getFilepathFieldBuilder() {
      if (filepathBuilder_ == null) {
        if (!(FilecmdCase_ == 2)) {
          Filecmd_ = test.grpc.grpc.XFileCmdsByPath.getDefaultInstance();
        }
        filepathBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            test.grpc.grpc.XFileCmdsByPath, test.grpc.grpc.XFileCmdsByPath.Builder, test.grpc.grpc.XFileCmdsByPathOrBuilder>(
                (test.grpc.grpc.XFileCmdsByPath) Filecmd_,
                getParentForChildren(),
                isClean());
        Filecmd_ = null;
      }
      FilecmdCase_ = 2;
      onChanged();
      return filepathBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        test.grpc.grpc.XFileCmdsByID, test.grpc.grpc.XFileCmdsByID.Builder, test.grpc.grpc.XFileCmdsByIDOrBuilder> fileidBuilder_;
    /**
     * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
     * @return Whether the fileid field is set.
     */
    @java.lang.Override
    public boolean hasFileid() {
      return FilecmdCase_ == 3;
    }
    /**
     * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
     * @return The fileid.
     */
    @java.lang.Override
    public test.grpc.grpc.XFileCmdsByID getFileid() {
      if (fileidBuilder_ == null) {
        if (FilecmdCase_ == 3) {
          return (test.grpc.grpc.XFileCmdsByID) Filecmd_;
        }
        return test.grpc.grpc.XFileCmdsByID.getDefaultInstance();
      } else {
        if (FilecmdCase_ == 3) {
          return fileidBuilder_.getMessage();
        }
        return test.grpc.grpc.XFileCmdsByID.getDefaultInstance();
      }
    }
    /**
     * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
     */
    public Builder setFileid(test.grpc.grpc.XFileCmdsByID value) {
      if (fileidBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        Filecmd_ = value;
        onChanged();
      } else {
        fileidBuilder_.setMessage(value);
      }
      FilecmdCase_ = 3;
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
     */
    public Builder setFileid(
        test.grpc.grpc.XFileCmdsByID.Builder builderForValue) {
      if (fileidBuilder_ == null) {
        Filecmd_ = builderForValue.build();
        onChanged();
      } else {
        fileidBuilder_.setMessage(builderForValue.build());
      }
      FilecmdCase_ = 3;
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
     */
    public Builder mergeFileid(test.grpc.grpc.XFileCmdsByID value) {
      if (fileidBuilder_ == null) {
        if (FilecmdCase_ == 3 &&
            Filecmd_ != test.grpc.grpc.XFileCmdsByID.getDefaultInstance()) {
          Filecmd_ = test.grpc.grpc.XFileCmdsByID.newBuilder((test.grpc.grpc.XFileCmdsByID) Filecmd_)
              .mergeFrom(value).buildPartial();
        } else {
          Filecmd_ = value;
        }
        onChanged();
      } else {
        if (FilecmdCase_ == 3) {
          fileidBuilder_.mergeFrom(value);
        }
        fileidBuilder_.setMessage(value);
      }
      FilecmdCase_ = 3;
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
     */
    public Builder clearFileid() {
      if (fileidBuilder_ == null) {
        if (FilecmdCase_ == 3) {
          FilecmdCase_ = 0;
          Filecmd_ = null;
          onChanged();
        }
      } else {
        if (FilecmdCase_ == 3) {
          FilecmdCase_ = 0;
          Filecmd_ = null;
        }
        fileidBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
     */
    public test.grpc.grpc.XFileCmdsByID.Builder getFileidBuilder() {
      return getFileidFieldBuilder().getBuilder();
    }
    /**
     * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
     */
    @java.lang.Override
    public test.grpc.grpc.XFileCmdsByIDOrBuilder getFileidOrBuilder() {
      if ((FilecmdCase_ == 3) && (fileidBuilder_ != null)) {
        return fileidBuilder_.getMessageOrBuilder();
      } else {
        if (FilecmdCase_ == 3) {
          return (test.grpc.grpc.XFileCmdsByID) Filecmd_;
        }
        return test.grpc.grpc.XFileCmdsByID.getDefaultInstance();
      }
    }
    /**
     * <code>.rpc_package.XFileCmdsByID fileid = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        test.grpc.grpc.XFileCmdsByID, test.grpc.grpc.XFileCmdsByID.Builder, test.grpc.grpc.XFileCmdsByIDOrBuilder> 
        getFileidFieldBuilder() {
      if (fileidBuilder_ == null) {
        if (!(FilecmdCase_ == 3)) {
          Filecmd_ = test.grpc.grpc.XFileCmdsByID.getDefaultInstance();
        }
        fileidBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            test.grpc.grpc.XFileCmdsByID, test.grpc.grpc.XFileCmdsByID.Builder, test.grpc.grpc.XFileCmdsByIDOrBuilder>(
                (test.grpc.grpc.XFileCmdsByID) Filecmd_,
                getParentForChildren(),
                isClean());
        Filecmd_ = null;
      }
      FilecmdCase_ = 3;
      onChanged();
      return fileidBuilder_;
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


    // @@protoc_insertion_point(builder_scope:rpc_package.XFileCmd)
  }

  // @@protoc_insertion_point(class_scope:rpc_package.XFileCmd)
  private static final test.grpc.grpc.XFileCmd DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new test.grpc.grpc.XFileCmd();
  }

  public static test.grpc.grpc.XFileCmd getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<XFileCmd>
      PARSER = new com.google.protobuf.AbstractParser<XFileCmd>() {
    @java.lang.Override
    public XFileCmd parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new XFileCmd(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<XFileCmd> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<XFileCmd> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public test.grpc.grpc.XFileCmd getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
