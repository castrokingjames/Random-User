/*
 * Copyright 2024, King James Castro and project contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.castrokingjames.timber

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import timber.log.Timber
import timber.log.Tree

class JvmTree @JvmOverloads constructor(private val defaultTag: String = "App") : Tree() {

  private val logger: Logger = LoggerFactory.getLogger(defaultTag)

  override fun performLog(priority: Int, tag: String?, throwable: Throwable?, message: String?) {
    val sb = StringBuilder()
    if (message != null) {
      sb.append(message)
      sb.append(" ")
    }
    if (throwable != null) {
      sb.append(throwable)
    }
    val message = sb.toString()
    when (priority) {
      Timber.ERROR -> logger.error(message)
      Timber.DEBUG -> logger.debug(message)
      Timber.WARNING -> logger.warn(message)
      else -> logger.info(message)
    }
  }
}
