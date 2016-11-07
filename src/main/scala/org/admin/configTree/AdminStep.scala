package org.admin.configTree

import org.configTree.step.SelectionCriterium
import play.api.libs.json.Writes
import play.api.libs.json.Json

case class AdminStep (
//  status: Boolean,
  id:String
  ,stepId: String
  ,adminId: String
  //first, default, final
  ,kind: String
//  ,selectionCriterium: SelectionCriterium
                )