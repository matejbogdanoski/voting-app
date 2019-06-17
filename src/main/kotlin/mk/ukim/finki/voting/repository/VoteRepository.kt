package mk.ukim.finki.voting.repository

import mk.ukim.finki.voting.domain.Poll
import mk.ukim.finki.voting.domain.Vote
import org.springframework.data.jpa.repository.JpaRepository

interface VoteRepository : JpaRepository<Vote, Long> {
    fun existsByIpAddressAndPoll(ip: String, poll: Poll): Boolean
}
