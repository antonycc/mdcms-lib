package uk.co.diyaccounting.util.xml;

import java.util.Enumeration;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An Enumeration that walks through a w3c NodeList
 * 
 * @author Antony
 */
public final class NodeEnumeration {

   /**
    * The logger for this class.
    */
   // private static final Logger logger =
   // LoggerFactory.getLogger(NodeEnumeration.class);

   /**
    * The Enumeration is backed by a vector created enumeration of Nodes which is copied on creation
    */
   private Enumeration<Node> e;

   /**
    * Private constructor - a NodeList is required to create.
    */
   private NodeEnumeration() {
   }

   /**
    * Creates a new enumeration from a NodeList.
    * 
    * @returns true if and only if this enumeration object contains at least one more element to provide; false
    *          otherwise.
    */
   public NodeEnumeration(final NodeList nodes) {
      this();
      int l = nodes.getLength();
      Vector<Node> v = new Vector<Node>(l);
      for (int i = 0; i < l; i++) {
         v.add(nodes.item(i));
      }
      this.e = v.elements();
   }

   /**
    * Tests if this enumeration contains more elements.
    * 
    * @returns true if and only if this enumeration object contains at least one more element to provide; false
    *          otherwise.
    */
   public boolean hasMoreElements() {
      return this.e.hasMoreElements();
   }

   /**
    * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
    * 
    * @returns the next element of this enumeration.
    */
   public Node nextElement() {
      return this.e.nextElement();
   }
}
