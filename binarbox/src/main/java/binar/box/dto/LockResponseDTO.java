package binar.box.dto;

import binar.box.domain.Lock;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public class LockResponseDTO {

	private long id;

	private Double longitude;

	private Double latitude;

	private String message;

	private Integer fontSize;

	private String fontStyle;

	private String fontColor;

	private String lockSection;

	private String lockType;

	private long panelId;

	public LockResponseDTO(Lock lock) {
		this.id = lock.getId();
		this.longitude = lock.getLongitude();
		this.latitude = lock.getLatitude();
		this.message = lock.getMessage();
		this.fontSize = lock.getFontSize();
		this.fontStyle = lock.getFontStyle();
		this.fontColor = lock.getFontColor();
		this.lockSection = lock.getLockSection().getSection();
		this.lockType = lock.getLockType().getType();
		this.panelId = lock.getPanel().getId();

	}

	public LockResponseDTO() {
	}

	public long getPanelId() {
		return panelId;
	}

	public void setPanelId(long panelId) {
		this.panelId = panelId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getLockSection() {
		return lockSection;
	}

	public void setLockSection(String lockSection) {
		this.lockSection = lockSection;
	}

	public String getLockType() {
		return lockType;
	}

	public void setLockType(String lockType) {
		this.lockType = lockType;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
}
