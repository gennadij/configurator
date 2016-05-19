package org

import org.specs2.Specification
import org.configMgr._
import org.configTree.step._
import org.configTree.component._
import scala.collection.mutable.ListBuffer
import org.configSettings.ConfigSettings

/**
  * Created by gennadi on 12.05.16.
  */
class RunConfig extends Specification{
  sequential 
  def is = 
  
  s2"""
      Specification for whole Config by step 001, 003, 004, 006
        Step 001                        $e1
        Step 003                        $e2
        step 004                        $e3
        step 006                        $e4
    """
        
  def e1 = ConfigMgr.startConfig.id must_== "001"
  
  val selectedComponent001003 = "001003"
  
  def e2 = ConfigMgr.getNextStep(selectedComponent001003).id must_== "003"
  
  val selectedComponent003002 = "003002"
  
  def e3 = ConfigMgr.getNextStep("003002").id must_== "004"
  
  val selectedComponent004003 = "004003"
  
  def e4 = ConfigMgr.getNextStep("004003").id must_== "006"
}
