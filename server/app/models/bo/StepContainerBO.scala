package models.bo

case class StepContainerBO(
                         configUrl: Option[String] = None,
                         step: Option[StepBO] = None,
                         componentsForSelection: Option[ComponentBO] = None,
                         error: Option[List[ErrorBO]]
                         )
