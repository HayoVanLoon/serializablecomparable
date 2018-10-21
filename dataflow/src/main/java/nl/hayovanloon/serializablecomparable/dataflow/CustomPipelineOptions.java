package nl.hayovanloon.serializablecomparable.dataflow;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Default;


interface CustomPipelineOptions extends DataflowPipelineOptions {

  String getDataBucket();

  void setDataBucket(String tempBucket);

  @Default.Long(100)
  Long getSize();

  void setSize(Long size);

  String getCoder();

  void setCoder(String coder);
}
