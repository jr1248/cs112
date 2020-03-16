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
		int degree=0;
		float coeff=0;
		Node poly= new Node (coeff,degree,null);
		Node head = poly;
		Node empty=null;
		if(poly1==null && poly2==null) {
			return empty;
		}
		while(poly1!=null && poly2!=null) {
			if(poly1.term.coeff+poly2.term.coeff==0) {
				return empty;
			}
			else if(poly1.term.degree==poly2.term.degree) {
					poly.term.coeff=poly1.term.coeff+poly2.term.coeff;
					poly.term.degree=poly1.term.degree;
					poly1=poly1.next;
					poly2=poly2.next;
			}
			else if(poly1.term.degree<poly2.term.degree) {
				poly.term.coeff=poly1.term.coeff;
				poly.term.degree=poly1.term.degree;
				poly1=poly1.next;
			}
			else if(poly1.term.degree>poly2.term.degree) {
				poly.term.coeff=poly2.term.coeff;
				poly.term.degree=poly2.term.degree;
				poly2=poly2.next;
			}
			
			if( poly1!=null || poly2==null ) {
				
				poly.next = new Node(coeff,degree,null);
			}
			else {
				poly.next = null;
			}
			poly = poly.next;
		}
		while(poly1!=null) {
			poly.term.coeff=poly1.term.coeff;
			poly.term.degree=poly1.term.degree;
			poly1=poly1.next;
			if( poly1!=null ) {
				poly.next = new Node(coeff,degree,null);
			}
			else {
				poly.next = null;
			}
			poly = poly.next;
		}
		while(poly2!=null) {
			poly.term.coeff=poly2.term.coeff;
			poly.term.degree=poly2.term.degree;
			poly2=poly2.next;
			if( poly2!=null ){
				poly.next = new Node(coeff,degree,null);
			}
			else {
				poly.next = null;
			}
			poly = poly.next;			
		}
		
		return head;
		
	}
	
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

	    if (poly1 == null || poly2 == null)
	        return null;

	    Node py1 = poly1;
	    Node py2 = poly2;
	    Node head = null;
	    float coeff;
	    int degree;
	    int maxExpValue = 0;
	    while (py1 != null) {
	        while (py2 != null) {
	            coeff = py1.term.coeff * py2.term.coeff;
	            degree = py1.term.degree + py2.term.degree;
	            head = new Node(coeff, degree, head);
	            if (degree > maxExpValue)
	            	maxExpValue = degree;
	            py2 = py2.next;
	        }
	        py1 = py1.next;
	        py2 = poly2;
		}
	    Node poly = null;
	    for (int i = 0; i<= maxExpValue; i++) {
	        Node temp = head;
	        float finalSum = 0;
	        while (temp != null) {
	            if (temp.term.degree == i)
	            	finalSum+=temp.term.coeff;
	            temp = temp.next;
	        }
	        if (finalSum != 0)
	            poly = new Node(finalSum, i, poly);
	    }
	    Node rev = poly;
	    Node polyf = null;
	    while (rev != null)
	    {
	        if (rev.term.coeff == 0)
	        {
	            rev = rev.next;
	            continue;
	        }
	        else
	        {
	            polyf = new Node(rev.term.coeff, rev.term.degree, polyf);
	            rev = rev.next;
	        }
	    }

	    if (polyf == null)
	        return null;
	    else
	    {
	   return polyf;
	    }  
	}

			
		

		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		double num=0;
		float coeff=0;
		float degree=0;
		while(poly!=null) {
			coeff=poly.term.coeff;
			degree=(float)poly.term.degree;
			num=num+(coeff*((float)Math.pow( x,degree)));
			poly=poly.next;
		}
		return (float) num;
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
