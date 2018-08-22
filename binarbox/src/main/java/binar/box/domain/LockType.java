package binar.box.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by Timis Nicu Alexandru on 16-Apr-18.
 */
@Entity
@Table(name = "lock_type")
public class LockType extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "lock_type")
	private String type;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockType")
	private List<File> files;

	@Column(name = "total_rating")
	private Float totalRating;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lockType")
	private List<LockTypeTemplate> lockTypeTemplate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lock_type_price")
	private LockTypePrice price;

	public LockType() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public Float getTotalRating() {
		return totalRating;
	}

	public void setTotalRating(Float totalRating) {
		this.totalRating = totalRating;
	}

	public LockTypePrice getPrice() {
		return price;
	}

	public void setPrice(LockTypePrice price) {
		this.price = price;
	}

	public List<LockTypeTemplate> getLockTypeTemplate() {
		return lockTypeTemplate;
	}

	public void setLockTypeTemplate(List<LockTypeTemplate> lockTypeTemplate) {
		this.lockTypeTemplate = lockTypeTemplate;
	}

}
