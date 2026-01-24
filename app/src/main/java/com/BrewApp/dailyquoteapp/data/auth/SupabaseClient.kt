package com.BrewApp.dailyquoteapp.data.auth

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://grgvxfbfkcitalbcwvkv.supabase.co",
        supabaseKey = "sb_publishable_1wAs3sgB13ifWYmsmFS-PQ_Qe_2WNuT"
    ) {
        // --- FIX: Ignore extra fields (like 'created_at') from Supabase ---
        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
        })

        install(Auth) {
            flowType = FlowType.PKCE
            // FIXED: Updated to match the redirect URL
            scheme = "io.supabase.dailyquoteapp"
            host = "login-callback"
            // Enable automatic token refresh
            autoLoadFromStorage = true
            autoSaveToStorage = true
        }
        install(Postgrest)
        install(Storage)
    }
}