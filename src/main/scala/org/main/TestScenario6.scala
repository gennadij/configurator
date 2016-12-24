//package org.main
//
//import org.admin.Admin
//import org.status.SuccessfulStatus
//import org.status.ErrorStatus
//
//class TestScenario6 {
//  def scenario6 = {
//    println("############################## SCENARIO 6 #######################")
//    
//    val status1 = Admin.connect("test1", "5A105E8B9D40E1329780D62EA2265D8A")
//    
//    require(status1.isInstanceOf[SuccessfulStatus], status1.message)
//    
//    val status2 = Admin.connect("test", "5A105E8B9D40E1329780D62EA2265D8A")
//    
//    require(status2.isInstanceOf[ErrorStatus], status2.message)
//    
//    val status3 = Admin.connect("test1", "A105E8B9D40E1329780D62EA2265D8A")
//    
//    require(status3.isInstanceOf[ErrorStatus], status3.message)
//  }
//}