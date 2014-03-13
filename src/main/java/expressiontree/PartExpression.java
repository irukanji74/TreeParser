package expressiontree;
/**
 * Class to hold a member of  expression with a sign
 */
public class PartExpression {

	public boolean up;
	public Node expr;
	
	public PartExpression(Node expression, boolean up){
		this.expr = expression;
		this.up = up;
	}
}
