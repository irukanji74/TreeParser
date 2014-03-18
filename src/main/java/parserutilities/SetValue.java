package parserutilities;

import expressiontree.VarNode;

public class SetValue extends AbstractNodeVisitor{

	private String name;
	private double value;
	
	public SetValue(String name, double value){
		this.name = name;
		this.value = value;
	}
	public void visit(VarNode node) {

		if(node.getName().equals(name))
			node.setValue(value);
	}
}
