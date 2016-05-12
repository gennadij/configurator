package org.configTree.component


case class ImmutableComponent(
                    id: String,
                    nameToShow: String,
                    nextStep: String
                    ) extends Component