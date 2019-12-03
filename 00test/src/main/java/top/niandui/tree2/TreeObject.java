package top.niandui.tree2;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeObject extends BaseTree<String, TreeObject> {
	private String id;
	private String parentId;
    private List<TreeObject> children;

	public TreeObject(String id, String parentId) {
		this.id = id;
		this.parentId = parentId;
	}

	@Override
	public String getTreeId() {
		return id;
	}

	@Override
	public String getTreeParentId() {
		return parentId;
	}
}
