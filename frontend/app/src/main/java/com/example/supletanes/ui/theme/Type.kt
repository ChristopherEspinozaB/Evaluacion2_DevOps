package com.example.supletanes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.supletanes.R

//1.- Definir las familias de fuentes
val RobotoFamily = FontFamily.Default //Roboto es la fuente por defecto
val BebasNeueFamily = FontFamily(
    Font(R.font.bebas_neue_regular, FontWeight.Normal) //Fuente personalizada
)

//2.- Definir los estilos de texto usando las fuentes y colores
val AppTypography = Typography(
    //Estilos para Títulos. Usaremos Bebas Neue
    displayLarge = TextStyle(
        fontFamily = BebasNeueFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
        color = AzulAcero //Color para títulos principales
    ),
    headlineLarge = TextStyle(
        fontFamily = BebasNeueFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        color = AzulAcero
    ),
    headlineMedium = TextStyle(
        fontFamily = BebasNeueFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        color = AzulAcero
    ),

    //Estilos para Texto de cuerpo y botones. Usaremos Roboto
    bodyLarge = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    //Estilo para botones
    labelLarge = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
)