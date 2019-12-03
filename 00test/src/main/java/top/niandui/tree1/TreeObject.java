package top.niandui.tree1;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeObject extends BaseTree<String, TreeObject> {
	private String id;
	private String parentId;

	private List<TreeObject> children = new ArrayList<>();

	public TreeObject() {
	}

	public TreeObject(String id, String parentId) {
		this.id = id;
		this.parentId = parentId;
	}

	@Override
	public void setting() {
		setGetIdMethod(this::getId);
		setGetParentIdMethod(this::getParentId);
		setGetChildrenMethod(this::getChildren);
		setSetChildrenMethod(this::setChildren);
	}
}
