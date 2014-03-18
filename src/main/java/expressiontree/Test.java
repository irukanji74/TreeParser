package expressiontree;


import parserutilities.SetValue;
import parserutilities.TreeParser;

public class Test {

	public static void main(String[] args) {

		TreeParser parser = new TreeParser();
		try {
			Node expr = parser.parse("2*(1+sin(x/2))^2");
			expr.accept(new SetValue("x", 6.0));
			System.out.println("The value of the expression is " + expr.getValue());

		} catch (ParserException e) {
			System.out.println(e.getMessage());
		}

	}
}
