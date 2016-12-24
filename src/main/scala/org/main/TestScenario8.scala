//package org.main
//
//import org.admin._
//import org.client._
//import org.configMgr._
//
//class TestScenario8 {
//
////  Admin.setConnectPathForConfigClient("C0000001", "http://configuration/config_3")
////  
////  val client: org.client.ConfigClient = Client.setClient("http://configuration/config_3")
//
//  def scenario8 = {
//    
//    println("############################## SCENARIO 8 #######################")
//    
////    val step = ConfigMgr.startConfig(client)
//    
////    require(step.dependencies(0).ifComponents(0) == "S000001C000001", step.dependencies(0).ifComponents(0))
////    require(step.dependencies(0).ifOperator == "single", step.dependencies(0).ifOperator)
////    require(step.dependencies(0).thenComponents(0) == "S000002C000002", step.dependencies(0).thenComponents(0))
////    require(step.dependencies(0).thenOperator == "single", step.dependencies(0).thenOperator)
////    require(step.dependencies(0).ruletype == "exclude", step.dependencies(0).ruletype)
//    
////    val adminUser: AdminUser = Admin.register("test3", "test3")
////    println(adminUser.id)
////    println(adminUser.status)
//    
//    require(Admin.authenticate("test3", "test3") == "AU#38:1") 
//  }
//}