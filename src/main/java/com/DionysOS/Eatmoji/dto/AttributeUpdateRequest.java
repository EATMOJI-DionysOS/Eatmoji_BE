package com.DionysOS.Eatmoji.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttributeUpdateRequest { // JSON 파일 내에서 DTO에서 category, flavor, disease, allergy에 nested braces로 감싸서 안에 add, remove를 넣을 수 있게 하는 class
    private List<String> add;
    private List<String> remove;
}
