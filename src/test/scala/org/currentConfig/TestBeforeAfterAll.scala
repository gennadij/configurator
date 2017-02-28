package org.currentConfig

import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

class TestBeforeAfterAll extends Specification with BeforeAfterAll{
  
  def afterAll(): Unit = {
    println("after")
  }
  def beforeAll(): Unit = {
    println("before")
  }
  
  
  "test" >> {
    "case 1" >> {
      "" === ""
    }
    "case 2" >> {
      "" === ""
    }
  }
}