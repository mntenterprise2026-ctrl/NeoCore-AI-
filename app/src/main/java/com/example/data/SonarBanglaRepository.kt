package com.example.data

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SonarBanglaRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val chatDao = db.chatMessageDao()
    private val settingDao = db.settingDao()
    private val artworkDao = db.artworkDao()

    val chatMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages()
    val galleryArtworks: Flow<List<ArtworkEntity>> = artworkDao.getAllArtworks()

    // Default configuration constants
    companion object {
        const val KEY_THEME = "theme"
        const val KEY_VOICE_GENDER = "voice_gender"
        const val KEY_VOICE_SPEED = "voice_speed"
        const val KEY_VOICE_PITCH = "voice_pitch"
        const val KEY_VOICE_LANGUAGE = "voice_language"
        const val KEY_DARK_MODE_PREF = "dark_mode_pref"
        const val KEY_HAPTIC_ENABLED = "haptic_enabled"
        const val KEY_SPEECH_AUTO_READ_ENABLED = "speech_auto_read_enabled"
        const val KEY_GLOW_EFFECT_ENABLED = "glow_effect_enabled"
        const val KEY_STREAM_TEXT_ENABLED = "stream_text_enabled"
        const val KEY_CREATIVITY_TEMP = "creativity_temp"

        // Theme Names
        const val THEME_ROYAL_BENGAL = "ROYAL_BENGAL"
        const val THEME_FUTURE_GLASS = "FUTURE_GLASS"
        const val THEME_TERRACOTTA = "TERRACOTTA"
        const val THEME_GANGES_BLUE = "GANGES_BLUE"
        const val THEME_BAUL_YELLOW = "BAUL_YELLOW"
    }

    suspend fun getTheme(): String = withContext(Dispatchers.IO) {
        settingDao.getSettingValue(KEY_THEME) ?: THEME_FUTURE_GLASS
    }

    suspend fun saveTheme(theme: String) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_THEME, theme))
    }

    suspend fun getDarkModePref(): String = withContext(Dispatchers.IO) {
        settingDao.getSettingValue(KEY_DARK_MODE_PREF) ?: "LIGHT" // Clean Light Mode by Default!
    }

    suspend fun saveDarkModePref(pref: String) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_DARK_MODE_PREF, pref))
    }

    suspend fun getHapticEnabled(): Boolean = withContext(Dispatchers.IO) {
        (settingDao.getSettingValue(KEY_HAPTIC_ENABLED) ?: "true") == "true"
    }

    suspend fun saveHapticEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_HAPTIC_ENABLED, enabled.toString()))
    }

    suspend fun getSpeechAutoReadEnabled(): Boolean = withContext(Dispatchers.IO) {
        (settingDao.getSettingValue(KEY_SPEECH_AUTO_READ_ENABLED) ?: "false") == "true"
    }

    suspend fun saveSpeechAutoReadEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_SPEECH_AUTO_READ_ENABLED, enabled.toString()))
    }

    suspend fun getGlowEffectEnabled(): Boolean = withContext(Dispatchers.IO) {
        (settingDao.getSettingValue(KEY_GLOW_EFFECT_ENABLED) ?: "true") == "true"
    }

    suspend fun saveGlowEffectEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_GLOW_EFFECT_ENABLED, enabled.toString()))
    }

    suspend fun getStreamTextEnabled(): Boolean = withContext(Dispatchers.IO) {
        (settingDao.getSettingValue(KEY_STREAM_TEXT_ENABLED) ?: "true") == "true"
    }

    suspend fun saveStreamTextEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_STREAM_TEXT_ENABLED, enabled.toString()))
    }

    suspend fun getCreativityTemp(): Float = withContext(Dispatchers.IO) {
        settingDao.getSettingValue(KEY_CREATIVITY_TEMP)?.toFloatOrNull() ?: 0.7f
    }

    suspend fun saveCreativityTemp(temp: Float) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_CREATIVITY_TEMP, temp.toString()))
    }

    suspend fun getVoiceGender(): String = withContext(Dispatchers.IO) {
        settingDao.getSettingValue(KEY_VOICE_GENDER) ?: "FEMALE"
    }

    suspend fun saveVoiceGender(gender: String) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_VOICE_GENDER, gender))
    }

    suspend fun getVoiceSpeed(): Float = withContext(Dispatchers.IO) {
        settingDao.getSettingValue(KEY_VOICE_SPEED)?.toFloatOrNull() ?: 1.0f
    }

    suspend fun saveVoiceSpeed(speed: Float) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_VOICE_SPEED, speed.toString()))
    }

    suspend fun getVoicePitch(): Float = withContext(Dispatchers.IO) {
        settingDao.getSettingValue(KEY_VOICE_PITCH)?.toFloatOrNull() ?: 1.0f
    }

    suspend fun saveVoicePitch(pitch: Float) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_VOICE_PITCH, pitch.toString()))
    }

    suspend fun getVoiceLanguage(): String = withContext(Dispatchers.IO) {
        settingDao.getSettingValue(KEY_VOICE_LANGUAGE) ?: "BENGALI"
    }

    suspend fun saveVoiceLanguage(language: String) = withContext(Dispatchers.IO) {
        settingDao.insertSetting(SettingEntity(KEY_VOICE_LANGUAGE, language))
    }

    suspend fun insertChatMessage(sender: String, text: String, imageUrl: String? = null) = withContext(Dispatchers.IO) {
        chatDao.insertMessage(ChatMessage(sender = sender, text = text, imageUrl = imageUrl))
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        chatDao.clearHistory()
    }

    suspend fun insertArtwork(artwork: ArtworkEntity) = withContext(Dispatchers.IO) {
        artworkDao.insertArtwork(artwork)
    }

    suspend fun deleteArtwork(id: Int) = withContext(Dispatchers.IO) {
        artworkDao.deleteArtwork(id)
    }

    // Call Gemini API to get regional West Bengal cultural chat responses
    suspend fun getChatAnswer(prompt: String, conversationHistory: List<ChatMessage>): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "GEMINI_API_KEY") {
            return@withContext "Hello! I am NeoCore AI, your intelligent assistant. I notice that the Gemini API key has not been configured in the Secrets Panel of AI Studio yet. Please configure it in your Secrets to enable full conversational intelligence!\n\nIn the meantime, I can still guide you about the rich history of West Bengal locally! I was created by Mahefuz Mondal. Feel free to explore our Alpana Canvas to create majestic artworks or check out the regional custom themes!"
        }

        val systemInstruction = """
            You are NeoCore AI, an expert virtual assistant deeply knowledgeable about West Bengal, India's culture, heritage, food, language, and history.
            
            Guidelines:
            1. All interactions must be in English by default. Do NOT use Bengali script unless the user explicitly prompts or asks in Bengali.
            2. If the user prompts in Bengali, only then respond in Bengali script.
            3. Be precise, helpful, polite, and professional like ChatGPT.
            4. If asked about credits or the developer, explicitly identify Mahefuz Mondal as the creator of NeoCore AI.
        """.trimIndent()

        // Gather relevant recent history
        val recentHistory = conversationHistory.takeLast(10)
        val apiContents = mutableListOf<Content>()

        recentHistory.forEach { msg ->
            apiContents.add(Content(listOf(Part(text = if (msg.sender == "USER") msg.text else msg.text))))
        }
        apiContents.add(Content(listOf(Part(text = prompt))))

        val request = GenerateContentRequest(
            contents = apiContents,
            systemInstruction = Content(listOf(Part(text = systemInstruction)))
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "Hello! I could not fetch a response. Please check your network and try again soon."
        } catch (e: Exception) {
            Log.e("SonarBanglaAI", "Network error in Gemini call", e)
            "Hello! A connection issue occurred: ${e.message}. If you are running locally, check your internet. I am always happy to talk to you!"
        }
    }

    // Call Gemini to generate a beautiful, authentic descriptive narrative for the generated Bengal picture
    suspend fun generateArtworkDescription(prompt: String, artType: String): Pair<String, String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val generatedTitle = when (artType) {
            "ALPANA" -> "Traditional Alpana: Bengal Folk Harmony"
            "DURGA", "CYBER_GRID" -> "Cyber AI Network: The Neural Grid"
            "TIGER" -> "Sundarbans Majesty: The Royal Predator"
            "BAUL" -> "Baul Mystic: Echoes of the Soil"
            "HOWRAH" -> "The Howrah Cantilever: Kolkata Lifeline"
            else -> "Bengal Heritage Masterpiece"
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "GEMINI_API_KEY") {
            val fallbackDesc = when (artType) {
                "ALPANA" -> "A beautiful traditional Bengal symmetric circular pattern representing harmony and nature. Hand-drawn off-white lines forming floral, geometric, and ornamental waves."
                "DURGA", "CYBER_GRID" -> "A futuristic digital cybernetic visualization showing pulsing glowing neural lines, orbital intelligence hubs, and cyber data rings."
                "TIGER" -> "The breathtaking Royal Bengal Tiger bathing in the deep Sundarban mangrove delta, surrounded by horizontal respiratory roots, glowing golden sunbeams, and wild green ferns."
                "BAUL" -> "A joyful wandering folk singer carrying the stringed Ek-tara, wrapped in a saffron-stitched custom robe, dancing gracefully beneath the broad foliage of an old banyan tree."
                "HOWRAH" -> "The magnificent cantilever Howrah steel bridge bridging Kolkata and Howrah across the shimmering blue Hooghly river, with traditional wooden wooden boats sailing in the distance."
                else -> "A stunning custom-crafted visual representation honoring the rich heart of West Bengal traditional art."
            }
            return@withContext Pair(generatedTitle, fallbackDesc)
        }

        val requestPrompt = "Generate a beautifully written, short 3-sentence descriptions explaining the symbolic story, colors, and design of: '$prompt' in the traditional West Bengal '$artType' art style. Make it deeply literary, inspiring, and culturally authentic. Do not include markdown headers."
        val systemInstruction = "You are a master of traditional Bengal art history. You summarize the deep artistic, folk, and creative significance of traditional crafts (Patachitra, Alpana, Clay sculpting, Baul traditions, Kolkata heritage)."

        val request = GenerateContentRequest(
            contents = listOf(Content(listOf(Part(text = requestPrompt)))),
            systemInstruction = Content(listOf(Part(text = systemInstruction)))
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val desc = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "A striking masterpiece of traditional Bengal aesthetics."
            
            // Generate a creative, short title
            val titleRequest = "Generate a very short 2-3 word poetic, traditional English title with Bengali script in bracket (e.g., 'Ganges Sunset (গঙ্গার সূর্যাস্ত)') for: '$prompt' categorized as '$artType'."
            val titleResponse = RetrofitClient.service.generateContent(apiContents = listOf(Content(listOf(Part(text = titleRequest)))), apiKey = apiKey)
            val title = titleResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace("\"", "")?.trim() ?: generatedTitle
            
            Pair(title, desc)
        } catch (e: Exception) {
            Pair(generatedTitle, "A striking and colorful representation of traditional Bengali culture, designed with fine details resembling the majestic folk traditions of India.")
        }
    }

    // Helper for generating titles
    private suspend fun GeminiApiService.generateContent(apiContents: List<Content>, apiKey: String): GenerateContentResponse {
        return this.generateContent(apiKey, GenerateContentRequest(contents = apiContents))
    }
}
