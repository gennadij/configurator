package org.main

import org.admin._
import org.client._
import org.configMgr.ConfigMgr
import org.configTree.component._


class TestScenario7 {
  def scenario7 = {

    Admin.setConnectPathForConfigClient("C0000001", "http://configuration/config_3")
    
    val client: org.client.ConfigClient = Client.setClient("http://configuration/config_3")
    
    println("############################## SCENARIO 7 #######################")
    
    
    val step = ConfigMgr.startConfig(client)
    
    val step1 = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000001C000001", -1)))
    
    
    
    
  }
}