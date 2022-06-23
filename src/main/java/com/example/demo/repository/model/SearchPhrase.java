package com.example.demo.repository.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchPhrase {
	private String firstField;
	private String secondField;
	private String thirdField;
}
