package parserutilities;

import java.util.LinkedList;

import javax.swing.tree.VariableHeightLayoutCache;

import expressiontree.AddSubNode;
import expressiontree.ConstantNode;
import expressiontree.FunctionNode;
import expressiontree.MulDivNode;
import expressiontree.Node;
import expressiontree.PartExpression;
import expressiontree.PowNode;
import expressiontree.VarNode;

public class TreeParser {

	private LinkedList<ExpressionUnit> units;// saves all separated units of an
												// expression
	ExpressionUnit nextUnit;// saves next unit iterated

	private void nextToken() {
		units.pop();
		// at the end of input we return an epsilon token
		if (units.isEmpty())
			nextUnit = new ExpressionUnit(ExpressionUnit.FINISH, "", -1);
		else
			nextUnit = units.getFirst();
	}

	public Node parse(String expression) {
		UnitParser unitParser = UnitParser.getUnitParser();
		unitParser.separate(expression);
		LinkedList<ExpressionUnit> exprUnits = unitParser.getUnits();
		return this.parse(exprUnits);

	}

	public Node parse(LinkedList<ExpressionUnit> exprUnits) {
		this.units = new LinkedList<ExpressionUnit>(exprUnits);
		this.nextUnit = units.getFirst();

		Node expression = expression();
		if (nextUnit.unit != ExpressionUnit.FINISH) {
			System.out.printf("Unexpected symbol %s found", nextUnit);
		}
		return expression;
	}

	// top level node
	private Node expression() {

		Node exp = signedMember();
		exp = sumMembers(exp);
		return exp;
	}

	// sum(+ or -) of all members of expression
	private Node sumMembers(Node exp) {

		if (nextUnit.unit == ExpressionUnit.PLUSMINUS) {
			AddSubNode sum;
			if (exp.getType() == Node.ADDSUB_NODE)
				sum = (AddSubNode) exp;
			else
				sum = new AddSubNode(exp, true);

			boolean up = nextUnit.sequence.equals("+");
			nextToken();
			Node t = term();
			sum.add(t, up);

			return sumMembers(sum);
		}
		return exp;
	}

	// checking for sign of member
	private Node signedMember() {

		if (nextUnit.unit == ExpressionUnit.PLUSMINUS) {
			boolean up = nextUnit.sequence.equals("+");
			nextToken();
			Node t = term();
			if (up)
				return t;
			else
				return new AddSubNode(t, false);
		}
		return term();
	}

	private Node term() {

		Node f = factor();
		return termMembers(f);
	}

	private Node termMembers(Node expr) {

		if (nextUnit.unit == ExpressionUnit.MULDIV) {
			MulDivNode product;
			if (expr.getType() == Node.MULDIV_NODE)
				product = (MulDivNode) expr;
			else
				product = new MulDivNode(expr, true);
			boolean up = nextUnit.sequence.equals("*");
			nextToken();
			Node f = factor();
			product.add(f, up);

			return termMembers(product);
		}

		return expr;
	}

	private Node factor() {

		Node a = argument();
		return factorMember(a);
	}

	private Node factorMember(Node expr) {
		if (nextUnit.unit == ExpressionUnit.RAISED) {
			nextToken();
			Node exponent = factor();

			return new PowNode(expr, exponent);
		}
		return expr;
	}

	private Node argument() {

		if (nextUnit.unit == ExpressionUnit.FUNCTION) {
			int functionId = FunctionNode.stringToFunction(nextUnit.sequence);
			if (functionId < 0)
				System.out.printf("Unexpected Function %s found", nextUnit);
			nextToken();
			Node expr = argument();
			return new FunctionNode(functionId, expr);
		} else if (nextUnit.unit == ExpressionUnit.OPEN_BRACKET) {
			nextToken();
			Node expr = expression();
			if (nextUnit.unit != ExpressionUnit.CLOSE_BRACKET)
				System.out.println("Closing brackets expected");
			nextToken();
			return expr;

		}
		return numberVar();
	}

	private Node numberVar() {

		if (nextUnit.unit == ExpressionUnit.NUMBER) {
			Node expr = new ConstantNode(nextUnit.sequence);
			nextToken();
			return expr;
		}
		if (nextUnit.unit == ExpressionUnit.VARIABLE) {
			Node expr = new VarNode(nextUnit.sequence);
			//System.out.println(nextUnit.sequence);
			nextToken();
			return expr;
		}
		return null;
	}
}
