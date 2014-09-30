package net.cbean.commons.file;

import java.util.Collection;

public class ResourceGatherAction implements IResourceAction {
	private Collection<String> collection;
	
	public ResourceGatherAction(Collection<String> res) {
		collection = res;
	}

	@SuppressWarnings("unchecked")
	public void execute(String resourcepath) {
		resourcepath=resourcepath.replace('\\','/');
		collection.add(resourcepath);
	}

}
