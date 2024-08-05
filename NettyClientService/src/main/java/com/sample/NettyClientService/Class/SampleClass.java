package com.sample.NettyClientService.Class;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class SampleClass {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("NAME")
	private String NAME;
	
	@JsonProperty("AGE")
	private int AGE;
	
	@JsonProperty("BIRTH")
	private String BIRTH;

    public String toStream() {
        StringBuilder result = new StringBuilder();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (!field.isSynthetic() && !java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value != null) {
                        result.append(value.toString()).append(":");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }
}
