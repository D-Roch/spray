/*
 * Copyright © 2011-2013 the spray project <http://spray.io>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spray.http

import java.net.{ UnknownHostException, InetAddress }

sealed abstract class RemoteAddress extends ValueRenderable {
  def toOption: Option[InetAddress]
}

object RemoteAddress {
  case object Unknown extends RemoteAddress {
    def toOption = None
    def render[R <: Rendering](r: R): r.type = r ~~ "unknown"
  }

  case class IP(ip: InetAddress) extends RemoteAddress {
    def toOption: Option[InetAddress] = Some(ip)
    def render[R <: Rendering](r: R): r.type = r ~~ ip.getHostAddress
  }

  def apply(s: String): RemoteAddress =
    try IP(InetAddress.getByName(s)) catch { case _: UnknownHostException ⇒ Unknown }

  def fromInetAddress(a: InetAddress): IP = IP(a)
}