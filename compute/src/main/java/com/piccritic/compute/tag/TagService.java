package com.piccritic.compute.tag;

import java.util.List;

import com.piccritic.compute.MasterConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.tag.Tag;
import com.piccritic.database.tag.TagException;
import com.piccritic.database.user.Critic;

public class TagService implements TagInterface {

	@Override
	public void insertTag(Tag tag) throws TagException {
		MasterConnector.tagConnector.insertTag(tag);
	}

	@Override
	public List<Tag> findTags(String query) {
		if (query == null) {
			return null;
		}
		return MasterConnector.tagConnector.findTags(query);
	}

	@Override
	public boolean deleteTag(Tag tag) throws TagException {
		return MasterConnector.tagConnector.deleteTag(tag);
	}

	@Override
	public List<Post> findPosts(List<String> tags, Critic critic) {
		return MasterConnector.tagConnector.findPosts(tags, critic);
	}
	
}
