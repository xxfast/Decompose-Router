package io.github.xxfast.krouter.sample.api

import io.github.xxfast.krouter.sample.models.Article
import io.github.xxfast.krouter.sample.models.ArticleUri
import io.github.xxfast.krouter.sample.models.TopStoryResponse
import io.github.xxfast.krouter.sample.models.TopStorySection
import io.ktor.client.HttpClient
import io.ktor.http.path

class NyTimesWebService(private val client: HttpClient) {
  suspend fun topStories(section: TopStorySection): Result<TopStoryResponse> = client
    .get { url { path("svc/topstories/v2/${section.name}.json") } }

  suspend fun story(section: TopStorySection, uri: ArticleUri): Result<Article?> = client
    .get<TopStoryResponse> { url { path("svc/topstories/v2/${section.name}.json") } }
    .map { response -> response.results.find { article -> article.uri == uri } }
}
