package mk.ukim.finki.voting.api

import mk.ukim.finki.voting.domain.Option
import mk.ukim.finki.voting.domain.User
import java.time.LocalDateTime


data class PollJSON(
        val id: Long,
        val question: String,
        val options: List<Option>,
        val user: User,
        val dateCreated: LocalDateTime,
        val shortUrl: String
)
