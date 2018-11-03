package binar.box.dto;

import lombok.Data;

@Data
public class LockDTO {

	private Long id;

	private String message;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private String lockColor;

	private Long lockSection;

	private Long lockType;

	private Long lockTypeTemplate;

	private Long panelId;

	private Boolean privateLock;
}
