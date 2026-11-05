package com.bharat.streamly.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "users")
@Getter
@AllArgsConstructor
public class UserDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String username;
}
