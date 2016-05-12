

package org.configTree.component

import org.configTree.component.Component

case class ImmutableComponent(
                    id: String,
                    nameToShow: String,
                    nextStep: String
                    ) extends Component