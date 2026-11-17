package com.jubu.pofol.domain.model;

import com.jubu.pofol.domain.CustomException;
import com.jubu.pofol.util.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(name = "bg_color", nullable = false, length = 10)
	private String bgColor;
	
	@Column(name = "t_color", nullable = false, length = 10)
	private String tColor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_group_id", nullable = false)
	private TagGroup tagGroup;
	
	public static Tag create(String name, String bgColor, String tColor, TagGroup tagGroup) {
		Check.notBlank(name, () -> new CustomException("tag name is null"));
		
		Check.notBlank(bgColor, () -> new CustomException("tag background color is null"));
		
		Check.notBlank(tColor, () -> new CustomException("tag text color is null"));
		
		Check.notNull(tagGroup, () -> new CustomException("tag group is null"));
		
		Tag tag = new Tag();
		
		tag.name = name;
		tag.bgColor = bgColor;
		tag.tColor = tColor;
		tag.tagGroup = tagGroup;
		
		return tag;
	}
	
	public void changeBgColor(String bgColor) {
		Check.notBlank(bgColor, () -> new CustomException("tag background color is null"));
		
		this.bgColor = bgColor;
	}
	
	public void changeTColor(String tColor) {
		Check.notBlank(tColor, () -> new CustomException("tag text color is null"));
		
		this.tColor = tColor;
	}
}
