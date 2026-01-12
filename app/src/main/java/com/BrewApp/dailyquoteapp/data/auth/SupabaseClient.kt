package com.BrewApp.dailyquoteapp.data.auth

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://grgvxfbfkcitalbcwvkv.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdyZ3Z4ZmJma2NpdGFsYmN3dmt2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzY3Mzk1OTYsImV4cCI6MjA1MjMxNTU5Nn0.sb_publishable_1wAs3sgB13ifWYmsmFS-PQ_Qe_2WNuT"
    ) {
        install(Auth)
        install(Postgrest)
    }
}