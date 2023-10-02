package ao.play.freekick.Classes;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRConstructor {
    public static Bitmap createQR(String text) {
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new QRCodeWriter().encode(Ciphering.encrypt(text), BarcodeFormat.QR_CODE, 780, 780);
        } catch (Exception ignored) {
        }
        if (bitMatrix != null) {
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        }
        return null;
    }

}
