package configMgr

// TODO nestStepp: Boolean
case class Step(
               id: String,
               nameToShow: String,
               nextStep: String,
               isStartStep: String,
               components: Seq[Component]
               )