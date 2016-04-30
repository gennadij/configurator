package org.configTree

/**
  * Created by gennadi on 29.04.16.
  */
// TODO nextStep: Boolean
case class Step (
               id: String,
               nameToShow: String = "standard",
               nextStep: String = "999",
               isStartStep: String = "false",
               components: Seq[Component] = Seq.empty
               ) extends ConfigTree
