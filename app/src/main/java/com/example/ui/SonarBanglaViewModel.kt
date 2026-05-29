package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.Locale

class SonarBanglaViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {
    private val repository = SonarBanglaRepository(application)

    // Reactive StateFlows from Room
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val galleryArtworks: StateFlow<List<ArtworkEntity>> = repository.galleryArtworks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Settings States
    private val _themeMode = MutableStateFlow(SonarBanglaRepository.THEME_FUTURE_GLASS)
    val themeMode: StateFlow<String> = _themeMode

    private val _darkModePref = MutableStateFlow("LIGHT") // LIGHT, DARK, SYSTEM
    val darkModePref: StateFlow<String> = _darkModePref

    private val _hapticEnabled = MutableStateFlow(true)
    val hapticEnabled: StateFlow<Boolean> = _hapticEnabled

    private val _speechAutoReadEnabled = MutableStateFlow(false)
    val speechAutoReadEnabled: StateFlow<Boolean> = _speechAutoReadEnabled

    private val _glowEffectEnabled = MutableStateFlow(true)
    val glowEffectEnabled: StateFlow<Boolean> = _glowEffectEnabled

    private val _streamTextEnabled = MutableStateFlow(true)
    val streamTextEnabled: StateFlow<Boolean> = _streamTextEnabled

    private val _creativityTemp = MutableStateFlow(0.7f)
    val creativityTemp: StateFlow<Float> = _creativityTemp

    private val _voiceGender = MutableStateFlow("FEMALE")
    val voiceGender: StateFlow<String> = _voiceGender

    private val _voiceSpeed = MutableStateFlow(1.0f)
    val voiceSpeed: StateFlow<Float> = _voiceSpeed

    private val _voicePitch = MutableStateFlow(1.0f)
    val voicePitch: StateFlow<Float> = _voicePitch

    private val _voiceLanguage = MutableStateFlow("BENGALI")
    val voiceLanguage: StateFlow<String> = _voiceLanguage

    // Interactive UI States
    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading

    private val _isArtGenerating = MutableStateFlow(false)
    val isArtGenerating: StateFlow<Boolean> = _isArtGenerating

    private val _artGeneratingProgress = MutableStateFlow(0f)
    val artGeneratingProgress: StateFlow<Float> = _artGeneratingProgress

    private val _artGeneratingSecondsLeft = MutableStateFlow(29)
    val artGeneratingSecondsLeft: StateFlow<Int> = _artGeneratingSecondsLeft

    private val _artGeneratingStepText = MutableStateFlow("Initializing NeoCore Engine...")
    val artGeneratingStepText: StateFlow<String> = _artGeneratingStepText

    private val _currentGeneratedArt = MutableStateFlow<ArtworkEntity?>(null)
    val currentGeneratedArt: StateFlow<ArtworkEntity?> = _currentGeneratedArt

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    // Native TTS Engine
    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false

    init {
        // Load settings from Database
        viewModelScope.launch {
            _themeMode.value = repository.getTheme()
            _darkModePref.value = repository.getDarkModePref()
            _hapticEnabled.value = repository.getHapticEnabled()
            _speechAutoReadEnabled.value = repository.getSpeechAutoReadEnabled()
            _glowEffectEnabled.value = repository.getGlowEffectEnabled()
            _streamTextEnabled.value = repository.getStreamTextEnabled()
            _creativityTemp.value = repository.getCreativityTemp()
            _voiceGender.value = repository.getVoiceGender()
            _voiceSpeed.value = repository.getVoiceSpeed()
            _voicePitch.value = repository.getVoicePitch()
            _voiceLanguage.value = repository.getVoiceLanguage()
        }

        // Initialize Android Text To Speech
        try {
            tts = TextToSpeech(application, this)
        } catch (e: Exception) {
            Log.e("SonarBanglaVM", "Failed to initialize TTS engine helper", e)
        }
    }

