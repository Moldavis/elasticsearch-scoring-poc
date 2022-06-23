package com.example.demo.document;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.elasticsearch.annotations.Setting;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "documents")
@Builder
@FieldNameConstants
@Setting(settingPath = "/elasticsearch/document_setting.json")
public class Document {
	private String firstField;
	private String secondField;
	private String thirdField;
}
