package parserutilities;

import expressiontree.AddSubNode;
import expressiontree.ConstantNode;
import expressiontree.FunctionNode;
import expressiontree.MulDivNode;
import expressiontree.PowNode;
import expressiontree.VarNode;

public class SetValue implements NodeVisitor{

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
	public void visit(AddSubNode node) {
		// TODO Auto-generated method stub
		
	}
	public void visit(ConstantNode node) {
		// TODO Auto-generated method stub
		
	}
	public void visit(FunctionNode node) {
		// TODO Auto-generated method stub
		
	}
	public void visit(MulDivNode node) {
		// TODO Auto-generated method stub
		
	}
	public void visit(PowNode node) {
		// TODO Auto-generated method stub
		
	}

}
