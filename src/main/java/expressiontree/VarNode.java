package expressiontree;

import parserutilities.NodeVisitor;

public class VarNode implements Node {

	private String name;
	private double value;
	private boolean isSet;

	public VarNode(String name) {
		this.name = name;
		isSet = false;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return Node.VAR_NODE;
	}

	public void setValue(double value) {
		this.value = value;
		isSet = true;
	}

	public double getValue() {
		if (isSet)
			return value;
		else
			System.out.println("Variable '" + name + "' was not initialized.");
		return 0.0;
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

}
