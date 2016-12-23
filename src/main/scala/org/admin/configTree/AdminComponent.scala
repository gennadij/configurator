///**
// * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
// */
//
//package org.admin.configTree
//
//import play.api.libs.json.Json
//
//case class AdminComponent(
//                      id: String,
//                      componentId: String,
//                      adminId: String,
//                      //immutable, mutable
//                      kind: String
//                    )
//                    
//object AdminComponent {
//  implicit val format = Json.format[AdminComponent]
//}