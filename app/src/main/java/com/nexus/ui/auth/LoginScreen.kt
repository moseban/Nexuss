package com.nexus.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginClick: (String, String) -> Unit
) {
    var emailStr by remember { mutableStateOf("") }
    var passwordStr by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Área del Logotipo y Título
        Icon(
            // Nota: El SVG real del chip debe importarse en res/drawable y llamarse con painterResource
            imageVector = Icons.Default.Email,
            contentDescription = "Logo",
            tint = TextPrimary,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Nexus Finance",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = "✨ Powered by Gemini 2.5 flash",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 48.dp)
        )

        // Contenedor de Formularios
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SurfaceBorder, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            // Input de Correo
            OutlinedTextField(
                value = emailStr,
                onValueChange = { emailStr = it },
                placeholder = { Text("user@gemini.ai", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextSecondary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = NexusPurple,
                    cursorColor = NexusGreen
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input de Contraseña
            OutlinedTextField(
                value = passwordStr,
                onValueChange = { passwordStr = it },
                placeholder = { Text("••••••••", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondary) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = NexusPurple,
                    cursorColor = NexusGreen
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Principal de Sincronización (Gradiente)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PrimaryGradient)
                    .clickable { onLoginClick(emailStr, passwordStr) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sincronizar →",
                    color = Color.Black, // Contraste oscuro sobre colores brillantes
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Navegación a Registro
        Row(
            modifier = Modifier.clickable { onNavigateToRegister() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "¿Nueva entidad? ", color = TextSecondary, fontSize = 14.sp)
            Text(text = "Inicializar perfil", color = NexusPurple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppNexusTheme {
        LoginScreen(onNavigateToRegister = {}, onLoginClick = { _, _ -> })
    }
}