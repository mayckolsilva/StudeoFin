package uniftec.fabio.com.studeofin;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import uniftec.fabio.com.studeofin.databinding.ActivityMainBinding;
import uniftec.fabio.com.studeofin.fragment.PerfilFragment;
import uniftec.fabio.com.studeofin.global.Global;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public final static Integer CAMERA = 1;
    public final static Integer GALLERY = 2;
    public static Integer PROCESSID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_planejamento_financeiro, R.id.nav_gerir_receitas_despesas,
                R.id.nav_controle_saldo, R.id.nav_alerta_gastos, R.id.nav_categorias,
                R.id.nav_dicas_investimentos, R.id.nav_metas, R.id.nav_perfil, R.id.nav_sair)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        final View header = navigationView.getHeaderView(0);
        final TextView txt_usuario = (TextView) header.findViewById(R.id.nav_usuario);
        txt_usuario.setText(Global.getDesNome() + " " + Global.getDesSobreNome());

        if (Global.getImgFoto() != null) {

            final ImageView imageUsuario = header.findViewById(R.id.imageView);

            byte[] bytes = Base64.decode(Global.getImgFoto().getBytes(), Base64.DEFAULT);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(
                    bytes);
            Bitmap bmp_foto = BitmapFactory
                    .decodeStream(imageStream);
            RoundImage ri = new RoundImage(bmp_foto);
            imageUsuario.setImageDrawable(ri);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (PROCESSID == GALLERY) {
            if (resultCode == RESULT_OK) {
                try {

                    String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
                    Cursor cur = managedQuery(imageReturnedIntent.getData(), orientationColumn, null, null, null);
                    int orientation = -1;
                    if (cur != null && cur.moveToFirst()) {
                        orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
                    }

                    Bitmap bm = decodeUri(imageReturnedIntent.getData());

                    Matrix m = new Matrix();

                    if ((orientation == 180)) {
                        m.postRotate(180);
                        //m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                        // if(m.preRotate(90)){
                        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

                    } else if (orientation == 90) {
                        m.postRotate(90);
                        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

                    } else if (orientation == 270) {
                        m.postRotate(270);
                        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

                    }

                    RoundImage ri = new RoundImage(bm);

                    PerfilFragment.imageUsuario.setImageDrawable(ri);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (PROCESSID == CAMERA) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) imageReturnedIntent.getExtras()
                        .get("data");

                RoundImage ri = new RoundImage(photo);
                PerfilFragment.imageUsuario.setImageDrawable(ri);
            }

        }
    }


    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        if (false) {
            String path = selectedImage.getPath();
            int orientation;
            try {
                if (path == null) {
                    return null;
                }
                // decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                // Find the correct scale value. It should be the power of 2.
                final int REQUIRED_SIZE = 70;
                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 0;
                while (true) {
                    if (width_tmp / 2 < REQUIRED_SIZE
                            || height_tmp / 2 < REQUIRED_SIZE)
                        break;
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale++;
                }
                // decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                Bitmap bm = BitmapFactory.decodeFile(path, o2);
                Bitmap bitmap = bm;

                ExifInterface exif = new ExifInterface(path);

                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                //exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

                Matrix m = new Matrix();

                if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                    m.postRotate(180);
                    //m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                    // if(m.preRotate(90)){
                    bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                    return bitmap;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    m.postRotate(90);
                    bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                    return bitmap;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    m.postRotate(270);
                    bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                    return bitmap;
                }
                return bitmap;
            } catch (Exception e) {
                return null;
            }
        }
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap result = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

        return cropToSquare(result);
    }

    private Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final int width = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().height() : drawable.getIntrinsicHeight();

        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width,
                height <= 0 ? 1 : height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}