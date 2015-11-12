import akka.actor.ActorSystem
import akka.pattern.ask
import play.module.io.joaovasques.playspark.akkaguice.{AkkaModule, GuiceAkkaExtension}
import com.google.inject.Guice
import config.ConfigModule
import net.codingwell.scalaguice.InjectorExtensions._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import play.module.io.joaovasques.playspark.execution.{ExectionModule}
import play.module.io.joaovasques.playspark.core.{CoreModule, CoreActor}
import play.module.io.joaovasques.playspark.persistence._


/**
 * A main class to start up the application.
 */
object Main extends App {

  val injector = Guice.createInjector(
    new ConfigModule(),
    new AkkaModule(),
    new ExectionModule(),
    new PersistenceModule(),
    new CoreModule()
  )

  val system = injector.instance[ActorSystem]
  val core = system.actorOf(GuiceAkkaExtension(system).props(CoreActor.name))

  core ! "Hey"

  // // this could be called inside a supervisor actor to create a supervisor hierarchy,
  // // using context.actorOf(GuiceAkkaExtension(context.system)...
  //val counter = system.actorOf(GuiceAkkaExtension(system).props(CountingActor.name))

  // // tell it to count three times
  //counter ! Count
  // counter ! Count
  // counter ! Count

  // // Create a second counter to demonstrate that `AuditCompanion` is injected under Prototype
  // // scope, which means that every `CountingActor` will get its own instance of `AuditCompanion`.
  // // However `AuditBus` is injected under Singleton scope. Therefore every `AuditCompanion`
  // // will get a reference to the same `AuditBus`.
  // val counter2 = system.actorOf(GuiceAkkaExtension(system).props(CountingActor.name))
  // counter2 ! Count
  // counter2 ! Count

  // // print the result
  // for {
  //   actor <- Seq(counter, counter2)
  //   result <- actor.ask(Get)(3.seconds).mapTo[Int]
  // } {
  //   println(s"Got back $result from $counter")
  // }

  //system.shutdown()
  //system.awaitTermination()
}