package mk.ukim.finki.voting.service

import mk.ukim.finki.voting.api.PollJSON
import mk.ukim.finki.voting.api.exceptions.PollNotFoundException
import mk.ukim.finki.voting.api.exceptions.UserNotFoundException
import mk.ukim.finki.voting.api.exceptions.UserVotedException
import mk.ukim.finki.voting.domain.Option
import mk.ukim.finki.voting.domain.Poll
import mk.ukim.finki.voting.domain.User
import mk.ukim.finki.voting.domain.Vote
import mk.ukim.finki.voting.repository.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest


@Service
class PollService(val pollRepository: PollRepository, val optionRepository: OptionRepository,
                  val shortUrlService: ShortUrlService, val voteRepository: VoteRepository,
                  val userRepository: UserRepository) {

    val logger = LoggerFactory.getLogger(PollService::class.java)

    fun createPoll(question: String, options: List<String>, user: User): Poll {
        val poll = Poll(question = question, shortUrl = shortUrlService.generateShortUrl(), user = user)
        logger.info("Saving poll [{}]", poll)
        pollRepository.save(poll)
        for (o in options) {
            val option = Option(value = o, poll = poll)
            logger.info("Saving options [{}]", option)
            optionRepository.save(option)
        }
        return poll
    }

    fun getByShortUrl(shortUrl: String): PollJSON {
        return if (pollRepository.existsByShortUrl(shortUrl)) {
            val poll = pollRepository.getByShortUrl(shortUrl)
            val options = optionRepository.getByPollId(poll.id)
            PollJSON(poll.id, poll.question, options, poll.user, poll.dateCreated, poll.shortUrl)
        } else {
            throw PollNotFoundException()
        }
    }

    fun showLatestPolls() = pollRepository.getLatest10()


    fun showMostVotedPolls() = pollRepository.findTopTrending()

    fun getPollsByUsername(username: String) = pollRepository.findByUser(
            userRepository.findByUsername(username).map { it.id }.orElseThrow{UserNotFoundException()})

    fun vote(ip: String, optionId: Long, pollId: Long): Boolean {
        val poll = pollRepository.findById(pollId).get()
        val option = optionRepository.findById(optionId).get()
        val vote = Vote(ipAddress = ip, poll = poll, option = option)
        return if (voteRepository.existsByIpAddressAndPoll(ip, poll)) {
            logger.info("User with IP [{}] already voted", ip)
            throw UserVotedException()
        } else {
            option.timesVoted++
            logger.info("Vote saving [{}]", vote)
            voteRepository.save(vote)
            true
        }
    }

    fun getClientIpAddr(request: HttpServletRequest): String {
        var ip: String? = request.getHeader("X-Forwarded-For")
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_CLIENT_IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
        }
        return ip ?: ""
    }
}
