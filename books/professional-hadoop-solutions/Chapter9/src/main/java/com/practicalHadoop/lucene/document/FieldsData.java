/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.practicalHadoop.lucene.document;

@SuppressWarnings("all")
public class FieldsData extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = org.apache.avro.Schema.parse("{\"type\":\"record\",\"name\":\"FieldsData\",\"namespace\":\"com.practicalHadoop.lucene.document\",\"fields\":[{\"name\":\"fieldsArray\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"singleField\",\"fields\":[{\"name\":\"binary\",\"type\":\"boolean\"},{\"name\":\"data\",\"type\":[\"string\",\"bytes\"]}]}}}]}");
  public java.util.List<com.practicalHadoop.lucene.document.singleField> fieldsArray;
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return fieldsArray;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: fieldsArray = (java.util.List<com.practicalHadoop.lucene.document.singleField>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
}
