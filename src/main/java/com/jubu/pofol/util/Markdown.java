package com.jubu.pofol.util;

import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Link;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public final class Markdown {

	private static final List<Extension> EXTENSIONS = List.of(
			TablesExtension.create(),
			StrikethroughExtension.create(),
			AutolinkExtension.create()
	);

	private static final Parser PARSER = Parser.builder()
			.extensions(EXTENSIONS)
			.build();

	private static final HtmlRenderer RENDERER = HtmlRenderer.builder()
			.extensions(EXTENSIONS)
			.escapeHtml(true)
			.sanitizeUrls(true)
			.softbreak("<br>\n")
			.attributeProviderFactory(context -> (node, tagName, attributes) -> {
				if (node instanceof Link) {
					attributes.put("target", "_blank");
					attributes.put("rel", "noopener noreferrer");
				}
			})
			.build();

	private Markdown() {}

	public static String toHtml(String markdown) {
		if (markdown == null || markdown.isBlank()) {
			return "";
		}

		return RENDERER.render(PARSER.parse(markdown));
	}
}
