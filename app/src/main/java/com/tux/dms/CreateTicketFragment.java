package com.tux.dms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tux.dms.cache.SessionCache;
import com.tux.dms.dto.ImageUploadResponse;
import com.tux.dms.dto.Ticket;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTicketFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Button scanButton ;
    Button createTicket ;
    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    Bitmap bmp;
    ByteArrayOutputStream baos=new ByteArrayOutputStream();
    byte[] imageData;
    String imagePath;

    String[] sources = { "State", "District","Sub-Division","Gram Panchayat","Other Block Offices","Others" };
    Spinner sourceSpiner;
    String sourceText;
    EditText subjectText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateTicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateTicketFragment newInstance(String param1, String param2) {
        CreateTicketFragment fragment = new CreateTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_ticket, container, false);
        sourceSpiner = view.findViewById(R.id.source);
        sourceSpiner.setOnItemSelectedListener(this);

        ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, sources);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpiner.setAdapter(ad);

        scanButton = view.findViewById(R.id.buttonScan);
        subjectText = view.findViewById(R.id.subjectText);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        createTicket = (Button) view.findViewById(R.id.buttonCreateTicket);

        createTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(imageData);
            }
        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        sourceText = sourceSpiner.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        //alert dialog giving three options: Add Image from Camera,Gallery,Cancel
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");
               // ivImage.setImageBitmap(bmp);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                imageData = baos.toByteArray();
                Toast.makeText(getContext(),imageData.toString(),Toast.LENGTH_LONG).show();
                //uploadImage(imageData);
            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();
                //ivImage.setImageURI(selectedImageUri);
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),
                            selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                imageData = baos.toByteArray();
                Toast.makeText(getContext(),imageData.toString(),Toast.LENGTH_LONG).show();
                //uploadImage(imageData);
            }

        }

    }

    private void uploadImage(byte[] imageBytes) {
        System.out.println("upload image");

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

        Call<ImageUploadResponse> call = apiInterface.uploadImage(body);

        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                Toast.makeText(getContext(), "image uploaded/scanned",
                        Toast.LENGTH_LONG).show();
                postTicket(subjectText.getText().toString(),sourceText , response.body().getPath());
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {

            }
        });
    }

    private void postTicket(String subjectStr, String sourceStr, String imagePath) {

        Ticket ticketBody = new Ticket();
        ticketBody.setSubject(subjectStr);
        ticketBody.setSource(sourceStr);
        ticketBody.setFilePath(imagePath);
        String token = sessionCache.getToken();
        Call<Ticket> ticketCall = apiInterface.createTicket(token, ticketBody);

        ticketCall.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                Toast.makeText(getContext(), "Ticket created successfully",
                        Toast.LENGTH_LONG).show();
                
            }
            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {

            }
        });
    }
}