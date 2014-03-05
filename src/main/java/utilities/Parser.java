package utilities;

/***
Math Expression parser
Description: 
This Program is a simple Math expression evalutaor written in java.
*/

import java.util.*;

/**
* @author Tapas
* 
*/
public class Parser {
public static void main(String[] args) {
 Scanner in = new Scanner(System.in);
 Parser prs = new Parser();
 String expr;

 System.out.println("Enter an expression and press Enter to calculate the result.");
 System.out.println("Enter an empty expression to quit.");
 System.out.println("");

 do {
  // request an expression
  System.out.print("> ");
  expr = in.nextLine();

  if (expr.length() > 0) {
   // evaluate the expression
   String result = prs.parse(expr);
   System.out.println("\t" + result);
  }
 } while (expr.length() > 0);
}

// private data
private String expr; // holds the expression
private int expr_pos; // points to the current position in expr
private char expr_c; // holds the current character from expr

private String token; // holds the token
private TOKENTYPE token_type; // type of the token

private double ans; // holds the result of the expression
private String ans_str; // holds a string containing the result of the expression

// list with variables defined by user
private Map<String, Double> user_var = new HashMap<String, Double>();

// private enumerations
private enum TOKENTYPE {
 NOTHING, DELIMETER, NUMBER, VARIABLE, FUNCTION, UNKNOWN
}

private enum OPERATOR {
 UNKNOWN, AND, OR, BITSHIFTLEFT, BITSHIFTRIGHT, 
 EQUAL, UNEQUAL, SMALLER, LARGER, SMALLEREQ, LARGEREQ, 
 PLUS, MINUS, 
 MULTIPLY, DIVIDE, MODULUS, XOR, 
 POW, 
 FACTORIAL
} 

/**
 * Initializes all data with zeros and empty strings
 */
Parser() {
 expr = "";
 expr_pos = -1;
 expr_c = '\0';

 token = "";
 token_type = TOKENTYPE.NOTHING;
}

/**
 * parses and evaluates the given expression On ParseError, an ParseError of type
 * ParseError is thrown
 */
String parse(final String new_expr) {
 try {
  // initialize all variables
  expr = new_expr; // copy the given expression to expr
  ans = 0.0;

  // get the first character in expr
  getFirstChar();
  getToken();

  // check whether the given expression is empty
  if (token_type == TOKENTYPE.DELIMETER && expr_c == '\0') {
   throw new ParseError(row(), col(), 4);
  }
  ans = parse_level1();

  // check for garbage at the end of the expression
  if (token_type != TOKENTYPE.DELIMETER || token.length() > 0) {
   if (token_type == TOKENTYPE.DELIMETER) {
    // user entered a not existing operator like "//"
    throw new ParseError(row(), col(), 101, token);
   } else {
    throw new ParseError(row(), col(), 5, token);
   }
  }

  // add the answer to memory as variable "Ans"
  user_var.put(new String("ANS"), new Double(ans));
  ans_str = String.format("Ans = %g", ans);
 } catch (ParseError err) {
  ans_str = err.get();
 }

 return ans_str;
}

/**
 * Shortcut for getting the current row value (one based) Returns the line
 * of the currently handled expression
 */
int row() {
 return -1;
}

/**
 * Shortcut for getting the current col value (one based) Returns the column
 * (position) where the last token starts
 */
int col() {
 return expr_pos - token.length() + 1;
}

/**
 * checks if the given char c is a minus
 */
boolean isMinus(final char c) {
 return c == '-';
}

/**
 * checks if the given char c is whitespace whitespace when space chr(32) or
 * tab chr(9)
 */
boolean isWhiteSpace(final char c) {
 return c == 32 || c == 9; // space or tab
}

/**
 * checks if the given char c is a delimeter minus is checked apart, can be
 * unary minus
 */
boolean isDelimeter(final char c) {
 return "&|<>=+/*%^!".indexOf(c) != -1;
}

/**
 * checks if the given char c is NO delimeter
 */
boolean isNotDelimeter(final char c) {
 return "&|<>=+-/*%^!()".indexOf(c) != -1;
}

/**
 * checks if the given char c is a letter or undersquare
 */
boolean isAlpha(final char c) {
 char cUpper = Character.toUpperCase(c);
 return "ABCDEFGHIJKLMNOPQRSTUVWXYZ_".indexOf(cUpper) != -1;
}

/**
 * checks if the given char c is a digit or dot
 */
boolean isDigitDot(final char c) {
 return "0123456789.".indexOf(c) != -1;
}

/**
 * checks if the given char c is a digit
 */
boolean isDigit(final char c) {
 return "0123456789".indexOf(c) != -1;
}

/**
 * checks if the given variable name is legal to use, i.e. not equal to
 * "pi", "e", etc.
 */
boolean isLegalVariableName(String name) {
 String nameUpper = name.toUpperCase();
 if (nameUpper.equals("E"))
  return false;
 if (nameUpper.equals("PI"))
  return false;

 return true;
}

/**
 * Get the next character from the expression. The character is stored into
 * the char expr_c. If the end of the expression is reached, the function
 * puts zero ('\0') in expr_c.
 */
void getChar() {
 expr_pos++;
 if (expr_pos < expr.length()) {
  expr_c = expr.charAt(expr_pos);
 } else {
  expr_c = '\0';
 }
}

/**
 * Get the first character from the expression. The character is stored into
 * the char expr_c. If the end of the expression is reached, the function
 * puts zero ('\0') in expr_c.
 */
void getFirstChar() {
 expr_pos = 0;
 if (expr_pos < expr.length()) {
  expr_c = expr.charAt(expr_pos);
 } else {
  expr_c = '\0';
 }
}

/***
 * Get next token in the current string expr. Uses the Parser data expr, e,
 * token, t, token_type and err
 */
void getToken() throws ParseError {
 token_type = TOKENTYPE.NOTHING;
 token = ""; // set token empty

 // skip over whitespaces
 while (isWhiteSpace(expr_c)) // space or tab
 {
  getChar();
 }

 // check for end of expression
 if (expr_c == '\0') {
  // token is empty
  token_type = TOKENTYPE.DELIMETER;
  return;
 }

 // check for minus
 if (expr_c == '-') {
  token_type = TOKENTYPE.DELIMETER;
  token += expr_c;
  getChar();
  return;
 }

 // check for parentheses
 if (expr_c == '(' || expr_c == ')') {
  token_type = TOKENTYPE.DELIMETER;
  token += expr_c;
  getChar();
  return;
 }

 // check for operators (delimeters)
 if (isDelimeter(expr_c)) {
  token_type = TOKENTYPE.DELIMETER;
  while (isDelimeter(expr_c)) {
   token += expr_c;
   getChar();
  }
  return;
 }

 // check for a value
 if (isDigitDot(expr_c)) {
  token_type = TOKENTYPE.NUMBER;
  while (isDigitDot(expr_c)) {
   token += expr_c;
   getChar();
  }

  // check for scientific notation like "2.3e-4" or "1.23e50"
  if (expr_c == 'e' || expr_c == 'E') {
   token += expr_c;
   getChar();

   if (expr_c == '+' || expr_c == '-') {
    token += expr_c;
    getChar();
   }

   while (isDigit(expr_c)) {
    token += expr_c;
    getChar();
   }
  }

  return;
 }

 // check for variables or functions
 if (isAlpha(expr_c)) {
  while (isAlpha(expr_c) || isDigit(expr_c)) {
   token += expr_c;
   getChar();
  }

  // skip whitespaces
  while (isWhiteSpace(expr_c)) // space or tab
  {
   getChar();
  }

  // check the next non-whitespace character
  if (expr_c == '(') {
   token_type = TOKENTYPE.FUNCTION;
  } else {
   token_type = TOKENTYPE.VARIABLE;
  }

  return;
 }

 // something unknown is found, wrong characters -> a syntax Error
 token_type = TOKENTYPE.UNKNOWN;
 while (expr_c != '\0') {
  token += expr_c;
  getChar();
 }

 throw new ParseError(row(), col(), 1, token);
}

/**
 * assignment of variable or function
 */
double parse_level1() throws ParseError {
 if (token_type == TOKENTYPE.VARIABLE) {
  // skip whitespaces
  while (isWhiteSpace(expr_c)) // space or tab
  {
   getChar();
  }

  // check the next non-whitespace character
  if (expr_c == '=') {
   String var_name = token;

   // get the token '='
   getToken();

   // assignment
   double ans;
   getToken();
   ans = parse_level2();

   // check whether the token is a legal name
   if (isLegalVariableName(var_name)) {
    user_var.put(var_name.toUpperCase(), new Double(ans));
   } else {
    throw new ParseError(row(), col(), 300);
   }
   return ans;
  }
 }

 return parse_level2();
}

/**
 * conditional operators and bitshift
 */
double parse_level2() throws ParseError {
 OPERATOR op_id;
 double ans;
 ans = parse_level3();

 op_id = get_operator_id(token);
 while (op_id == OPERATOR.AND || op_id == OPERATOR.OR
   || op_id == OPERATOR.BITSHIFTLEFT
   || op_id == OPERATOR.BITSHIFTRIGHT) {
  getToken();
  ans = eval_operator(op_id, ans, parse_level3());
  op_id = get_operator_id(token);
 }

 return ans;
}

/**
 * conditional operators
 */
double parse_level3() throws ParseError {
 OPERATOR op_id;
 double ans;
 ans = parse_level4();

 op_id = get_operator_id(token);
 while (op_id == OPERATOR.EQUAL || op_id == OPERATOR.UNEQUAL
   || op_id == OPERATOR.SMALLER || op_id == OPERATOR.LARGER
   || op_id == OPERATOR.SMALLEREQ || op_id == OPERATOR.LARGEREQ) {
  getToken();
  ans = eval_operator(op_id, ans, parse_level4());
  op_id = get_operator_id(token);
 }

 return ans;
}

/**
 * add or subtract
 */
double parse_level4() throws ParseError {
 OPERATOR op_id;
 double ans;
 ans = parse_level5();

 op_id = get_operator_id(token);
 while (op_id == OPERATOR.PLUS || op_id == OPERATOR.MINUS) {
  getToken();
  ans = eval_operator(op_id, ans, parse_level5());
  op_id = get_operator_id(token);
 }

 return ans;
}

/**
 * multiply, divide, modulus, xor
 */
double parse_level5() throws ParseError {
 OPERATOR op_id;
 double ans;
 ans = parse_level6();

 op_id = get_operator_id(token);
 while (op_id == OPERATOR.MULTIPLY || op_id == OPERATOR.DIVIDE
   || op_id == OPERATOR.MODULUS || op_id == OPERATOR.XOR) {
  getToken();
  ans = eval_operator(op_id, ans, parse_level6());
  op_id = get_operator_id(token);
 }

 return ans;
}

/**
 * power
 */
double parse_level6() throws ParseError {
 OPERATOR op_id;
 double ans;
 ans = parse_level7();

 op_id = get_operator_id(token);
 while (op_id == OPERATOR.POW) {
  getToken();
  ans = eval_operator(op_id, ans, parse_level7());
  op_id = get_operator_id(token);
 }

 return ans;
}

/**
 * Factorial
 */
double parse_level7() throws ParseError {
 OPERATOR op_id;
 double ans;
 ans = parse_level8();

 op_id = get_operator_id(token);
 while (op_id == OPERATOR.FACTORIAL) {
  getToken();
  // factorial does not need a value right from the
  // operator, so zero is filled in.
  ans = eval_operator(op_id, ans, 0.0);
  op_id = get_operator_id(token);
 }

 return ans;
}

/**
 * Unary minus
 */
double parse_level8() throws ParseError {
 double ans;

 OPERATOR op_id = get_operator_id(token);
 if (op_id == OPERATOR.MINUS) {
  getToken();
  ans = parse_level9();
  ans = -ans;
 } else {
  ans = parse_level9();
 }

 return ans;
}

/**
 * functions
 */
double parse_level9() throws ParseError {
 String fn_name;
 double ans;

 if (token_type == TOKENTYPE.FUNCTION) {
  fn_name = token;
  getToken();
  ans = eval_function(fn_name, parse_level10());
 } else {
  ans = parse_level10();
 }

 return ans;
}

/**
 * parenthesized expression or value
 */
double parse_level10() throws ParseError {
 // check if it is a parenthesized expression
 if (token_type == TOKENTYPE.DELIMETER) {
  if (token.equals("(")) {
   getToken();
   double ans = parse_level2();
   if (token_type != TOKENTYPE.DELIMETER || !token.equals(")")) {
    throw new ParseError(row(), col(), 3);
   }
   getToken();
   return ans;
  }
 }

 // if not parenthesized then the expression is a value
 return parse_number();
}

double parse_number() throws ParseError {
 double ans = 0.0;

 switch (token_type) {
 case NUMBER:
  // this is a number
  ans = Double.parseDouble(token);
  getToken();
  break;

 case VARIABLE:
  // this is a variable
  ans = eval_variable(token);
  getToken();
  break;

 default:
  // syntax error or unexpected end of expression
  if (token.length() == 0) {
   throw new ParseError(row(), col(), 6);
  } else {
   throw new ParseError(row(), col(), 7);
  }
 }

 return ans;
}

/**
 * returns the id of the given operator treturns -1 if the operator is not
 * recognized
 */
OPERATOR get_operator_id(final String op_name) {
 if (op_name.equals("&")) {
  return OPERATOR.AND;
 }
 if (op_name.equals("|")) {
  return OPERATOR.OR;
 }
 if (op_name.equals("<<")) {
  return OPERATOR.BITSHIFTLEFT;
 }
 if (op_name.equals(">>")) {
  return OPERATOR.BITSHIFTRIGHT;
 }

 if (op_name.equals("=")) {
  return OPERATOR.EQUAL;
 }
 if (op_name.equals("<>")) {
  return OPERATOR.UNEQUAL;
 }
 if (op_name.equals("<")) {
  return OPERATOR.SMALLER;
 }
 if (op_name.equals(">")) {
  return OPERATOR.LARGER;
 }
 if (op_name.equals("<=")) {
  return OPERATOR.SMALLEREQ;
 }
 if (op_name.equals(">=")) {
  return OPERATOR.LARGEREQ;
 }

 if (op_name.equals("+")) {
  return OPERATOR.PLUS;
 }
 if (op_name.equals("-")) {
  return OPERATOR.MINUS;
 }

 if (op_name.equals("*")) {
  return OPERATOR.MULTIPLY;
 }
 if (op_name.equals("/")) {
  return OPERATOR.DIVIDE;
 }
 if (op_name.equals("%")) {
  return OPERATOR.MODULUS;
 }
 if (op_name.equals("||")) {
  return OPERATOR.XOR;
 }

 if (op_name.equals("^")) {
  return OPERATOR.POW;
 }

 if (op_name.equals("!")) {
  return OPERATOR.FACTORIAL;
 }

 return OPERATOR.UNKNOWN;
}

/**
 * evaluate an operator for given valuess
 */
double eval_operator(final OPERATOR op_id, final double lhs,
  final double rhs) throws ParseError {
 switch (op_id) {
 case AND:
  return (int) lhs & (int) rhs;
 case OR:
  return (int) lhs | (int) rhs;
 case BITSHIFTLEFT:
  return (int) lhs << (int) rhs;
 case BITSHIFTRIGHT:
  return (int) lhs >> (int) rhs;

 case EQUAL:
  return (lhs == rhs) ? 1.0 : 0.0;
 case UNEQUAL:
  return (lhs != rhs) ? 1.0 : 0.0;
 case SMALLER:
  return (lhs < rhs) ? 1.0 : 0.0;
 case LARGER:
  return (lhs > rhs) ? 1.0 : 0.0;
 case SMALLEREQ:
  return (lhs <= rhs) ? 1.0 : 0.0;
 case LARGEREQ:
  return (lhs >= rhs) ? 1.0 : 0.0;

 case PLUS:
  return lhs + rhs;
 case MINUS:
  return lhs - rhs;

 case MULTIPLY:
  return lhs * rhs;
 case DIVIDE:
  return lhs / rhs;
 case MODULUS:
  return modulus(lhs, rhs);
 case XOR:
  return (int) lhs ^ (int) rhs;

 case POW:
  return Math.pow(lhs, rhs);

 case FACTORIAL:
  return factorial(lhs);
 }

 throw new ParseError(row(), col(), 104);
}

/**
 * evaluate a function
 */
double eval_function(final String fn_name, final double value) throws ParseError {
 // first make the function name upper case
 String fnUpper = fn_name.toUpperCase();

 // arithmetic
 if (fnUpper.equals("ABS")) {
  return Math.abs(value);
 }
 if (fnUpper.equals("EXP")) {
  return Math.exp(value);
 }
 if (fnUpper.equals("SIGN")) {
  return sign(value);
 }
 if (fnUpper.equals("SQRT")) {
  return Math.sqrt(value);
 }
 if (fnUpper.equals("LOG")) {
  return Math.log(value);
 }
 if (fnUpper.equals("LOG10")) {
  return Math.log10(value);
 }

 // trigonometric
 if (fnUpper.equals("SIN")) {
  return Math.sin(value);
 }
 if (fnUpper.equals("COS")) {
  return Math.cos(value);
 }
 if (fnUpper.equals("TAN")) {
  return Math.tan(value);
 }
 if (fnUpper.equals("ASIN")) {
  return Math.asin(value);
 }
 if (fnUpper.equals("ACOS")) {
  return Math.acos(value);
 }
 if (fnUpper.equals("ATAN")) {
  return Math.atan(value);
 }

 // probability
 if (fnUpper.equals("FACTORIAL")) {
  return factorial(value);
 }

 // unknown function
 throw new ParseError(row(), col(), 102, fn_name);
}

/**
 * evaluate a variable
 */
double eval_variable(final String var_name) throws ParseError {
 // first make the variable name uppercase
 String varUpper = var_name.toUpperCase();

 // check for built-in variables
 if (varUpper.equals("E")) {
  return Math.E;
 }
 if (varUpper.equals("PI")) {
  return Math.PI;
 }

 // check for user defined variables
 if (user_var.containsKey(varUpper)) {
  double ans = user_var.get(varUpper).doubleValue();
  return ans;
 }

 // unknown variable
 throw new ParseError(row(), col(), 103, var_name);
}

/**
 * calculate factorial of value for example 5! = 5*4*3*2*1 = 120
 */
static double factorial(double value) throws ParseError {
 double res;
 int v = (int) value;

 if (value != v) {
  throw new ParseError(400, "factorial");
 }

 res = v;
 v--;
 while (v > 1) {
  res *= v;
  v--;
 }

 if (res == 0)
  res = 1; // 0! is per definition 1
 return res;
}

/**
 * calculate the modulus of the given values
 */
static double modulus(double a, double b) throws ParseError {
 // values must be integer
 int a_int = (int) a;
 int b_int = (int) b;
 if (a_int == a && b_int == b) {
  return a_int % b_int;
 } else {
  throw new ParseError(400, "%");
 }
}

/**
 * calculate the sign of the given value
 */
static double sign(double value) {
 if (value > 0)
  return 1;
 if (value < 0)
  return -1;
 
 return 0;
}

}


