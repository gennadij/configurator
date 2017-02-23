//package org.dependency
//
//import org.client.ConfigClient
//import org.configTree.component.Component
//import org.configTree.step.Step
//import org.configTree.step.AnnounceStep
//import org.configTree.step.SuccessStep
//
//trait Dependency {
//  
//  def checkDependency(client: ConfigClient, step: Step): AnnounceStep = {
//    
//    val currentConfig = client.currentConfig
//    
//    val tempDependency: List[org.configTree.step.Dependency] = step.dependencies
//
//    
//    new SuccessStep("3", "dependency")
//  }
//}