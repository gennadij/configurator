package org.views

import org.scalajs.jquery.{JQuery, jQuery}
import org.shared.component.json.JsonComponentStatus

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 01.10.2018
  */
class DrawSelectedComponent(jsonComponentStatus: JsonComponentStatus) {

  def markSelectedComponent(selectedComponentId: String): JQuery = {
    jsonComponentStatus.selectedComponent.get.status match {
      case "ADDED_COMPONENT"        =>
        jQuery(s"#$selectedComponentId").css("background-color", "#9FF781")
      case "REMOVED_COMPONENT"      =>
        jQuery(s"#$selectedComponentId").css("background-color", "#F5A9D0")
      case "NOT_ALLOWED_COMPONENT"  =>
        jQuery(s"#$selectedComponentId").css("background-color", "#B40431")
    }
  }

//  def drawStatus: JQuery = {
//    val htmlHeader =
//      s"<dev id='status' class='status'>" +
//        "componentType = " + jsonComponentStatus.componentType.get.status +
//        " , " +
//        "selectionCriterium = " + jsonComponentStatus.selectionCriterium.get.status +
//        " , " + "</br>" +
//        "selectedComponent = " + jsonComponentStatus.selectedComponent.get.status +
//        " , " +
//        "excludeDependency = " + jsonComponentStatus.excludeDependency.get.status +
//        " , " +
//        "common = " + jsonComponentStatus.common.get.status +
//        "</dev>"
//
//    jQuery("#status").remove()
//    jQuery(htmlHeader).appendTo(jQuery("header"))
//  }


}
