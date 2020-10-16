package com.example.music.entity

import com.example.respository.bean.UserJson

/**
 * 用户信息实体类
 */

class User {
    var avatar: String = ""
    var nickname: String = ""
    var userId: Long = -1L

    companion object {
        fun of(userJson: UserJson): User {
            val u = User()
            userJson.profile?.apply {
                u.avatar = avatarUrl ?: ""
                u.nickname = nickname ?: ""
                u.userId = userId ?: -1L
            }
            return u
        }

        fun userMessageError(user: User) = user.avatar.isEmpty() || user.nickname.isEmpty() || user.userId == -1L
    }

    override fun toString(): String {
        return "[User] avatar = $avatar, nickname = $nickname, userId = $userId [User]"
    }
}

