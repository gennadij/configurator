//package org
//
//import org.specs2.Specification
//import org.configMgr._
//import org.configTree.step._
//import org.configTree.component._
//import scala.collection.mutable.ListBuffer
//import org.configSettings.ConfigSettings
//
///**
//  * Created by gennadi on 12.05.16.
//  */
//class RunConfig extends Specification{
//  def is = 
//  
//  s2"""
//      Specification for whole Config by step 001, 003, 004, 006
//        StartStepId 001                                                      $e1
//        step 001 type FirstStep                                              $e5
//        SelectedComponentId 001003 NextStepId 003                            $e2
//        step 003 type DefaultStep                                            $e6
//        SelectedComponentId 003002, 003003 NextStepId 004                    $e3
//        step 004 type DefaultStep                                            $e7
//        SelectedComponentId 004003 NextStepId 006                            $e4
//        step 006 type LastStep                                               $e8
//        SelectedComponentId 006001 NextStepId 000                            $e9
//        step 0 type FinalStep                                               $e10
//    """
//        
//  def e1 = ConfigMgr.startConfig.id must_== "001"
//  
//  def e5 = ConfigMgr.startConfig.isInstanceOf[FirstStep] must_== true 
//  
//  def e2 = ConfigMgr.getNextStep(Set(new SelectedComponent("001003"))).id must_== "003"
//  
//  def e6 = ConfigMgr.getNextStep(Set(new SelectedComponent("001003"))).isInstanceOf[DefaultStep]  must_== true
//  
//  def e3 = ConfigMgr.getNextStep(Set(new SelectedComponent("003002"), new SelectedComponent("003003"))).id must_== "004"
//  
//  def e7 = ConfigMgr.getNextStep(Set(new SelectedComponent("003002"), new SelectedComponent("003003"))).isInstanceOf[DefaultStep]  must_== true
//  
//  def e4 = ConfigMgr.getNextStep(Set(new SelectedComponent("004003"))).id must_== "006"
//  
//  def e8 = ConfigMgr.getNextStep(Set(new SelectedComponent("004003"))).isInstanceOf[LastStep]  must_== true
//  
//  def e9 = ConfigMgr.getNextStep(Set(new SelectedComponent("006001"))).id must_== "0"
//  
//  def e10 = ConfigMgr.getNextStep(Set(new SelectedComponent("006001"))).isInstanceOf[FinalStep]  must_== true
//}
