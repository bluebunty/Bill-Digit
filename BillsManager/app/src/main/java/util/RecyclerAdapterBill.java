package util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sd.billsmanager.R;

import java.io.File;
import java.util.List;

import model.ItemBill;


public class RecyclerAdapterBill extends RecyclerView.Adapter<RecyclerAdapterBill.ViewHolder> {


	private List<ItemBill> mItems;
	Context mContext;
	int mMargin = 10;
    private int lastPosition = -1;
	public RecyclerAdapterBill(Context context, List<ItemBill> objects) {
		mContext = context;
		mItems = objects;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView billImage;
        public TextView hashText;

		public ViewHolder(View itemView) {
			super(itemView);
            billImage = (ImageView)itemView.findViewById(R.id.item_image);
            hashText = (TextView)itemView.findViewById(R.id.item_hash);
		}
	}

	@Override
	public int getItemCount() {

		return mItems.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

        ItemBill billItem= mItems.get(position);
        holder.billImage.setImageURI(Uri.parse(new File(billItem.imagePath).toString()));
        holder.hashText.setText(billItem.hashTag);
        /*CommonComponent item = mItems.get(position);
        Picasso.with(mContext).load(item.getResId()).into(holder.mImageView);
		holder.mTextView.setText(item.getTitle());*/
	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        final ItemBill item = mItems.get(arg1);
		LayoutInflater inflater =
				(LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View convertView = inflater.inflate(R.layout.item_bill, parent, false);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) convertView.getLayoutParams();
        params.leftMargin = mMargin; params.topMargin = mMargin;
        convertView.setLayoutParams(params);

		return new ViewHolder(convertView);
	
	}

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
