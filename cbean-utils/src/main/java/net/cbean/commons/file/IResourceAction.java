package net.cbean.commons.file;

/**
 * @created 2003-3-26
 * 一个函数对象，可以在某个类路径代表的资源上作某些动作
 * @see PackageUtil#processClasses
 * @author Simon.Wu
 *
 */
public interface IResourceAction {
	public void execute(String resourcepath) ;
}
