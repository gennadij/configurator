package org.shared.warnings

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 18.01.2019
  */
sealed abstract class Warning (val message: String,
                    val name: String,
                    val code: String)

//case class TestWarning extends Warning(
//  message = "",
//  name = "",
//  code = ""
//)
