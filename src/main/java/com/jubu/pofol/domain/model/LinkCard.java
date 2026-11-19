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
@Table(name = "link_cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LinkCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 1000)
	private String url;
	
	@Column(nullable = false, length = 50)
	private String title;
	
	@Column(nullable = false, length = 255)
	private String description;

	/** 아이콘 이미지 경로 또는 URL (없으면 화면에서 기본 아이콘 표시) */
	@Column(length = 1000)
	private String icon;

	public static LinkCard create(String url, String title, String description, String icon) {
		Check.notBlank(url, () -> new CustomException("linkcard url is null"));
		
		Check.notBlank(title, () -> new CustomException("linkcard title is null"));
		
		Check.notBlank(description, () -> new CustomException("linkcard description is null"));
		
		LinkCard linkCard = new LinkCard();
		
		linkCard.url = url;
		linkCard.title = title;
		linkCard.description = description;
		linkCard.icon = blankToNull(icon);

		return linkCard;
	}

	/** 빈 문자열("")을 보내면 아이콘 제거 → 기본 아이콘 */
	public void changeIcon(String icon) {
		this.icon = blankToNull(icon);
	}

	private static String blankToNull(String value) {
		return (value == null || value.isBlank()) ? null : value;
	}
	
	public void changeUrl(String url) {
		Check.notBlank(url, () -> new CustomException("linkcard url is null"));
		
		this.url = url;
	}
	
	public void changeTitle(String title) {
		Check.notBlank(title, () -> new CustomException("linkcard title is null"));
		
		this.title = title;
	}
	
	public void changeDescription(String description) {
		Check.notBlank(description, () -> new CustomException("linkcard description is null"));
		
		this.description = description;
	}
}
