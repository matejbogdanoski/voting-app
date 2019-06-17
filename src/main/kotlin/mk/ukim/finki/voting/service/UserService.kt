package mk.ukim.finki.voting.service

import mk.ukim.finki.voting.api.exceptions.EmailExistsException
import mk.ukim.finki.voting.api.exceptions.UserNotFoundException
import mk.ukim.finki.voting.api.exceptions.UsernameExistsException
import mk.ukim.finki.voting.domain.User
import mk.ukim.finki.voting.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class UserService(val userRepository: UserRepository) {

    val logger = LoggerFactory.getLogger(UserService::class.java)

    fun createUser(email: String, userName: String, password: String, firstName: String,
                   lastName: String, pictureUrl: String): User {

        if (userRepository.existsByUsername(userName)) {
            logger.info("Username already exists [{}]", userName)
            throw UsernameExistsException()
        }
        if (userRepository.existsByEmail(email)) {
            logger.info("Email already exists [{}]", email)
            throw EmailExistsException()
        }

        val user = User(email = email, username = userName,
                password = BCryptPasswordEncoder().encode(password),
                firstName = firstName, lastName = lastName, pictureUrl = pictureUrl)
        logger.info("Saving user [{}]", user)
        return userRepository.save(user)
    }


    fun findByUsername(userName: String): User {

        if (!userRepository.existsByUsername(userName)) {
            logger.info("Username [{}] not found", userName)
            throw UserNotFoundException()
        }

        logger.info("Username found [{}]", userName)
        return userRepository.findByUsername(userName).orElseThrow { UserNotFoundException() }
    }

    fun getUserForPrincipal(principal: Principal): User {
        return userRepository.findByUsername(principal.name).orElseThrow {
            UserNotFoundException()
        }
    }


}
