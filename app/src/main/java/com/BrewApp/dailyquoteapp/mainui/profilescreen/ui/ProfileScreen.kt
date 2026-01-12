package com.BrewApp.dailyquoteapp.mainui.profilescreen.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.BrewApp.dailyquoteapp.ui.theme.InterFont
import com.BrewApp.dailyquoteapp.ui.theme.PlayfairFont
import com.BrewApp.dailyquoteapp.ui.theme.PrimaryBlue

// Color definitions specific to the Profile design
private val CreamBg = Color(0xFFFDFCF5)
private val CreamSurface = Color(0xFFF7F5EB)
private val TextPrimary = Color(0xFF1F2937)
private val TextSecondary = Color(0xFF6B7280)
private val BorderColor = Color(0xFFE7E5E4) // Stone-200 equivalent
private val RedText = Color(0xFFEF4444)
private val RedBgHover = Color(0xFFFEF2F2)
private val RedBorderHover = Color(0xFFFEE2E2)

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        containerColor = CreamBg,
        topBar = {
            // Sticky Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CreamBg)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Transparent, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "Profile",
                    fontFamily = PlayfairFont,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 40.dp), // Balance the title to center
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
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
            // Max width constraint for larger screens
            Column(
                modifier = Modifier
                    .width(448.dp) // max-w-md
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                // 1. Profile Header
                Box(contentAlignment = Alignment.BottomEnd) {
                    // Avatar Image Placeholder
                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .shadow(elevation = 10.dp, shape = CircleShape)
                            .background(Color.Gray, CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Using Icon as placeholder for "https://lh3.googleusercontent.com/aida-public/AB6AXuDQwgOrJDtiXd5FlplFhNeHI0grHZrCoN2-uk3KZ2d5NoDzBHG7e60y8cOAxdRwg3S7xbuEenzTNQeq9oQH5VcZWSBCb-EDO55Oj_tkAkJk9MsFZlbiMRpQ61W5YHLm04r99TYzvBr_70enhLeckemTfg9kPspLWGWyHaeHka9GWIeMlBfpSjFDg1YofW5MTxUC3ue_tMGc58fLyNitACPLSbLCsjoW-1OIdT_4Jb0VAMGaf5pxP_rnMu4g_hmoBxjTiptWVfumVqQi"
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Picture",
                            tint = Color.White,
                            modifier = Modifier.size(80.dp)
                        )
                    }

                    // Edit Badge
                    Box(
                        modifier = Modifier
                            .padding(bottom = 4.dp, end = 4.dp)
                            .size(36.dp)
                            .background(PrimaryBlue, CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                            .clickable { onEditProfileClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Name and Email
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Jane Doe",
                        fontFamily = PlayfairFont,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "jane.doe@example.com",
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 2. Stats Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileStatCard(
                        modifier = Modifier.weight(1f),
                        count = "124",
                        label = "Quotes Saved"
                    )
                    ProfileStatCard(
                        modifier = Modifier.weight(1f),
                        count = "8",
                        label = "Collections"
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 3. Menu List
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileMenuItem(
                        icon = Icons.Default.Settings,
                        label = "Account Settings",
                        onClick = onSettingsClick
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.Notifications,
                        label = "Notifications",
                        onClick = onNotificationsClick
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.Favorite,
                        label = "Quote Preferences",
                        onClick = onPreferencesClick
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 4. Logout Button
                Surface(
                    onClick = onLogoutClick,
                    shape = RoundedCornerShape(50),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            tint = RedText,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Log Out",
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RedText,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Version
                Text(
                    text = "Version 2.4.0",
                    fontFamily = InterFont,
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileStatCard(
    modifier: Modifier = Modifier,
    count: String,
    label: String
) {
    Column(
        modifier = modifier
            .background(CreamSurface, RoundedCornerShape(16.dp))
            .border(1.dp, BorderColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count,
            fontFamily = PlayfairFont,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label.uppercase(),
            fontFamily = InterFont,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6)), // Stone-100
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp)), // Blue-50
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Label
            Text(
                text = label,
                fontFamily = InterFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            // Chevron
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF9CA3AF) // Gray-400
            )
        }
    }
}