package models.bo

import org.shared.error.Error
import org.shared.warning.Warning


case class StepContainerBO(
                         configUrl: Option[String] = None,
                         step: Option[StepBO] = None,
                         componentsForSelection: Option[Set[ComponentBO]] = None,
                         error: Option[List[Error]] = None,
                         warnings: Option[List[Warning]] = None
                         )
