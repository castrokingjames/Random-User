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
package io.github.castrokingjames.datasource.remote

const val RESPONSE_EMPTY_USER = """
    {"results":[],"info":{"seed":"39f7c8a6519dbc07","results":1,"page":1,"version":"1.4"}}
"""

const val RESPONSE_1_USER = """
    {"results":[{"gender":"female","name":{"title":"Ms","first":"Gema","last":"Herrera"},"location":{"street":{"number":2498,"name":"Ronda de Toledo"},"city":"Burgos","state":"Castilla y León","country":"Spain","postcode":48420,"coordinates":{"latitude":"-38.1081","longitude":"35.5091"},"timezone":{"offset":"+4:00","description":"Abu Dhabi, Muscat, Baku, Tbilisi"}},"email":"gema.herrera@example.com","login":{"uuid":"66d93f25-69ad-4d7c-87b5-d1188496a6b6","username":"goldenswan162","password":"qwer","salt":"MP1ZCYXD","md5":"c0658dd2510410f61322a0dbfdeb7cb6","sha1":"44013b65ae2ada43ae5a4bc45a52b78cd23ddf32","sha256":"0f7615d8db3dd3fe7185519478e9d71d79499ea1089fa226ccb3056090b890b8"},"dob":{"date":"1948-07-17T21:22:37.218Z","age":76},"registered":{"date":"2012-06-20T14:33:35.677Z","age":12},"phone":"925-464-740","cell":"626-905-593","id":{"name":"DNI","value":"70505274-V"},"picture":{"large":"https://randomuser.me/api/portraits/women/52.jpg","medium":"https://randomuser.me/api/portraits/med/women/52.jpg","thumbnail":"https://randomuser.me/api/portraits/thumb/women/52.jpg"},"nat":"ES"}],"info":{"seed":"39f7c8a6519dbc07","results":1,"page":1,"version":"1.4"}}
"""
