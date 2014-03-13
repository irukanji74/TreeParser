package graphchartapplic;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import parserutilities.SetValue;
import parserutilities.TreeParser;
import expressiontree.Node;

public class TreeParserTest extends Assert{

	Node expr;
	SetValue setVal;
	TreeParser parser;
	double resulted;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		parser = new TreeParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNoVar() {

		expr = parser.parse("7+3");
		double expectedExpression = 10.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testNoVar", expectedExpression, resulted);
	}
	
	@Test
	public void testConstPow() {
		
		expr = parser.parse("7+3^3");
		double expectedExpression = 34.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testConstPow", expectedExpression, resulted);
	}
	
	@Test
	public void testConstPowMult() {
		
		expr = parser.parse("7+3^2*4");
		double expectedExpression = 43.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testConstPowMult", expectedExpression, resulted);
	}
	
	@Test
	public void testConstPowDiv() {
		
		expr = parser.parse("7+3^2/3");
		double expectedExpression = 10.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testConstPowMult", expectedExpression, resulted);
	}
	
	@Test
	public void testVar() {

		expr = parser.parse("7+3*x");
		double expectedExpression = 25.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testVar", expectedExpression, resulted);
	}
	
	@Test
	public void testVarPow() {

		expr = parser.parse("7+x^2");
		double expectedExpression = 43.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testVarPow", expectedExpression, resulted);
	}

	@Test
	public void testVarPowMul() {

		expr = parser.parse("7+x^2*2.2");
		double expectedExpression = 86.2;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testVarPowMul", expectedExpression, resulted);
	}
	
	@Test
	public void testVarPowDiv() {

		expr = parser.parse("7+x^2/9");
		double expectedExpression = 11.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testVarPowDiv", expectedExpression, resulted);
	}
	
	@Test
	public void testVarpow() {

		expr = parser.parse("x^3*x^2/x^4");
		double expectedExpression = 2.0;
		expr.accept(new SetValue("x", 2.0));
		resulted = expr.getValue();
		assertEquals("testVarPowDiv", expectedExpression, resulted);
	}
	
	@Test
	public void testVarBrack1() {

		expr = parser.parse("7+(x^2+4)-x^3");
		double expectedExpression = -169.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testVarBrack1", expectedExpression, resulted);
	}
	
	@Test
	public void testVarBrack2() {

		expr = parser.parse("7+(x^2+4)*(5+x^2)");
		double expectedExpression = 1647.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testVarBrack2", expectedExpression, resulted);
	}
	
	@Test
	public void testVarBrack3() {

		expr = parser.parse("(x^2*2)/(3+x)");
		double expectedExpression = 8.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testVarBrack3", expectedExpression, resulted);
	}
	
	@Test
	public void testVarBrack4() {

		expr = parser.parse("x*(3+x/(x-3))");
		double expectedExpression = 30.0;
		expr.accept(new SetValue("x", 6.0));
		resulted = expr.getValue();
		assertEquals("testVarBrack4", expectedExpression, resulted);
	}
	
	@Test
	public void testVarBrack5() {

		expr = parser.parse("sqrt(x^3/x*2-x^2)");
		double expectedExpression = 4.0;
		expr.accept(new SetValue("x", 4.0));
		resulted = expr.getValue();
		assertEquals("testVarBrack5", expectedExpression, resulted);
	}
	
	@Test
	public void testVarBrack6() {

		expr = parser.parse("sqrt(sqrt(x*sqrt(x*2+8)))");
		double expectedExpression = 2.0;
		expr.accept(new SetValue("x", 4.0));
		resulted = expr.getValue();
		assertEquals("testVarBrack6", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc1() {

		expr = parser.parse("7+sin(pi)");
		double expectedExpression = 7.0;
		expr.accept(new SetValue("pi", Math.PI));
		resulted = expr.getValue();
		assertEquals("testVarFunc1", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc2() {

		expr = parser.parse("7+cos(pi)");
		double expectedExpression = 6.0;
		expr.accept(new SetValue("pi", Math.PI));
		resulted = expr.getValue();
		assertEquals("testFunc2", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc3() {

		expr = parser.parse("tan(pi/4)+sin(pi)");
		double expectedExpression = 1.0;
		expr.accept(new SetValue("pi", Math.PI));
		resulted = expr.getValue();
		assertEquals("testFunc3", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc4() {

		expr = parser.parse("tan(pi/4)+sin(pi)");
		double expectedExpression = 1.0;
		expr.accept(new SetValue("pi", Math.PI));
		resulted = expr.getValue();
		assertEquals("testFunc4", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc5() {

		expr = parser.parse("tan(pi/2^2)+sin(pi*(pi+1))");
		double expectedExpression = 1.4303012170000915;
		expr.accept(new SetValue("pi", Math.PI));
		resulted = expr.getValue();
		assertEquals("testFunc5", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc6() {

		expr = parser.parse("tan(pi/2^2)+sqrt(x+2)");
		double expectedExpression = 3.0;
		expr.accept(new SetValue("pi", Math.PI));
		expr.accept(new SetValue("x", 2.0));
		resulted = expr.getValue();
		assertEquals("testFunc6", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc7() {

		expr = parser.parse("sqrt(x^2+x^3)+sin(pi)");
		double expectedExpression = 3.4641016151377544;
		expr.accept(new SetValue("x", 2.0));
		expr.accept(new SetValue("pi", Math.PI));
		resulted = expr.getValue();
		assertEquals("testFunc7", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc8() {

		expr = parser.parse("sqrt(x^2+x^3)+cos(pi)");
		double expectedExpression = 2.4641016151377544;
		expr.accept(new SetValue("x", 2.0));
		expr.accept(new SetValue("pi", Math.PI));
		resulted = expr.getValue();
		assertEquals("testFunc8", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc9() {

		expr = parser.parse("sin(pi/4)^2");
		double expectedExpression = 0.4999999999999999;
		expr.accept(new SetValue("x", 2.0));
		expr.accept(new SetValue("pi", Math.PI));
		resulted = expr.getValue();
		assertEquals("testFunc9", expectedExpression, resulted);
	}
	
	@Test
	public void testFunc10() {

		expr = parser.parse("sqrt(x^2)+cos(pi^2)-tan(pi)");
		double expectedExpression = 1.0973146380669287;
		expr.accept(new SetValue("x", 2.0));
		expr.accept(new SetValue("pi", Math.PI));
		resulted = expr.getValue();
		assertEquals("testFunc10", expectedExpression, resulted);
	}
}
