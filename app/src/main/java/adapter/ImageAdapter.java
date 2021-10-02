package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tux.dms.R;
import com.tux.dms.dto.Image;
import com.tux.dms.dto.Pdf;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<Image> {

    public ImageAdapter(@NonNull Context context, ArrayList<Image> imageArrayList) {
        super(context, 0, imageArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }
        Image image = getItem(position);
//        TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
        ImageView courseIV = listitemView.findViewById(R.id.imgView);
//        courseTV.setText(pdf.getCourse_name());
        courseIV.setImageResource(image.getImg_id());
        return listitemView;
    }

}
