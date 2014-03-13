package utilities;

import java.util.Map;
import java.util.TreeMap;

public class GraphComposer {

	MatchParser parser = new MatchParser();
	Map<Double, Double> lineCoords = new TreeMap<Double, Double>();
	String function;
	String fromX;
	String toX;
	Double coordY;
	
	public GraphComposer(String function, String fromX, String toX){
		this.function = function;
		this.fromX = fromX;
		this.toX = toX;
	}
	
	public void linePoints(){
		double from = Double.parseDouble(fromX);
		double to = Double.parseDouble(toX);
		for(double i = from; i <= to; i = i + 0.1){
			parser.setVariable("x", i);
			try {
				coordY = parser.parse(function);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lineCoords.put(i, coordY);
		}
	}
}

class CoordsCheck{
	public static void main(String[] args) {
		GraphComposer composer = new GraphComposer("x+23*(sin(x*3)-x*0.21)", "-4", "5");
		composer.linePoints();
		System.out.println(composer.lineCoords.values().toString());
	}
}
