package dinf.adapters

import dinf.domain.ID
import dinf.domain.SearchIndexRepository
import dinf.domain.SearchQuery
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize

class FailoverSearchIndexRepositoryTest : StringSpec({

    "use main method result if no error happened" {
        val expected = List(3) { ID.first() }

        val repository = FailoverSearchIndexRepository(
            main = SearchIndexRepository.Stub(expected),
            fallback = SearchIndexRepository.Empty()
        )

        val result = repository.search(SearchQuery("test"))

        result shouldContainExactly expected
    }

    "switch to fallback on error" {
        val repository = FailoverSearchIndexRepository(
            main = SearchIndexRepository.Error(),
            fallback = SearchIndexRepository.Empty()
        )

        val result = repository.search(SearchQuery("test"))

        result shouldHaveSize 0
    }

})