    // TTS OnInit Interface
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            applyTtsSettings()
        } else {
            Log.e("SonarBanglaVM", "TextToSpeech initialization failed with status: $status")
        }
    }

    // Apply pitch and speed dynamically to TTS
    private fun applyTtsSettings() {
        val engine = tts ?: return
        if (!isTtsInitialized) return

        try {
            // Set Pitch
            engine.setPitch(_voicePitch.value)
            
            // Set Speaking Rate (Speed)
            engine.setSpeechRate(_voiceSpeed.value)

            // Select Locale based on Settings Language Preferences
            val locale = when (_voiceLanguage.value) {
                "HINDI" -> Locale("hi", "IN")
                "ENGLISH" -> Locale("en", "US")
                "SPANISH" -> Locale("es", "ES")
                "FRENCH" -> Locale("fr", "FR")
                "GERMAN" -> Locale("de", "DE")
                "JAPANESE" -> Locale("ja", "JP")
                "CHINESE" -> Locale("zh", "CN")
                "KOREAN" -> Locale("ko", "KR")
                "RUSSIAN" -> Locale("ru", "RU")
                "ITALIAN" -> Locale("it", "IT")
                else -> Locale("bn", "IN") // BENGALI (Default local accent)
            }

            val langResult = engine.setLanguage(locale)
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("SonarBanglaVM", "Regional Language ${_voiceLanguage.value} is missing files or not supported on this device.")
                // Fallback to standard Locale
                engine.setLanguage(Locale("bn"))
            }

            // High compatibility settings: Adjusting different synth voice profiles
            val basePitch = _voicePitch.value
            val adjustedPitch = when (_voiceGender.value) {
                "FEMALE" -> basePitch * 1.25f // Raise pitch
                "CHILD" -> basePitch * 1.6f  // Brighter cute pitch
                "ROBOT" -> basePitch * 0.55f  // Deep metallic pitch
                "DEEP_BASS" -> basePitch * 0.72f // Rich deep bass pitch
                "SOFT_NEURAL" -> basePitch * 1.05f // Soft pleasing pitch
                else -> basePitch * 0.82f // "MALE" - Deep baritone pitch
            }
            engine.setPitch(adjustedPitch)

        } catch (e: Exception) {
            Log.e("SonarBanglaVM", "Error applying voice configurations to engine", e)
        }
    }

    // Speak response text out loud in regional voice context
    fun speakText(text: String) {
        val engine = tts ?: return
        if (!isTtsInitialized) {
            Toast.makeText(getApplication(), "Voice synthesizer model is starting...", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            applyTtsSettings()
            _isSpeaking.value = true
            
            // For sweet presentation, remove markdown icons/prompts from reading sequence
            val cleanText = text
                .replace(Regex("[*#_`🙏✨🌸🌿]"), "")
                .replace("Nomoshkar", "Hello")
                .replace("Kemon achen", "How are you")

            // Speak behavior
            engine.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "sonar_bangla_speech_id")
        }
    }

    // Stop speaking immediately
    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }

    // --- Interactive Business Actions ---

    fun updateTheme(newTheme: String) {
        viewModelScope.launch {
            repository.saveTheme(newTheme)
            _themeMode.value = newTheme
        }
    }

    fun updateVoiceGender(gender: String) {
        viewModelScope.launch {
            repository.saveVoiceGender(gender)
            _voiceGender.value = gender
            applyTtsSettings()
        }
    }

    fun updateVoiceSpeed(speed: Float) {
        viewModelScope.launch {
            repository.saveVoiceSpeed(speed)
            _voiceSpeed.value = speed
            applyTtsSettings()
        }
    }

    fun updateVoicePitch(pitch: Float) {
        viewModelScope.launch {
            repository.saveVoicePitch(pitch)
            _voicePitch.value = pitch
            applyTtsSettings()
        }
    }

    fun updateVoiceLanguage(lang: String) {
        viewModelScope.launch {
            repository.saveVoiceLanguage(lang)
            _voiceLanguage.value = lang
            applyTtsSettings()
        }
    }

    fun updateDarkModePref(pref: String) {
        viewModelScope.launch {
            repository.saveDarkModePref(pref)
            _darkModePref.value = pref
        }
    }

    fun updateHapticEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveHapticEnabled(enabled)
            _hapticEnabled.value = enabled
        }
    }

    fun updateSpeechAutoReadEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveSpeechAutoReadEnabled(enabled)
            _speechAutoReadEnabled.value = enabled
            if (!enabled) {
                stopSpeaking()
            }
        }
    }

    fun updateGlowEffectEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveGlowEffectEnabled(enabled)
            _glowEffectEnabled.value = enabled
        }
    }

    fun updateStreamTextEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveStreamTextEnabled(enabled)
            _streamTextEnabled.value = enabled
        }
    }

    fun updateCreativityTemp(temp: Float) {
        viewModelScope.launch {
            repository.saveCreativityTemp(temp)
            _creativityTemp.value = temp
        }
    }

    // Send chat messages to Bangla AI
    fun sendChatMessage(prompt: String, userAttachedImage: String? = null) {
        if (prompt.trim().isEmpty() && userAttachedImage == null) return

        viewModelScope.launch {
            // 1. Insert user message to database
            repository.insertChatMessage("USER", prompt, userAttachedImage)
            _isChatLoading.value = true

            // Get conversational history for context
            val currentList = chatMessages.value
            
            // 2. Fetch answer from Gemini
            val reply = repository.getChatAnswer(prompt, currentList)

            // Check if it's an image request
            val lowerPrompt = prompt.lowercase()
            val isImageRequest = lowerPrompt.contains("generate ") || 
                                 lowerPrompt.contains("draw ") || 
                                 lowerPrompt.contains("picture ") || 
                                 lowerPrompt.contains("image ") || 
                                 lowerPrompt.contains("photo ") || 
                                 lowerPrompt.contains("ghibli") || 
                                 lowerPrompt.contains("anime") || 
                                 lowerPrompt.contains("cyberpunk")
                                 
            val finalImageUrl = if (isImageRequest) {
                // Generate a Pollinations image link
                val encodedPrompt = android.net.Uri.encode(prompt)
                "https://image.pollinations.ai/prompt/$encodedPrompt"
            } else {
                null
            }

            // 3. Save AI answer to database WITH potential image url
            repository.insertChatMessage("AI", reply, finalImageUrl)
            _isChatLoading.value = false

            // Auto-speak responses if user settings has enabled audio reading
            if (_speechAutoReadEnabled.value) {
                speakText(reply)
            }
        }
    }

    // Clear entire chat history
    fun clearChatMessages() {
        viewModelScope.launch {
            stopSpeaking()
            repository.clearHistory()
        }
    }

    // Traditional Art Picture Generation Trigger
    fun generateTraditionalArt(prompt: String, forcedType: String? = null) {
        if (prompt.trim().isEmpty()) return

        viewModelScope.launch {
            _isArtGenerating.value = true
            _currentGeneratedArt.value = null
            _artGeneratingProgress.value = 0f
            _artGeneratingSecondsLeft.value = 29
            _artGeneratingStepText.value = "Powering up NeoCore Neural Canvas engine..."

            // Determine Bengali art category based on prompt context keywords
            val lowerPrompt = prompt.lowercase()
            val finalArtType = forcedType ?: when {
                lowerPrompt.contains("cyber") || lowerPrompt.contains("grid") || lowerPrompt.contains("network") || lowerPrompt.contains("circuit") || lowerPrompt.contains("ai") || lowerPrompt.contains("digital") -> "CYBER_GRID"
                lowerPrompt.contains("tiger") || lowerPrompt.contains("bagh") || lowerPrompt.contains("predator") || lowerPrompt.contains("sundarban") -> "TIGER"
                lowerPrompt.contains("baul") || lowerPrompt.contains("singer") || lowerPrompt.contains("mystic") || lowerPrompt.contains("music") || lowerPrompt.contains("ektara") -> "BAUL"
                lowerPrompt.contains("bridge") || lowerPrompt.contains("howrah") || lowerPrompt.contains("kolkata") || lowerPrompt.contains("river") || lowerPrompt.contains("hooghly") -> "HOWRAH"
                else -> "ALPANA" // Default traditional folk floral pattern
            }

            // Request Gemini description in the background with regular launch
            var gTitle = ""
            var gDesc = ""
            val descJob = launch(Dispatchers.IO) {
                try {
                    val (title, description) = repository.generateArtworkDescription(prompt, finalArtType)
                    gTitle = title
                    gDesc = description
                } catch (e: Exception) {
                    Log.e("SonarBanglaVM", "Failed to retrieve description", e)
                    gTitle = "Bengal Heritage Masterpiece"
                    gDesc = "A stunning custom-crafted traditional artwork honoring West Bengal."
                }
            }

            // Accurate 29-second ticking loop (29000ms in 100ms chunks)
            val totalMills = 29000L
            val interval = 100L
            var elapsed = 0L

            while (elapsed < totalMills) {
                kotlinx.coroutines.delay(interval)
                elapsed += interval
                
                val rawProgress = elapsed.toFloat() / totalMills.toFloat()
                _artGeneratingProgress.value = rawProgress.coerceAtMost(1f)
                
                val secondsLeft = ((totalMills - elapsed) / 1000L).toInt() + 1
                _artGeneratingSecondsLeft.value = secondsLeft.coerceIn(1, 29)

                _artGeneratingStepText.value = when {
                    rawProgress < 0.10f -> "Powering up NeoCore Neural Canvas engine..."
                    rawProgress < 0.20f -> "Deep-scanning prompt elements and regional symbols..."
                    rawProgress < 0.30f -> "Synthesizing authentic Ganges clay & vermillion patterns..."
                    rawProgress < 0.40f -> "Configuring high-fidelity procedural vector grids..."
                    rawProgress < 0.50f -> "Drafting traditional Patachitra borders and marigold alignments..."
                    rawProgress < 0.60f -> "Tuning woodcraft Ektara strings for audio-reactive frequencies..."
                    rawProgress < 0.70f -> "Assembling Bengal heritage silhouettes and sacred elements..."
                    rawProgress < 0.80f -> "Applying deep shades, dynamic shadows, and glowing highlights..."
                    rawProgress < 0.90f -> "Compiling complex procedural vector layers on canvas..."
                    else -> "Finalizing masterwork preservation inside Room database..."
                }
            }

            descJob.join()
            if (gTitle.isEmpty()) {
                gTitle = "Bengal Heritage Masterpiece"
                gDesc = "A stunning custom-crafted traditional artwork honoring West Bengal."
            }

            // Create custom artwork entity with styleSeed
            val newArt = ArtworkEntity(
                prompt = prompt,
                title = gTitle,
                description = gDesc,
                artType = finalArtType,
                styleSeed = (1000..99999).random().toLong()
            )

            // Save to persistent SQLite Gallery
            repository.insertArtwork(newArt)
            _currentGeneratedArt.value = newArt
            _isArtGenerating.value = false

            // Speak the artwork description if enabled
            if (_speechAutoReadEnabled.value) {
                speakText("Here is your newly generated artwork: ${newArt.title}. ${newArt.description}")
            }
        }
    }

    // Delete single masterpiece from Gallery
    fun deleteMasterpiece(id: Int) {
        viewModelScope.launch {
            repository.deleteArtwork(id)
            if (_currentGeneratedArt.value?.id == id) {
                _currentGeneratedArt.value = null
            }
        }
    }

    // Clean memory
    override fun onCleared() {
        super.onCleared()
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("SonarBanglaVM", "Error shutting down TTS service", e)
        }
    }
}
