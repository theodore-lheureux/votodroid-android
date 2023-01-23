package com.theodorelheureux.votodroid_android.services

import com.theodorelheureux.votodroid_android.GetUserQuery
import com.theodorelheureux.votodroid_android.graphql.GQLClient

object UserService {
    private val gqlClient = GQLClient.instance

    suspend fun getUser(): GetUserQuery.User? {
        val query = gqlClient.query(GetUserQuery()).execute()

        if (query.hasErrors()) {
            return null
        }
        return query.data!!.users.me.user
    }

    suspend fun logout() {
        gqlClient.query(GetUserQuery()).execute()
    }

}