package org.configTree.component

import org.configTree.component.Component

/**
  * Created by gennadi on 30.04.16.
  */
case class MutableComponent(
                           id: String,
                           nameToShow: String,
                           nextStep: String,
                           mutableValue: String
                           ) extends Component
