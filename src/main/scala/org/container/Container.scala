package org.container

import org.configTree.Step

/**
  * Created by gennadi on 29.04.16.
  */
case class Container(
                    currentConfig: Seq[Step],
                    configSettings:Seq[Step],
                    tempConfig:Seq[Step]
                    )
