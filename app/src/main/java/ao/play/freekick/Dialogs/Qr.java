package ao.play.freekick.Dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import ao.play.freekick.R;

public class Qr {
    private static ProgressDialog qr;

    public static void showQrDialog(Context context, Bitmap bitmap) {
        qr = new ProgressDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.qr_dialog_layout, null);

        ImageView qrImageView = view.findViewById(R.id.qr_image_view);

        qrImageView.setImageBitmap(bitmap);

        qr.show();
        qr.setContentView(view);
        qr.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
