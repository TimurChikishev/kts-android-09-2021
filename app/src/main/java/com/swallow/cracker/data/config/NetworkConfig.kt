package com.swallow.cracker.data.config

object NetworkConfig {
    const val OAUTH_BASE_URI = "https://oauth.reddit.com/"
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_USER_AGENT = "User-Agent"
    const val HEADER_USER_AGENT_VALUE = "android:com.swallow.cracker:v1.0.0 (by u/swallow)"
    const val LOG_TAG = "Network"
    const val PAGE_SIZE = 10
    const val MAX_SIZE = 100
    const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
}
