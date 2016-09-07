package org.configTree.step

import org.configTree.component.Component

case class Dependency (
                        ifComponents: Seq[String],
                        ifOperator: String,
                        thenComponents: Seq[String],
                        thenOperator: String,
                        ruletype: String
  )