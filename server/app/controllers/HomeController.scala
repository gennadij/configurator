package controllers

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import controllers.websocket.{ClientActor, WebClientsMgr}
import javax.inject._
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit actorSystem: ActorSystem, mat: Materializer) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(""))
  }
  
  val webClientsMgr: ActorRef = actorSystem.actorOf(WebClientsMgr.props(), "WebClientsMgr")
  
  def websocket: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef { out => 
      ClientActor.props(out, webClientsMgr)
    }
  }
}
