package code.rest

import net.liftweb.common._
import net.liftweb.http.{JsonResponse, S}
import net.liftweb.http.rest.RestHelper
import net.liftweb.json.JValue
import net.liftweb.json.JsonDSL._


object FileUpload extends RestHelper {
  serve {
    case "upload" :: id :: Nil Post req if authorized_?(id) =>
	println("ASDFGFFf")
	val results: Box[List[JValue]] = S.session.flatMap(_.findFunc(id)).map(func => req.uploadedFiles.map(func(_)).asInstanceOf[List[JValue]])
	
      results match {
        case Full(upload)       => {println(upload.toString);JsonResponse(("files" -> upload), 200)}
        case Failure(msg, _, _) => {println("BBB");JsonResponse(msg, 400)}
	  case Empty              => {println("CCC");JsonResponse("An error occured", 500)}
      }
  }

  def authorized_?(id: String): Boolean = S.session.dmap(false)(_.findFunc(id).map(_ => true).getOrElse(false))
}