package org.admin.configTree

import org.configTree.step.SelectionCriterium

case class AdminNextStep (
                      status: Boolean,
                      componentId: String,
                  id:String,
                   stepId: String,
                   adminId: String,
                  //first, default, final
                  kind: String,
                  selectionCriterium: SelectionCriterium
)