/**
 * Class ParseError
 */

class ParseError extends Exception {

 /**
  * Create an error with given message id and fill in given string in message
  * @PARAM id    id of the message
  * @PARAM str   a string which will be filled in in the message
  */
 ParseError(final int id, final String str)
 { 
  row_ = -1;
  col_ = -1;
  id_ = id;

  msg_ = String.format(errorMsg(id_), str);
 }

 /**
  * Create an error with given message id and fill in given string in message
  * @PARAM id    id of the message
  */
 ParseError(final int id)
 { 
  row_ = -1;
  col_ = -1;
  id_ = id;

  msg_ = errorMsg(id_);
 }

 /**
  * Create an error with given message id and fill in given string in message
  * @PARAM row   row where the error occured
  * @PARAM col   column where the error occured 
  * @PARAM id    id of the message
  * @PARAM str   a string which will be filled in in the message
  */
 ParseError(final int row, final int col, final int id, final String str)
 { 
  row_ = row;
  col_ = col;
  id_ = id;

  msg_ = String.format(errorMsg(id_), str);
 }

 /**
  * Create an error with given message id and fill in given string in message
  * @PARAM row   row where the error occured 
  * @PARAM col   column where the error occured 
  * @PARAM id    id of the message
  */
 ParseError(final int row, final int col, final int id)
 { 
  row_ = row;
  col_ = col;
  id_ = id;

  msg_ = errorMsg(id_);
 }

