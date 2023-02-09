package io.github.xxfast.krouter.sample.api

import io.github.xxfast.krouter.sample.models.NewsStory
import io.github.xxfast.krouter.sample.models.TopStoryResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.path

class NewsWebService(private val client: HttpClient) {
  suspend fun topStories(page: Int = 1): Result<TopStoryResponse> = client
    .get {
      url { path("v1/news/top") }
      parameter("limit", 5)
      parameter("page", page)
    }

  suspend fun story(id: String): Result<NewsStory> = client
    .get { url { path("v1/news/uuid/$id") } }
}
