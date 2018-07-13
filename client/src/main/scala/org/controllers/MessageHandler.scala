package org.controllers

import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.JsResult
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import org.scalajs.dom.raw.WebSocket
import org.shared.common.JsonNames
import org.shared.startConfig.json.JsonStartConfigOut

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Created by Gennadi Heimann 25.04.2018
 */
class MessageHandler(websocket: WebSocket) {


  def handleMessage(receivedMessage: JsValue) = (receivedMessage \ "json").asOpt[String] match {
    case Some(JsonNames.START_CONFIG) => startConfig(receivedMessage)
//      case Some(JsonNames.GET_USER) => getUser(receivedMessage)
//      case Some(JsonNames.ADD_CONFIG) => addConfig(receivedMessage)
//      case Some(JsonNames.GET_CONFIGS) => getConfigs(receivedMessage)
//      case Some(JsonNames.DELET_CONFIG) => deleteConfig(receivedMessage)
//      case Some(JsonNames.UPDATE_CONFIG) => updateConfig(receivedMessage)
//      case Some(JsonNames.ADD_STEP) => addStep(receivedMessage)
//      case Some(JsonNames.CONFIG_TREE) => configTree(receivedMessage)
//      case Some(JsonNames.ADD_COMPONENT) => addComponent(receivedMessage)
//      case Some(JsonNames.CONNECT_COMPONENT_TO_STEP) => ??? //connectComponentToStep(receivedMessage, admin)
//      case Some(JsonNames.CREATE_DEPENDENCY) => ??? //createDependency(receivedMessage, admin)
//      case Some(JsonNames.VISUAL_PROPOSAL_FOR_ADDITIONAL_STEPS_IN_ON_LEVEL) => ???
//        visualProposalForAdditionalStepsInOneLevel(receivedMessage, admin)
    case _ => Json.obj("error" -> "keinen Treffer")
  }

  private def startConfig(receivedMessage: JsValue): Unit = {
    val jsonComponentOut: JsResult[JsonStartConfigOut] = Json.fromJson[JsonStartConfigOut](receivedMessage)
    jsonComponentOut match {
      case jCOut: JsSuccess[JsonStartConfigOut] => ??? //new Component(websocket).updateStatus(jCOut.value)
      case e: JsError => println("Errors -> " + JsonNames.START_CONFIG + ": " + JsError.toJson(e).toString())
    }
  }

//  private def getUser(receivedMessage: JsValue): Unit = {
//    val getUserOut: JsResult[JsonUserOut] = Json.fromJson[JsonUserOut](receivedMessage)
//    getUserOut match {
//      case s: JsSuccess[JsonUserOut] => new GetUser(websocket).drawUser(getUserOut.get)
//      case e: JsError => println("Errors -> " + JsonNames.GET_USER + ": " + JsError.toJson(e).toString())
//    }
//  }
//
//  private def getConfigs(receivedMessage: JsValue): Unit = {
//    val getConfigsIn: JsResult[JsonGetConfigsOut] = Json.fromJson[JsonGetConfigsOut](receivedMessage)
//    getConfigsIn match {
//      case s: JsSuccess[JsonGetConfigsOut] => new GetConfig(websocket).drawAllConfigs(getConfigsIn.get)
//      case e: JsError => println("Error -> : " + JsonNames.GET_CONFIGS + " -> " + JsError.toJson(e).toString())
//    }
//  }
//
//  private def configTree(receivedMessage: JsValue) = {
//    val configTreeOut: JsResult[JsonConfigTreeOut] = Json.fromJson[JsonConfigTreeOut](receivedMessage)
//    configTreeOut match {
//      case s: JsSuccess[JsonConfigTreeOut] => s.get
//      case e: JsError => println("Errors -> CREATE_DEPENDENCY: " + JsError.toJson(e).toString())
//    }
//    new ConfigTree(websocket).drawConfigTree(configTreeOut.get)
//  }
//
//  private def addConfig(receivedMessage: JsValue) = {
//    val createConfigOut: JsResult[JsonAddConfigOut] = Json.fromJson[JsonAddConfigOut](receivedMessage)
//    createConfigOut match {
//      case s: JsSuccess[JsonAddConfigOut] => s.get
//      case e: JsError => println("Errors -> ADD_CONFIG: " + JsError.toJson(e).toString())
//    }
//    new CreateConfig(websocket, "").updateStatus(createConfigOut.get)
//   }
//
//  private def deleteConfig(receivedMessage: JsValue) = {
//    val deleteConfigOut: JsResult[JsonDeleteConfigOut] = Json.fromJson[JsonDeleteConfigOut](receivedMessage)
//    deleteConfigOut match {
//      case s: JsSuccess[JsonDeleteConfigOut] => s.get
//      case e: JsError => println("Errors -> CREATE_CONFIG: " + JsError.toJson(e).toString())
//    }
//    new DeleteConfig(websocket).updateStatus(deleteConfigOut.get)
//  }
//
//  private def updateConfig(receivedMessage: JsValue) = {
//    val updateConfigOut: JsResult[JsonUpdateConfigOut] = Json.fromJson[JsonUpdateConfigOut](receivedMessage)
//    updateConfigOut match {
//      case s: JsSuccess[JsonUpdateConfigOut] => s.get
//      case e: JsError => println("Errors -> CREATE_CONFIG: " + JsError.toJson(e).toString())
//    }
//    new EditConfig(websocket).updateStatus(updateConfigOut.get)
//  }
//
//  private def addStep(receivedMessage: JsValue) = {
//    val addFirstStepOut: JsResult[JsonStepOut] = Json.fromJson[JsonStepOut](receivedMessage)
//    addFirstStepOut match {
//      case s: JsSuccess[JsonStepOut] => s.get
//      case e: JsError => println("Errors -> ADD_FIRST_STEP: " + JsError.toJson(e).toString())
//    }
//    new AddStep(websocket).updateStatus(addFirstStepOut.get)
//  }
}