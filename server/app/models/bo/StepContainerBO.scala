package models.bo

import org.shared.error.Error
import org.shared.warnings.Warning


case class StepContainerBO(
                         configUrl: Option[String] = None,
                         step: Option[StepBO] = None,
                         componentsForSelection: Option[Set[ComponentBO]] = None,
                         error: Option[Set[Error]] = None,
                         warnings: Option[Set[Warning]] = None
                         )
