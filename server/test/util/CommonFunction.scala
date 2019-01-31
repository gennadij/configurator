package util

import controllers.websocket.WebClient
import org.shared.json.{JsonKey, JsonNames}
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 09.03.2018
 */
object CommonFunction {
  
  def firstStep(wC: WebClient, configUrl: String): JsValue = {
    val startConfigIn = Json.obj(
          JsonKey.json -> JsonNames.STEP
          ,JsonKey.params-> Json.obj(
               JsonKey.params -> configUrl
           )
      )
      
      val startConfigOut = wC.handleMessage(startConfigIn, wC.currentConfig)
      
      Logger.info(this.getClass.getSimpleName + ": startConfigIn " + startConfigIn)
      Logger.info(this.getClass.getSimpleName + ": startConfigOut " + startConfigOut)
      
      startConfigOut
  }
  
  def selectComponent(wC: WebClient, componentId: String): JsValue = {
    val componentIn = Json.obj(
        JsonKey.json -> JsonNames.SELECTED_COMPONENT
        ,JsonKey.params-> Json.obj(
             JsonKey.componentId -> componentId
         )
    )
    
    val componentOut = wC.handleMessage(componentIn, wC.currentConfig)
    
    Logger.info(this.getClass.getSimpleName + ": componentIn " + componentIn)
    Logger.info(this.getClass.getSimpleName + ": componentOut " + componentOut)
    
    componentOut
  }
  
  def currentCongig(wC: WebClient): JsValue = {
    val jsonCurrentConfigIn : JsValue = Json.obj(
        JsonKey.json -> JsonNames.CURRENT_CONFIG
    )
    
    val jsonCurrentConfigOut: JsValue = wC.handleMessage(jsonCurrentConfigIn, wC.currentConfig)
      
    Logger.info(this.getClass.getSimpleName + ": currentConfigIn " + jsonCurrentConfigIn)
    Logger.info(this.getClass.getSimpleName + ": currentConfigOut " + jsonCurrentConfigOut)
    
    jsonCurrentConfigOut
  }
  
  def nextStep(wC: WebClient): JsValue = {
    
    val jsonNextStepIn : JsValue = Json.obj(
        JsonKey.json -> JsonNames.STEP
    )
      
    val jsonNextStepOut = wC.handleMessage(jsonNextStepIn, wC.currentConfig)
      
    Logger.info(this.getClass.getSimpleName + ": nextStepIn " + jsonNextStepIn)
    Logger.info(this.getClass.getSimpleName + ": nextStepOut " + jsonNextStepOut)
    
    jsonNextStepOut
  }
  
}