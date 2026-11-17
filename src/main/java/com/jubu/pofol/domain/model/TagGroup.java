package com.jubu.pofol.domain.model;

import com.jubu.pofol.domain.CustomException;
import com.jubu.pofol.util.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 30)
	private String name;
	
	public static TagGroup create(String name) {
		Check.notBlank(name, () -> new CustomException("tag group name is null"));
		
		TagGroup tagGroup = new TagGroup();
		
		tagGroup.name = name;
		
		return tagGroup;
	}
	
	public void changeName(String name) {
		Check.notBlank(name, () -> new CustomException("tag group name is null"));
		
		this.name = name;
	}
	
}
