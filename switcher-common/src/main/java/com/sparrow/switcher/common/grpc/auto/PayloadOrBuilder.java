// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: sparrow_grpc_service.proto

package com.sparrow.switcher.common.grpc.auto;

public interface PayloadOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Payload)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.Metadata metadata = 2;</code>
   */
  boolean hasMetadata();
  /**
   * <code>.Metadata metadata = 2;</code>
   */
  com.sparrow.switcher.common.grpc.auto.Metadata getMetadata();
  /**
   * <code>.Metadata metadata = 2;</code>
   */
  com.sparrow.switcher.common.grpc.auto.MetadataOrBuilder getMetadataOrBuilder();

  /**
   * <code>.google.protobuf.Any body = 3;</code>
   */
  boolean hasBody();
  /**
   * <code>.google.protobuf.Any body = 3;</code>
   */
  com.google.protobuf.Any getBody();
  /**
   * <code>.google.protobuf.Any body = 3;</code>
   */
  com.google.protobuf.AnyOrBuilder getBodyOrBuilder();
}