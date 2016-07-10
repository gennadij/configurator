//package org
//
//import org.configMgr._
//import org.configTree.step._
//import org.configTree.component._
//import scala.collection.mutable.ListBuffer
//import org.specs2.Specification
//
//
//
//class RunConfigNotLinear extends Specification{ 
//  def is =
//  
//  s2"""
//      Specification for whole Config by step 001, 003, 004, 006
//        Step 001                        $e1
//        Step 003                        $e2
//        step 004                        $e3
//        step 006                        $e4
//        step 003                        $e5
//    """
//
//  def e1 = ConfigMgr.startConfig.id must_== "001"
//  
//  def e2 = ConfigMgr.getNextStep(Set(new SelectedComponent("001003"))).id must_== "003"
//  
//  def e3 = ConfigMgr.getNextStep(Set(new SelectedComponent("003002"), new SelectedComponent("003003"))).id must_== "004"
//  
//  def e4 = ConfigMgr.getNextStep(Set(new SelectedComponent("004003"))).id must_== "006"
//  
//  def e5 = ConfigMgr.getNextStep(Set(new SelectedComponent("003003"), new SelectedComponent("003001"))).id must_== "004"
//}