package mk.ukim.finki.voting.api

data class CreatePoll(
        val question: String,
        val options: List<String>,
        val user: String
)

