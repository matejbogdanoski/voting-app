package mk.ukim.finki.voting.api

data class CreateUser(
        val email: String,
        val firstName: String,
        val lastName: String,
        val password: String,
        val pictureUrl: String,
        val username: String
)
