package com.piccritic.website.post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vaadin.tokenfield.TokenField;

import com.piccritic.compute.MasterService;
import com.piccritic.database.tag.Tag;
import com.vaadin.ui.VerticalLayout;

public class TagChooser extends VerticalLayout {
	static String caption = "Tags";
	private Collection<Tag> tags;
	
	private QueryToken tokens;
	
	private class QueryToken extends TokenField {
		public QueryToken() {
			tags = MasterService.tagService.findTags("");
			for (Tag t : tags) {
				cb.addItem(t.getTag());
			}
		}
		
		public List<String> getTags() {
			List<String> tags = new ArrayList<String>();
			for (Object o : buttons.keySet()) {
				tags.add(o.toString());
			}
			return tags;
		}
	}
	
	public TagChooser() {
		tokens = new QueryToken();
		addComponent(tokens);
	}
	
	public List<String> getTags() {
		return tokens.getTags();
	}

	
	
	
}
