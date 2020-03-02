package be.ehb.demo_filesystem_hellocamera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //magic numbers are bad text ipv cijfers bij request code
    final  int IMAGE_REQUEST = 1000;

    //UI
    private ImageView ivProfile;
    //listeners
    private View.OnClickListener profileIVListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dispatchTakePicture();
        }
    };
    //eenfield van gemaakt om gemakkelijker opnieuw te kunnen gebruiken
    private File currentImage;

    private void dispatchTakePicture() {
        Intent picureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //nakijken of er wel een camera zit in het toestel
        if(picureIntent.resolveActivity(getPackageManager()) != null){
            currentImage = createImageFile();
            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), "be.ehb.demo_filesystem_hellocamera.fileprovider", currentImage);
            picureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(picureIntent, IMAGE_REQUEST);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            Picasso.get()
                    .load(currentImage)
                    .resize(200,200)
                    .centerCrop()
                    .into(ivProfile);
        }


    }

    private File createImageFile(){
        String filename = "/"+ System.currentTimeMillis()+"pic_profile.jpeg";
        File storageDirectory = getFilesDir();

        return new File(storageDirectory + filename);
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivProfile = findViewById(R.id.iv_photo);
        ivProfile.setOnClickListener(profileIVListener);

    }
}
