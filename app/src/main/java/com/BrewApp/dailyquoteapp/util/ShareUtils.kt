package com.BrewApp.dailyquoteapp.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Toast
import androidx.core.content.FileProvider
import com.BrewApp.dailyquoteapp.data.model.Quote
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

enum class QuoteStyle {
    CLASSIC, MODERN, ARTISTIC
}

object ShareUtils {

    fun shareText(context: Context, quote: Quote) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            // Updated to "QuoteVault"
            putExtra(Intent.EXTRA_TEXT, "\"${quote.text}\"\n- ${quote.author}\n\nVia QuoteVault")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Quote")
        context.startActivity(shareIntent)
    }

    fun shareImage(context: Context, bitmap: Bitmap) {
        val uri = saveImageToCache(context, bitmap)
        if (uri != null) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share Quote Card"))
        } else {
            Toast.makeText(context, "Failed to prepare image", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImageToGallery(context: Context, bitmap: Bitmap) {
        val filename = "Quote_${System.currentTimeMillis()}.png"
        var fos: OutputStream? = null
        var imageUri: Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    // Updated folder name to "QuoteVault"
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QuoteVault")
                }
                imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (imageUri != null) {
                    fos = context.contentResolver.openOutputStream(imageUri)
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                // Updated folder name to "QuoteVault"
                val appDir = File(imagesDir, "QuoteVault")
                if (!appDir.exists()) appDir.mkdirs()
                val image = File(appDir, filename)
                fos = FileOutputStream(image)
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToCache(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val stream = FileOutputStream("$cachePath/share_image.png")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            val authority = "${context.packageName}.fileprovider"
            FileProvider.getUriForFile(context, authority, File(cachePath, "share_image.png"))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // --- Image Generation Logic ---
    fun generateQuoteBitmap(context: Context, quote: Quote, style: QuoteStyle): Bitmap {
        val width = 1080
        val height = 1080
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

        // 1. Background
        when (style) {
            QuoteStyle.CLASSIC -> canvas.drawColor(Color.WHITE)
            QuoteStyle.MODERN -> canvas.drawColor(Color.parseColor("#1E293B")) // Slate 800
            QuoteStyle.ARTISTIC -> canvas.drawColor(Color.parseColor("#FFFDD0")) // Cream
        }

        // 2. Styling Config
        val textColor = when (style) {
            QuoteStyle.CLASSIC -> Color.BLACK
            QuoteStyle.MODERN -> Color.WHITE
            QuoteStyle.ARTISTIC -> Color.parseColor("#4A3B2A") // Dark Brown
        }

        val accentColor = when (style) {
            QuoteStyle.CLASSIC -> Color.parseColor("#2563EB") // Blue
            QuoteStyle.MODERN -> Color.parseColor("#94A3B8") // Slate 400
            QuoteStyle.ARTISTIC -> Color.parseColor("#D4AF37") // Gold
        }

        // 3. Draw Decorative Elements
        if (style == QuoteStyle.ARTISTIC) {
            paint.color = accentColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 20f
            canvas.drawRect(40f, 40f, width - 40f, height - 40f, paint)
        }

        // 4. Draw Quote Text
        textPaint.color = textColor
        textPaint.textSize = 60f
        textPaint.typeface = when (style) {
            QuoteStyle.CLASSIC -> Typeface.create(Typeface.SERIF, Typeface.BOLD)
            QuoteStyle.MODERN -> Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            QuoteStyle.ARTISTIC -> Typeface.create(Typeface.SERIF, Typeface.ITALIC)
        }

        val textLayout = StaticLayout.Builder.obtain(quote.text, 0, quote.text.length, textPaint, width - 160)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(0f, 1.2f)
            .build()

        canvas.save()
        // Center Vertically
        val textY = (height - textLayout.height) / 2f - 50f
        canvas.translate(80f, textY)
        textLayout.draw(canvas)
        canvas.restore()

        // 5. Draw Author
        val authorPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        authorPaint.color = if (style == QuoteStyle.MODERN) accentColor else Color.GRAY
        authorPaint.textSize = 40f
        authorPaint.textAlign = Paint.Align.CENTER
        authorPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)

        canvas.drawText("- ${quote.author}", width / 2f, textY + textLayout.height + 80f, authorPaint)

        // 6. Draw Watermark (Updated to "QuoteVault")
        val watermarkPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        watermarkPaint.color = if (style == QuoteStyle.MODERN) Color.DKGRAY else Color.LTGRAY
        watermarkPaint.textSize = 30f
        watermarkPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("QuoteVault", width / 2f, height - 60f, watermarkPaint)

        return bitmap
    }
}