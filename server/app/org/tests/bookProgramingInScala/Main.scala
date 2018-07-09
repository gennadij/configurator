package org.tests.bookProgramingInScala

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 28.03.2018
 */
object MySimulation extends CircuitSimulation {
   def InverterDelay = 1
   def AndGateDelay = 3
   def OrGateDelay = 5
 }

object Main {
    def main(args: Array[String]) {
      
      import MySimulation._
      val input1, input2, sum, carry = new Wire
      
      probe("sum", sum)
      probe("carry", carry)
      halfAdder(input1, input2, sum, carry)
      input1 setSignal true
      
      run()
      
      input2 setSignal true
      
      run()
      
      println("Main")
    }
}

