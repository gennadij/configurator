package org.configTree.staticStep

import org.configTree._
import org.configTree.staticStep._

/**
  * Created by gennadi on 29.04.16.
  */

abstract class AbstractStep extends ConfigTree


// TODO nextStep: Boolean
case class Step (
                  id: String,
                  nameToShow: String,
                  nextStep: String,
                  isStartStep: String,
                  components: Seq[Component]
                )extends AbstractStep


case class StaticStep(
                       id: String,
                       nameToShow: String,
                       nextStep: Seq[NextStep],
                       kind: String,
                       selectionCriterium: SelectionCriterium = null,
                       from: Source,
                       components: Seq[StaticComponent]
                     ) extends AbstractStep

class FirstStep {

}

class CurrentStep {

}

case class LastStep (
                      id: String,
                      nameToShow: String,
                      nextStep: String,
                      isStartStep: String,
                      components: Seq[Component]
                    ) extends AbstractStep
