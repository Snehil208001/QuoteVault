package com.BrewApp.dailyquoteapp.mainui.loginscreen.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.BrewApp.dailyquoteapp.ui.theme.BackgroundCream
import com.BrewApp.dailyquoteapp.ui.theme.InterFont
import com.BrewApp.dailyquoteapp.ui.theme.PlayfairFont
import com.BrewApp.dailyquoteapp.ui.theme.PrimaryBlue
import com.BrewApp.dailyquoteapp.ui.theme.TextMuted
import com.BrewApp.dailyquoteapp.ui.theme.TextPrimary
import com.BrewApp.dailyquoteapp.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Main Container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCream),
        contentAlignment = Alignment.Center
    ) {
        // --- Decorative Background Blobs ---
        // Top-Right Blue Blob
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = (-20).dp)
                .size(256.dp)
                .blur(radius = 60.dp) // Creates the blur-3xl effect
                .background(PrimaryBlue.copy(alpha = 0.05f), CircleShape)
        )

        // Center-Left Orange/Cream Blob
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-40).dp)
                .size(192.dp)
                .blur(radius = 60.dp)
                .background(Color(0xFFFFE0B2).copy(alpha = 0.4f), CircleShape) // Light Orange
        )

        // --- Main Content ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .width(420.dp), // Max width constraint from design
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Header Section
            // Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(PrimaryBlue.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.FormatQuote,
                    contentDescription = "Logo",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Welcome Back",
                fontFamily = PlayfairFont, // Matches 'Newsreader' serif style
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Sign in to continue your daily journey",
                fontFamily = InterFont, // Matches 'Noto Sans'
                fontSize = 18.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 2. Form Section
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Email Input
                LoginTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    placeholder = "hello@example.com",
                    icon = Icons.Outlined.Email,
                    keyboardType = KeyboardType.Email
                )

                // Password Input
                LoginTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "Enter your password",
                    icon = Icons.Outlined.Lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    isPasswordVisible = passwordVisible,
                    onVisibilityChange = { passwordVisible = !passwordVisible }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Action Button
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Log In",
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4. Footer Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Forgot Password
                Text(
                    text = "Forgot Password?",
                    fontFamily = InterFont,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary,
                    modifier = Modifier
                        .clickable { onForgotPasswordClick() }
                )

                // Divider Line
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(1.dp)
                        .background(Color(0xFFE2E8F0)) // Slate-200
                )

                // Sign Up Link
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account? ",
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        color = Color(0xFF475569) // Slate-600
                    )
                    Text(
                        text = "Sign Up",
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        modifier = Modifier.clickable { onSignUpClick() }
                    )
                }
            }
        }
    }
}

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        // Label
        Text(
            text = label,
            fontFamily = InterFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF334155), // Slate-700
            modifier = Modifier.padding(start = 16.dp)
        )

        // Input Field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = TextMuted,
                    fontFamily = InterFont
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextMuted
                )
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onVisibilityChange) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = TextMuted
                        )
                    }
                }
            } else null,
            singleLine = true,
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.5f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                disabledContainerColor = Color.White.copy(alpha = 0.5f),
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = Color(0xFFE2E8F0), // Slate-200
                cursorColor = PrimaryBlue,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}