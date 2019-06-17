package mk.ukim.finki.voting.repository

import mk.ukim.finki.voting.domain.Option
import org.springframework.data.jpa.repository.JpaRepository

interface OptionRepository : JpaRepository<Option, Long> {
    fun getByPollId(id: Long): List<Option>
}
