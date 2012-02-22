package events

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterAll

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props

import akka.util.duration._
import akka.util.Timeout

import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import akka.testkit.EventFilter

import akka.pattern.ask

import akka.dispatch.Await

import com.typesafe.config.ConfigFactory

class EventsActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender 
  with FlatSpec with ShouldMatchers with BeforeAndAfterAll { 

    def this() = this(ActorSystem("EventsActorSpec"))

    override def afterAll { 
      system.shutdown()
    }

    import events._

    "An Events Actor" should "respond to fire and forget messages" in { 

      val eventActorRef = system.actorOf(Props[EventsActor])

      eventActorRef ! EventsListQuery("jboner","akka")
      expectMsgClass( 2 seconds, classOf[List[Event]])
    }

    ignore should "you would think send and receive eventually would send back a msg" in { 

      val eventActorRef = system.actorOf(Props[EventsActor])

      implicit val timeout = Timeout(2 seconds)

      within(2 seconds){ 

	eventActorRef ? EventsListQuery("jboner", "akka")

	expectMsgClass( 2 seconds, classOf[List[Event]])	
      }      
    }

    it should "respond indirectly to send and receive messages" in { 

      val eventActorRef = system.actorOf(Props[EventsActor])

      implicit val timeout = Timeout(2 seconds)
      val events = 
	Await.result(eventActorRef ? EventsListQuery("jboner","akka"), 
		     timeout.duration).asInstanceOf[List[Event]]

      events should not be ('empty)
    }

    it should "respond indirectly to send and receive message within timing assertion" in { 

      val eventActorRef = system.actorOf(Props[EventsActor])

      implicit val timeout = Timeout(2 seconds)

      within(2 seconds){ 
	
	val events =
	  Await.result(eventActorRef ? EventsListQuery("jboner","akka"), 
		       timeout.duration).asInstanceOf[List[Event]]

	events should not be ('empty)
      }
    }
}
