package com.tux.dms.fragment.tickets;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tux.dms.R;
import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.MimeTypeConst;
import com.tux.dms.dto.ImageUploadResponse;
import com.tux.dms.dto.Ticket;
import com.tux.dms.fragment.dashboard.AdminDashboardFragment;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketCreateFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    AlertDialog.Builder mBuilder;
    View mView;
    ProgressDialog progressDialog;
    Button btnYes, btnNo;
    Button scanButton;
    Button createTicket;
    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    Bitmap bmp;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    List<String> imagePaths = new ArrayList<>();
    List<String> pdfPaths = new ArrayList<>();

    String[] sources = {"State", "District", "Sub-Division", "Gram Panchayat", "Other Block Offices", "Others"};
    Spinner sourceSpiner;
    String sourceText;
    EditText subjectText;
    TextView ticketSuccess;
    AlertDialog dialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TicketCreateFragment() {
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
    public static TicketCreateFragment newInstance(String param1, String param2) {
        TicketCreateFragment fragment = new TicketCreateFragment();
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
        mBuilder = new AlertDialog.Builder(getContext());
        mView = getLayoutInflater().inflate(R.layout.dialog_layout, container,false);
        mBuilder.setView(mView);
        dialog = mBuilder.create();

        btnYes = (Button) mView.findViewById(R.id.btnYes);
        btnNo = (Button) mView.findViewById(R.id.btnNo);
        ticketSuccess = (TextView) mView.findViewById(R.id.ticketSuccessTextView);

        sourceSpiner = view.findViewById(R.id.source);
        sourceSpiner.setOnItemSelectedListener(this);

        ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, sources);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpiner.setAdapter(ad);

        scanButton = view.findViewById(R.id.buttonScan);
        subjectText = view.findViewById(R.id.assignSubjectText);
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
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Creating Ticket");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                postTicket(subjectText.getText().toString(), sourceText, imagePaths , pdfPaths);
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

        final CharSequence[] items = {"Camera","PDF", "Gallery", "Cancel"};

        //alert dialog giving three options: Add Image from Camera,Gallery,Cancel
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                else if(items[i].equals("PDF")){
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setType("application/pdf");
                        startActivityForResult(Intent.createChooser(intent,"Select PDF"), SELECT_FILE);
                }
                else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,"Select Pictures"), SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");
                try {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    handleAttachment(Arrays.asList(baos.toByteArray()), MimeTypeConst.imageMimeTypeInRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                if (data.getClipData() != null) {
                    List<byte[]> imageDataList = new ArrayList<>();
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    String mimeType = data.getClipData().getDescription().getMimeType(0);
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageDataList.add(getBytes(imageUri));
                    }
                    handleAttachment(imageDataList, mimeType);
                } else if (data.getData() != null) {
                    String imagePath = data.getData().getPath();
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }
        }

    }

    /**
     * get array of byte from uri of file (image/pdf)
     * @param uri
     * @return
     */
    private byte[] getBytes(Uri uri) {
        try {
            InputStream inputStream = getActivity()
                    .getApplicationContext().getContentResolver().openInputStream(uri);
            return readBytes(inputStream);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * get array of bytes from input stream
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    private void handleAttachment(List<byte[]> imageDataList, String mimeType) {

        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < imageDataList.size(); i++) {
            MultipartBody.Part body = null;
            switch (mimeType) {
                case MimeTypeConst.imageMimeTypeInRequest:
                    RequestBody requestFile = RequestBody.create(MediaType.parse(MimeTypeConst.imageMimeType),
                            imageDataList.get(i));
                    String fileName = "attachment" + i + ".jpg";
                    body = MultipartBody.Part.createFormData("image", fileName, requestFile);
                    break;
                case MimeTypeConst.pdfMimeType:
                     requestFile = RequestBody.create(MediaType.parse(MimeTypeConst.pdfMimeType),
                            imageDataList.get(i));
                    fileName = "attachment" + i + ".pdf";
                    body = MultipartBody.Part.createFormData("image", fileName, requestFile);
                    break;
            }
            parts.add(body);
        }
        uploadAttachment(parts);
    }

    private void uploadAttachment(List<MultipartBody.Part> parts) {

        String token = sessionCache.getToken();
        Call<ImageUploadResponse> call = apiInterface.uploadImage(token, parts);
        final ProgressDialog progressDialogUpload;
        progressDialogUpload = new ProgressDialog(getContext());
        progressDialogUpload.setTitle("Uploading . . .");
        progressDialogUpload.show();

        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                imagePaths.addAll(response.body().getImg());
                pdfPaths.addAll(response.body().getPdf());
                progressDialogUpload.dismiss();
                Toast.makeText(getContext(), "image scanned/attached",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {

            }
        });
    }

    private void postTicket(String subjectStr, String sourceStr, List<String> imagePaths, List<String> pdfPaths) {

        Ticket ticketBody = new Ticket();
        ticketBody.setSubject(subjectStr);
        ticketBody.setSource(sourceStr);
        ticketBody.setImageFilePath(imagePaths);
        ticketBody.setPdfFilePath(pdfPaths);
        String token = sessionCache.getToken();
        Call<Ticket> ticketCall = apiInterface.createTicket(token, ticketBody);

        ticketCall.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                ticketSuccess.setText("Ticket Created Successfully" + " " + response.body().getDocketId());
                progressDialog.dismiss();
                dialog.show();
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        subjectText.setText("");
                        sourceSpiner.setSelection(0);
                        dialog.dismiss();
                        imagePaths.clear();
                        pdfPaths.clear();
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AdminDashboardFragment adminDashboardFragment = new AdminDashboardFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, adminDashboardFragment);
                        fragmentTransaction.commit();
                        dialog.dismiss();

                    }
                });

            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {

            }
        });
    }
}