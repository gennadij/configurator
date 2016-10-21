package org.admin.configTree

case class AdminConfigTreeStep (
  components: List[AdminComponent],
  nextSteps: List[AdminNextStep]
)