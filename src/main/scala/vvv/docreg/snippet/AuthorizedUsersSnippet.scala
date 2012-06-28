package vvv.docreg.snippet

import net.liftweb.util.Helpers._
import xml.{NodeSeq, Text}
import vvv.docreg.model.{ProjectAuthorization, User, UserLookup}
import org.squeryl.PrimitiveTypeMode._
import net.liftweb.util.{PassThru, ClearClearable}
import net.liftweb.http.js.JsCmds.RedirectTo
import net.liftweb.http.{SHtml, S}
import net.liftweb.common.{Box, Empty, Full}

class AuthorizedUsersSnippet {
  def list = {
    val users = User.authorized()
    val authorizations = ProjectAuthorization.allAuthorizations().groupBy(_._1.userId)
    ClearClearable &
    ".x-user" #> users.map{ u =>
      val projects = authorizations.get(u.id).getOrElse(Nil).map(_._2).sortBy(_.name)
      ".x-name *" #> u.displayName &
      ".x-access *" #> u.accessLevel().toString() &
      ".x-authorizations *" #> projects.map(_.name).mkString(", ")
    }
  }
}