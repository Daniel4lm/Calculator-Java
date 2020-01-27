package calculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class CalculatorExpression {		

	/**** MAPE ****/
	private Map<Integer, String> operators ;
	private Map<Integer, Double> operands ;
	private Map<Integer, String> resultOperators ;
	private Map<Integer, Double> resultOperands ;
	
	private String expression;
	private String covertedExpression;
	
	/* Constructor for Calculator */
	public CalculatorExpression(String expression) {
		this.expression = expression;
		operators = new TreeMap<>();
		operands = new TreeMap<>();
		resultOperators = new TreeMap<>();
		resultOperands = new TreeMap<>();
		covertedExpression = pushBlackFields(expression);
	}

	/**** OPERACIJE VISEG REDA x^y & sqrt ****/
	
	private void sqrtpowOperation(String operat, Map<Integer, String> operator, Map<Integer, Double> operands) {

		ArrayList<Integer> operatorList = new ArrayList<>(operator.keySet());
		int i;
		for (i = 0; i < operatorList.size(); i++) {

			if (i > 0 && operator.get(operatorList.get(i - 1)).equals(operat)) {
				String operation = operator.get(operatorList.get(i - 1));
				operandsModification(operatorList.get(i - 1), operation, operands);
				operatorsModification(operatorList, operatorList.get(i - 1), operation, operator);
				i = i - 1;

			} else if (operator.get(operatorList.get(i)).equals(operat)) {
				String operation = operator.get(operatorList.get(i));
				operandsModification(operatorList.get(i), operation, operands);
				operatorsModification(operatorList, operatorList.get(i), operation, operator);
			}

			if (i > 0 && (i == operator.size() - 1) && operator.get(operatorList.get(i)).equals(operat)) {
				i = i - 1;
			}
		}
	}	

	/**** OPERACIJE VISEG REDA * & / ****/
	private void highOperations(Map<Integer, String> operator, Map<Integer, Double> operands) {

		ArrayList<Integer> operatorList = new ArrayList<>(operator.keySet());
		int i;
		for (i = 0; i < operatorList.size(); i++) {

			if (i > 0 && (operator.get(operatorList.get(i - 1)).equals("*")
					|| operator.get(operatorList.get(i - 1)).equals("/"))) {

				String operation = operator.get(operatorList.get(i - 1));
				operandsModification(operatorList.get(i - 1), operation, operands);
				operatorsModification(operatorList, operatorList.get(i - 1), operation, operator);
				i = i - 1;

			} else if (operator.get(operatorList.get(i)).equals("*") || operator.get(operatorList.get(i)).equals("/")) {

				String operation = operator.get(operatorList.get(i));
				operandsModification(operatorList.get(i), operation, operands);
				operatorsModification(operatorList, operatorList.get(i), operation, operator);

			}

			if (i > 0 && i == operator.size() - 1 && (operator.get(operatorList.get(i)).equals("*")
					|| operator.get(operatorList.get(i)).equals("/"))) {
				i = i - 1;
			}

		}
	}

	/**** OPERACIJE NIZEG REDA '+' '-' ****/

	private void lowerOperations(Map<Integer, String> operator, Map<Integer, Double> operands) {

		ArrayList<Integer> operatorList = new ArrayList<>(operator.keySet());
		int i;
		for (i = 0; i < operatorList.size(); i++) {

			if (i > 0 && i < operator.size()) {
				i = i - 1;
			}

			if (operator.get(operatorList.get(i)).equals("+") || operator.get(operatorList.get(i)).equals("-")) {
				// System.out.println(i + " " + operatorList.get(i) + " reg operator duzina " +
				// operatorList.size());
				String operation = operator.get(operatorList.get(i));
				operandsModification(operatorList.get(i), operation, operands);
				operatorsModification(operatorList, operatorList.get(i), operation, operator);
				// System.out.println(i + " " + operatorList.get(i) + " duzina " +
				// operatorList.size());
			}

			if (operator.size() == 1) {
				String operation = operator.get(operatorList.get(i));
				operandsModification(operatorList.get(i), operation, operands);
				operatorsModification(operatorList, operatorList.get(i), operation, operator);
			}
		}
	}

	/**** MODIFIKACIJA MAPE SA OPERATORIMA ****/

	private void operatorsModification(List<Integer> list, Integer ind, String operation,
			Map<Integer, String> operator) {

		operator.remove(ind);

		Map<Integer, String> temp = new LinkedHashMap<>(operator);

		Iterator<Integer> operandIterator = (temp.keySet()).iterator();
		Integer iter;

		list.clear();

		while (operandIterator.hasNext()) {

			iter = operandIterator.next();
			if (iter < ind) {
				list.add(iter);
			}

			if (operation.equals("sqrt") && iter > ind && operator.containsKey(iter)) {
				operator.put(iter - 1, operator.get(iter));
				operator.remove(iter);
				list.add(iter - 1);
			} else if (iter > ind && operator.containsKey(iter)) {
				operator.put(iter - 2, operator.get(iter));
				operator.remove(iter);
				list.add(iter - 2);
			}
		}
		temp.clear();
	}

	/**** MODIFIKACIJA MAPE SA OPERATORIMA ****/

	private void operandsModification(Integer ind, String operation, Map<Integer, Double> operands) {

		switch (operation) {

		case "+":

			if (operands.containsKey(ind - 1) && operands.containsKey(ind + 1)) {
				operands.put(ind - 1, operands.get(ind - 1) + operands.get(ind + 1));
			}

			break;

		case "-":

			if (operands.containsKey(ind - 1) && operands.containsKey(ind + 1)) {
				operands.put(ind - 1, operands.get(ind - 1) - operands.get(ind + 1));
			}

			break;

		case "*":

			if (operands.containsKey(ind - 1) && operands.containsKey(ind + 1)) {
				operands.put(ind - 1, operands.get(ind - 1) * operands.get(ind + 1));
			}

			break;

		case "/":

			if (operands.containsKey(ind - 1) && operands.containsKey(ind + 1)) {
				operands.put(ind - 1, operands.get(ind - 1) / operands.get(ind + 1));
			}

			break;

		case "^":

			if (operands.containsKey(ind - 1) && operands.containsKey(ind + 1)) {
				operands.put(ind - 1, Math.pow(operands.get(ind - 1), operands.get(ind + 1)));
			}

			break;

		case "sqrt":

			if (operands.containsKey(ind + 1)) {
				operands.put(ind, Math.sqrt(operands.get(ind + 1)));
			}

			break;
		}

		operands.remove(ind + 1);

		Map<Integer, Double> temp = new LinkedHashMap<>(operands);
		Iterator<Integer> operandIterator = temp.keySet().iterator();
		Integer iter;
		// System.out.println(operands);
		while (operandIterator.hasNext()) {

			if (operation.equals("sqrt")) {
				if ((iter = operandIterator.next()) > ind && operands.containsKey(iter)) {
					operands.put(iter - 1, operands.get(iter));
					operands.remove(iter);
				}
			} else {
				if ((iter = operandIterator.next()) > (ind - 1) && operands.containsKey(iter)) {
					operands.put(iter - 2, operands.get(iter));
					operands.remove(iter);
				}
			}
		}
		temp.clear();
	}

	/**** PODESAVANJE JEDNACINE ****/
	private String pushBlackFields(String str) {

		String returnStr = new String();

		for (Character ch : str.toCharArray()) {
			if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^') {
				returnStr = returnStr + " " + ch + " ";
			} else if (ch == 's') {
				returnStr = returnStr + " " + ch;
			} else if (ch == 'q' || ch == 'r') {
				returnStr = returnStr + ch;
			} else if (ch == 't') {
				returnStr = returnStr + ch + " ";
			} else if (ch == '(') {
				returnStr = returnStr + ch + " ";
			} else if (ch == ')') {
				returnStr = returnStr + " " + ch;
			} else {
				returnStr += ch;
			}
		}
		return returnStr.trim();
	}

	public void Calculation() {		
		
		String[] strArray = covertedExpression.split("\\s{1,}");

		try {

			/**** < UCITAVANJE U MAPE > ****/
			int i, m = 0;
			for (i = 0; i < strArray.length; i++) {

				if (strArray[i].equals("(")) {

					int k = i + 1, l = 0;
					while (!strArray[k].equals(")")) {

						if (strArray[k].equals("+") || strArray[k].equals("-") || strArray[k].equals("*")
								|| strArray[k].equals("/") || strArray[k].equals("^") || strArray[k].equals("sqrt")) {
							operators.put(l, strArray[k]);
						} else {
							operands.put(l, Double.parseDouble(strArray[k]));
						}
						k++;
						l++;
						i = k;
					}

					// System.out.println("Operators : " + operators);
					// System.out.println("Operands : " + operands + "\n");

					sqrtpowOperation("sqrt", operators, operands);
					// System.out.println("Operators : " + operators);
					// System.out.println("Operands : " + operands + "\n");

					sqrtpowOperation("^", operators, operands);
					// System.out.println("Operators : " + operators);
					// System.out.println("Operands : " + operands + "\n");

					highOperations(operators, operands);
					// System.out.println("Operators : " + operators);
					// System.out.println("Operands : " + operands + "\n");
					lowerOperations(operators, operands);

					// System.out.println("Operators : " + operators);
					// System.out.println("Operands : " + operands + "\n");

					resultOperands.put(m, operands.get(0));
					operands.clear();
					m++;

				} else if (strArray[i].equals("+") || strArray[i].equals("-") || strArray[i].equals("*")
						|| strArray[i].equals("/") || strArray[i].equals("^") || strArray[i].equals("sqrt")) {
					resultOperators.put(m, strArray[i]);
					m++;
				} else {
					resultOperands.put(m, Double.parseDouble(strArray[i]));
					m++;
				}
			}

			// System.out.println("Operators : " + resultOperators);
			// System.out.println("Operands : " + resultOperands + "\n");

			sqrtpowOperation("sqrt", resultOperators, resultOperands);
			// System.out.println("Operators : " + resultOperators);
			// System.out.println("Operands : " + resultOperands + "\n");

			sqrtpowOperation("^", resultOperators, resultOperands);
			// System.out.println("Operators : " + resultOperators);
			// System.out.println("Operands : " + resultOperands + "\n");

			highOperations(resultOperators, resultOperands);
			// System.out.println("Operators : " + resultOperators);
			// System.out.println("Operands : " + resultOperands + "\n");
			lowerOperations(resultOperators, resultOperands);

			// System.out.println("Operators : " + resultOperators);
			// System.out.println("Operands : " + resultOperands + "\n");

			System.out.println(expression + " = " + resultOperands.get(0));

		} catch (Exception evaluateException) {
			System.out.println("Wrong expression! ");
		}
		
	}
	
	public static void main(String[] args) {
		
		//Scanner scanner = new Scanner(System.in);
		// System.out.println("Unesite jednacinu: ");
		
		new CalculatorExpression("(444^2)+(1+3*3^2)^2*sqrt(4^4.2*3-2)/(18-2)+8^2.2").Calculation();
		
	}

}
