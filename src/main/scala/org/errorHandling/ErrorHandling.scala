package org.errorHandling

import org.configTree.step.Step
import org.configTree.component.Component

abstract class ErrorHandling extends Step{
  //  //ErrorStep 
  override def errorComponent: Seq[Component] = Nil
  override def errorMessage: String = ""
    //SaccessStep
  override def succsessMessage: String = ""
  
}