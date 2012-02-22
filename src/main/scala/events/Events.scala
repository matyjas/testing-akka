package events

import akka.actor.{ Actor, ActorLogging }

case class EventsListQuery(val user:String, val repo:String)

class EventsActor extends Actor with ActorLogging { 

  def receive = { 

    case EventsListQuery(user, repo) =>

      // this blocks a little
      val events = new EventsRequest("jboner","akka")

      log.debug("events string length is :: " + events.events.length)

      sender ! events.events

    case unknown =>

      log.debug("unknown message => " + unknown)
    
  }
}

case class Event(typeOf: String, login: String, created: String)

class EventsRequest(user: String, repo: String) { 

  import dispatch._
  import dispatch.liftjson.Js._
  import net.liftweb.json.DefaultFormats
  import net.liftweb.json._

  implicit val formats = DefaultFormats

  val gh = :/("api.github.com") / "repos" / user / repo / "events"

  val jsonEvents = Http(gh.secure ># { json => json.extract[List[JObject]]})

  val events = jsonEvents.map{ jsonEvent => 

    val typeOf = compact(render(jsonEvent \ "type"))
    val login = compact(render(jsonEvent \ "actor" \ "login"))
    val created = compact(render(jsonEvent \ "created_at"))
    Event(typeOf, login, created)  
    }
}