 /**
  * Returns the error message, including line and column number
  */
 final String get()
 {
  String res;
  if (row_ == -1)
  {
   if (col_ == -1)
   {
    res = String.format("Error: %s", msg_);
   }
   else
   {
    res = String.format("Error: %s (col %d)", msg_, col_);
   }
  }
  else
  {
   res = String.format("Error: %s (ln %d, col %d)", msg_, row_, col_);
  }
  return res;
 }

 int get_id()
 {
  return id_;
 }

 /// Private functions

 /**
  * Returns a pointer to the message description for the given message id.
  * Returns "Unknown error" if id was not recognized.
  */
 private String errorMsg(final int id)
 {
  switch (id)
  {
  // syntax errors
  case 1: return "Syntax error in part \"%s\"";
  case 2: return "Syntax error";
  case 3: return "Parentesis ) missing";
  case 4: return "Empty expression";
  case 5: return "Unexpected part \"%s\"";
  case 6: return "Unexpected end of expression";
  case 7: return "Value expected";

  // wrong or unknown operators, functions, variables
  case 101: return "Unknown operator %s";
  case 102: return "Unknown function %s";
  case 103: return "Unknown variable %s";
  case 104: return "Unknown operator";

  // domain errors
  case 200: return "Too long expression, maximum number of characters exceeded";

  // error in assignments of variables
  case 300: return "Defining variable failed";

  // error in functions
  case 400: return "Integer value expected in function %s";

  // unknown error
  case 500: return "%s";
  }

  return "Unknown error";
 }  

 /// Data  
 private int row_;    /// row where the error occured
 private int col_;    /// column (position) where the error occured
 private int id_;     /// id of the error
 private String msg_;
}

