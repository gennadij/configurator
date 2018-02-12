package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import akka.actor.ActorSystem
import akka.actor.Props
import models.websocket.WebClientsMgr
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import models.websocket.ClientActor
import akka.stream.Materializer

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class Controller @Inject()(cc: ControllerComponents) (implicit actorSystem: ActorSystem, mat: Materializer) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  
  val webClientsMgr = actorSystem.actorOf(WebClientsMgr.props(), "WebClientsMgr")
  
  def websocket = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef { out => 
      ClientActor.props(out, webClientsMgr)
    }
  }
}
