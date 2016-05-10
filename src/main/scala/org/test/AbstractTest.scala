package org.test



abstract class AbstractTest {
  def test: String
}

class Element(val test: String, val extend: String) extends AbstractTest {
  def extend2: String = extend
  
  override def toString = "Element" + test +  "  " + extend 
}