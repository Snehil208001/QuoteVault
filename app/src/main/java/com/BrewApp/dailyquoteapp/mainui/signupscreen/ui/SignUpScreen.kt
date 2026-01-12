package com.BrewApp.dailyquoteapp.mainui.signupscreen.ui


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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.BrewApp.dailyquoteapp.mainui.signupscreen.viewmodel.SignUpState
import com.BrewApp.dailyquoteapp.mainui.signupscreen.viewmodel.SignUpViewModel
import com.BrewApp.dailyquoteapp.ui.theme.InterFont
import com.BrewApp.dailyquoteapp.ui.theme.PlayfairFont
import com.BrewApp.dailyquoteapp.ui.theme.PrimaryBlue

// Color definitions specific to this screen's design
private val BackgroundLight = Color(0xFFF6F7F8)
private val TextDark = Color(0xFF0D141B)
private val TextMutedBlue = Color(0xFF4C739A)
private val InputBorder = Color(0xFFCFDBE7)
private val InputPlaceholder = Color(0xFF93ADC8)

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpSubmit: () -> Unit,
    viewModel: SignUpViewModel = viewModel()
) {
    val signUpState by viewModel.signUpState.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle signup state changes
    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.Success -> {
                onSignUpSubmit()
                viewModel.resetState()
            }
            is SignUpState.Error -> {
                snackbarHostState.showSnackbar((signUpState as SignUpState.Error).message)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        containerColor = BackgroundLight,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight.copy(alpha = 0.9f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Transparent, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextDark
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Limits width for larger screens (max-w-md)
            Column(
                modifier = Modifier.width(448.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Headline Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        text = "Begin your journey",
                        fontFamily = PlayfairFont,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 36.sp,
                        color = TextDark,
                        lineHeight = 40.sp,
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = "Daily wisdom awaits.",
                        fontFamily = InterFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = TextMutedBlue
                    )
                }

                // 2. Form Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Full Name
                    SignUpTextField(
                        value = fullName,
                        onValueChange = { viewModel.updateFullName(it) },
                        label = "Full Name",
                        placeholder = "Jane Doe",
                        keyboardType = KeyboardType.Text,
                        enabled = signUpState != SignUpState.Loading
                    )

                    // Email Address
                    SignUpTextField(
                        value = email,
                        onValueChange = { viewModel.updateEmail(it) },
                        label = "Email Address",
                        placeholder = "jane@example.com",
                        keyboardType = KeyboardType.Email,
                        enabled = signUpState != SignUpState.Loading
                    )

                    // Password
                    SignUpTextField(
                        value = password,
                        onValueChange = { viewModel.updatePassword(it) },
                        label = "Password",
                        placeholder = "••••••••",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        isPasswordVisible = passwordVisible,
                        onVisibilityChange = { passwordVisible = !passwordVisible },
                        enabled = signUpState != SignUpState.Loading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit Button
                    Button(
                        onClick = { viewModel.signUp() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 2.dp
                        ),
                        enabled = signUpState != SignUpState.Loading
                    ) {
                        if (signUpState == SignUpState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Create Account",
                                fontFamily = InterFont,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }

                    // Footer Link
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("Already a member? ")
                                withStyle(
                                    style = SpanStyle(
                                        color = PrimaryBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Log In")
                                }
                            },
                            fontFamily = InterFont,
                            fontSize = 14.sp,
                            color = TextMutedBlue,
                            modifier = Modifier.clickable { onLoginClick() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: () -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontFamily = InterFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark,
            modifier = Modifier.padding(start = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = InputPlaceholder,
                    fontFamily = InterFont,
                    fontSize = 16.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = CircleShape,
            singleLine = true,
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onVisibilityChange) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = InputPlaceholder
                        )
                    }
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = InputBorder,
                focusedTextColor = TextDark,
                unfocusedTextColor = TextDark,
                cursorColor = PrimaryBlue
            ),
            enabled = enabled
        )
    }
}