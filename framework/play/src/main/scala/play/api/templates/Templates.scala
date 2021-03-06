package play.api.templates

import play.api.mvc._
import play.templates._

/**
 * Content type used in default html templates.
 *
 * @param text The html text.
 */
case class Html(text: String) extends Appendable[Html] with Content {
  val buffer = new StringBuilder(text)

  /**
   * Append this Html fragment with another.
   */
  def +(other: Html) = {
    buffer.append(other.buffer)
    this
  }
  override def toString = buffer.toString

  /**
   * Content type of Html (text/html)
   */
  def contentType = "text/html"

  def body = toString

}

/**
 * Helper for utilities HTML methods.
 */
object Html {

  /**
   * Create an empty HTML fragment.
   */
  def empty = Html("")

}

/**
 * Formatter for Html content.
 */
object HtmlFormat extends Format[Html] {

  /**
   * Create a raw (unescaped) Html fragment.
   */
  def raw(text: String) = Html(text)

  /**
   * Create a safe (escaped) Html fragment.
   */
  def escape(text: String) = Html(org.apache.commons.lang.StringEscapeUtils.escapeHtml(text))

}

/**
 * Content type used in default text templates.
 *
 * @param text The plain text.
 */
case class Txt(text: String) extends Appendable[Txt] with Content {
  val buffer = new StringBuilder(text)

  /**
   * Append this Txt fragment with another.
   */
  def +(other: Txt) = {
    buffer.append(other.buffer)
    this
  }
  override def toString = buffer.toString

  /**
   * Content type of Txt (text/plain)
   */
  def contentType = "text/plain"

  def body = toString

}

/**
 * Helper for utilities Txt methods.
 */
object Txt {

  /**
   * Create an empty Txt fragment.
   */
  def empty = Txt("")

}

/**
 * Formatter for Txt content.
 */
object TxtFormat extends Format[Txt] {

  /**
   * Create a Txt fragment.
   */
  def raw(text: String) = Txt(text)

  /**
   * No need for a safe (escaped) Txt fragment.
   */
  def escape(text: String) = Txt(text)

}

/**
 * Content type used in default xml templates.
 *
 * @param text The plain xml text.
 */
case class Xml(text: String) extends Appendable[Xml] with Content {
  val buffer = new StringBuilder(text)

  /**
   * Append this Xml fragment with another.
   */
  def +(other: Xml) = {
    buffer.append(other.buffer)
    this
  }
  override def toString = buffer.toString

  /**
   * Content type of Xml (text/xml)
   */
  def contentType = "text/xml"

  def body = toString

}

/**
 * Helper for utilities Xml methods.
 */
object Xml {

  /**
   * Create an empty Txt fragment.
   */
  def empty = Xml("")

}

/**
 * Formatter for Xml content.
 */
object XmlFormat extends Format[Xml] {

  /**
   * Create a Xml fragment.
   */
  def raw(text: String) = Xml(text)

  /**
   * No need for a safe (escaped) Txt fragment.
   */
  def escape(text: String) = Xml(org.apache.commons.lang.StringEscapeUtils.escapeXml(text))

}

/**
 * Defines magic helper in Play templates.
 */
object PlayMagic {

  import scala.collection.JavaConverters._

  /**
   * Transform a Play java option to a proper scala option.
   */
  implicit def javaOptionToScala[T](x: play.libs.F.Option[T]): Option[T] = x match {
    case x: play.libs.F.Some[T] => Some(x.get)
    case x: play.libs.F.None[T] => None
  }

  /**
   * Generate a set of valid HTML attributes.
   *
   * Example:
   * {{{
   * toHtmlArgs(Seq('id -> "item", 'style -> "color:red"))
   * }}}
   */
  def toHtmlArgs(args: Seq[(Symbol, Any)]) = Html(args.map(a => a._1.name + "=\"" + a._2 + "\"").mkString(" "))

  /**
   * Transform a Play java Form fiel to a proper Scala Form field.
   */
  implicit def javaFieldtoScalaField(field: play.data.Form.Field) = {
    play.api.data.Field(
      field.name,
      field.constraints.asScala.map { jT =>
        jT._1 -> jT._2.asScala
      },
      Option(field.format).map(f => f._1 -> f._2.asScala),
      field.errors.asScala.map { jE =>
        play.api.data.FormError(
          jE.key,
          jE.message,
          jE.arguments.asScala)
      },
      Option(field.value))
  }

}
