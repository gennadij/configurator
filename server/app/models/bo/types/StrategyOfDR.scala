package models.bo.types

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 03.01.2019
  */
sealed abstract class StrategyOfDR(val value: String)

case object Auto extends StrategyOfDR("auto")
case object SelectableDecision extends StrategyOfDR("selectableDecision")
