package util

import play.api.libs.json.JsValue
import models.websocket.WebClient
import play.api.libs.json.Json
import models.json.JsonNames
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 09.03.2018
 */
object CommonFunction {
  
  def firstStep(wC: WebClient, configUrl: String): JsValue = {
    val startConfigIn = Json.obj(
          "json" -> JsonNames.START_CONFIG
          ,"params" -> Json.obj(
               "configUrl" -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn)
      
      Logger.info(this.getClass.getSimpleName + ": startConfigIn " + startConfigIn)
      Logger.info(this.getClass.getSimpleName + ": startConfigOut " + startConfigOut)
      
      startConfigOut
  }
  
  def selectComponent(wC: WebClient, componentId: String): JsValue = {
    val componentIn = Json.obj(
        "json" -> JsonNames.COMPONENT
        ,"params" -> Json.obj(
             "componentId" -> componentId
         )
    )
    
    val componentOut = wC.handleMessage(componentIn)
    
    Logger.info(this.getClass.getSimpleName + ": componentIn " + componentIn)
    Logger.info(this.getClass.getSimpleName + ": componentOut " + componentOut)
    
    componentOut
  }
  
  def currentCongig(wC: WebClient): JsValue = {
    val jsonCurrentConfigIn : JsValue = Json.obj(
        "json" -> JsonNames.CURRENT_CONFIG
    )
    
    val jsonCurrentConfigOut: JsValue = wC.handleMessage(jsonCurrentConfigIn)
      
    Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn)
    Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut)
    
    jsonCurrentConfigOut
  }
  
  def nextStep(wC: WebClient): JsValue = {
    
    val jsonNextStepIn : JsValue = Json.obj(
        "json" -> JsonNames.NEXT_STEP
    )
      
    val jsonNextStepOut = wC.handleMessage(jsonNextStepIn)
      
    Logger.info(this.getClass.getSimpleName + ": nextStepIn " + jsonNextStepIn)
    Logger.info(this.getClass.getSimpleName + ": nextStepOut " + jsonNextStepOut)
    
    jsonNextStepOut
  }
  
}