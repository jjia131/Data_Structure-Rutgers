package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		Node pol1 = new Node(poly1.term.coeff, poly1.term.degree, poly1.next);
        Node pol2 = new Node(poly2.term.coeff, poly2.term.degree, poly2.next);
        Node pol3 = null;
		Node head = null;
		int count = 0;
		//if (pol1.term.degree < pol2.term.degree)
		while ((pol1.next != null) && (pol2.next != null)) {
			if (pol1.term.degree > pol2.term.degree) {
				pol3 = new Node(pol2.term.coeff, pol2.term.degree, pol3);
				pol2 = pol2.next;
			} else if (pol1.term.degree < pol2.term.degree) {
				pol3 = new Node(pol1.term.coeff, pol1.term.degree, pol3);
				pol1 = pol1.next;
			} else {
				pol3 = new Node(pol1.term.coeff + pol2.term.coeff, pol1.term.degree, pol3);
				pol1 = pol1.next;
				pol2 = pol2.next;
			}
			if (count == 0) {
				head = pol3;
			}
			//pol3 = pol3.next;
			count++;
		}

		while (pol2 != null) {
            pol3 = new Node(pol2.term.coeff, pol2.term.degree, pol3);
            pol2 = pol2.next;
        }
		while (pol1 != null) {
            pol3 = new Node(pol1.term.coeff, pol1.term.degree,pol3);
            pol1 = pol1.next;		
		}
		
		Node newAddition = null;
		Node next = null;
		while (pol3 != null) {
			next = pol3.next;
			pol3.next = newAddition;
			newAddition = pol3;
			pol3 = next;
		}
		
		return newAddition;
	}

		/*Node addition = null;
		Node p1 = poly1;
		Node p2 = poly2;
		if (poly1 == null) {
			return p2;
		}
		if (poly2 == null) {
			return p1;
		}
		if(poly1.term.degree > poly2.term.degree) {
			p1 = poly2;
			p2 = poly1;
		}
		
		while(p1!=null&&p2!=null) {
			if(p1.term.degree == p2.term.degree) {
				//System.out.println("Before Poly1: " + toString(p1) + " Poly2: " + toString(p2));
				if((p1.term.coeff+p2.term.coeff)!=0) {
					addition = new Node(p1.term.coeff+p2.term.coeff,p1.term.degree,addition);
				}
				//if(p1.next!=null)
				p1 = p1.next;
				//if(p2.next!=null)
				p2 = p2.next;
				//System.out.println("After Poly1: " + toString(p1) + " Poly2: " + toString(p2));
				}
			else if(p1.term.degree<p2.term.degree) {
				//System.out.println("P1<P2: Poly1: " + toString(p1) +" Poly2: " + toString(p2));
				addition = new Node(p1.term.coeff,p1.term.degree,addition);
				//if(p1.next!=null)
				p1 = p1.next;
				}
			else if(p1.term.degree > p2.term.degree) {
				//System.out.println("P1>P2: Poly1: " + toString(p1) +" Poly2: " + toString(p2));
				addition = new Node(p2.term.coeff,p2.term.degree,addition);
				//if(p2.next!=null)
				p2 = p2.next;
				} 		
			} 
		while(p1 !=null || p2 !=null) {
			if(p1 !=null) {
				addition = new Node(p1.term.coeff,p1.term.degree,addition);
				p1 = p1.next;
			}
			if(p2!=null) {
				addition = new Node(p2.term.coeff,p2.term.degree,addition);
				p2 = p2.next;
			}
		}
		Node newAddition = null;
		Node next = null;
		while (addition != null) {
			next = addition.next;
			addition.next = newAddition;
			newAddition = addition;
			addition = next;
		}
		return newAddition;
		}*/
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		Node production = null;
		Node term1 = null;
		Node p1 = poly1;
		Node p2 = poly2;
		while(p1 !=null) {
			//System.out.println("P1: " + p1.term.coeff);
			p2 = poly2;
			term1 = null;
			while(p2 != null) {	
				if(p1.term.coeff!=0&&p2.term.coeff!=0) {
				term1 = new Node(p1.term.coeff*p2.term.coeff,p1.term.degree+p2.term.degree,term1);
				}
				//System.out.println("P1: "+ p1.term.coeff+ " P2: " + p2.term.coeff);	
				p2 = p2.next;
			}
			Node decTerm1 = null;
			Node next = null;
			while (term1 != null) {
				next = term1.next;
				term1.next = decTerm1;
				decTerm1 = term1;
				term1 = next;
			}
			/*Node decProd = null;
			Node next1 = null;
			while (production != null) {
				next1 = production.next;
				production.next = decProd;
				decProd = production;
				production = next1;
			}*/
			//System.out.println("After inverse Production: " + toString(decProd));
			//System.out.println("Term1: " + toString(decTerm1));
			production = add(decTerm1,production);
			//System.out.println("Production: " + toString(add(decTerm1,production)));		
			p1 = p1.next;
		}
		/*Node newProd = null;
		Node next = null;
		while (production != null) {
			next = production.next;
			production.next = newProd;
			newProd = production;
			production = next;
		}*/
		return production;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		float result =0;
		Node p = poly;
		double d = Double.parseDouble(new Float(x).toString());
		while(p!=null){
			result = (float) (Math.pow(d, p.term.degree)*p.term.coeff) + result;
			p = p.next;
		}
		return result;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
